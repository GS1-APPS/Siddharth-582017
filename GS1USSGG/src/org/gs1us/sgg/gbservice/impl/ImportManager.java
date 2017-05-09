package org.gs1us.sgg.gbservice.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gs1us.fileimport.CsvRowSource;
import org.gs1us.fileimport.HeadingRowRecordSource;
import org.gs1us.fileimport.HeadingRowRecordSourceSource;
import org.gs1us.fileimport.ImportException;
import org.gs1us.fileimport.PropertySetSource;
import org.gs1us.fileimport.RecordContentSummarizer;
import org.gs1us.fileimport.RecordSource;
import org.gs1us.fileimport.RecordSourcePropertySetSource;
import org.gs1us.fileimport.RecordSourceSource;
import org.gs1us.fileimport.RowKeyFactory;
import org.gs1us.fileimport.RowSource;
import org.gs1us.fileimport.RowSourceSource;
import org.gs1us.fileimport.RowSourceSourceFactory;
import org.gs1us.fileimport.SingletonRowSourceSource;
import org.gs1us.fileimport.TwoDPropertySetSource;
import org.gs1us.fileimport.ValueConditioner;
import org.gs1us.fileimport.XlsxRowSourceSource;
import org.gs1us.sgg.app.GBAppContext;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.ImportFile;
import org.gs1us.sgg.dao.ImportRecord;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeEnumValue;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AttributeType;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GBIllegalArgumentException;
import org.gs1us.sgg.gbservice.api.GBIllegalStateException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportPrevalidation;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationColumnStatus;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegment;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegmentSettings;
import org.gs1us.sgg.gbservice.api.ImportStatus;
import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.api.NoSuchImportException;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.json.InboundAttributeSet;
import org.gs1us.sgg.gbservice.json.InboundImportPrevalidation;
import org.gs1us.sgg.gbservice.json.InboundImportPrevalidationColumn;
import org.gs1us.sgg.gbservice.json.InboundImportPrevalidationSegment;
import org.gs1us.sgg.gbservice.json.InboundImportPrevalidationSegmentSettings;
import org.gs1us.sgg.gbservice.json.InboundProduct;
import org.gs1us.sgg.util.UserInputUtil;

public class ImportManager
{
    private static final String PRIMARY_KEY_ATTR_NAME = "gtin";
    private static final String XLSX_FORMAT = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    @Resource
    private GBDao m_gbDao;
    
    @Resource
    private ClockService m_clockService;
    
    @Resource
    private ProductOpsManager m_productOpsManager;
    
    private RowSourceSourceFactory m_rowSourceSourceFactory;
    
    public ImportManager()
    {
        m_rowSourceSourceFactory = new RowSourceSourceFactory();
        m_rowSourceSourceFactory.setValueConditioner(new ValueConditioner<String>()
        {
            @Override
            public String condition(String original)
            {
                return UserInputUtil.trimToNull(original);
            }
        });
    }

    public Import importUpload(GBAppContext appContext, String filename, String format, byte[] content) throws IOException, GlobalBrokerException, ImportException
    {
        ImportFile importFile = m_gbDao.newImportFile();
        importFile.setContent(content);
        m_gbDao.updateImportFile(importFile);
        ImportRecord importRecord = m_gbDao.newImportRecord();
        importRecord.setGBAccountGln(appContext.getGbAccount().getGln());
        importRecord.setImportFileId(importFile.getId());
        importRecord.setFilename(filename);
        String inferredFormat = m_rowSourceSourceFactory.inferFormat(filename, format, content);
        importRecord.setFormat(inferredFormat);
        importRecord.setUploadDate(m_clockService.now());
        InboundImportPrevalidation prevalidation = prevalidate(inferredFormat, content);
        if (prevalidation.getFileError() == null)
        {
            chooseInitialSettings(prevalidation, appContext);
            validateSegmentSettings(prevalidation);
        }
        importRecord.setPrevalidation(prevalidation);
        importRecord.setValidation(null);
        importRecord.setValidatedDate(null);
        importRecord.setStatus(prevalidation.getFileError() == null ? ImportStatus.UPLOADED : ImportStatus.UPLOAD_FAILED);
        m_gbDao.updateImportRecord(importRecord);
        
        
        return importRecord;
    }

    private InboundImportPrevalidation prevalidate(String format, byte[] content)
    {
        InboundImportPrevalidation prevalidation = new InboundImportPrevalidation();
        try
        {
            RecordSourceSource<String, String> recordSourceSource = recordSourceSourceForContent(format, content);
            prevalidateSegments(prevalidation, recordSourceSource);
        }
        catch (ImportException e)
        {
            prevalidation.setFileError("Unable to read file");
        }
        return prevalidation;
        
    }

    private RecordSourceSource<String, String> recordSourceSourceForContent(String format, byte[] content) throws ImportException
    {
        RowSourceSource<String> rowSourceSource = m_rowSourceSourceFactory.contentRowSourceSource(format, content);
        RecordSourceSource<String,String> recordSourceSource = new HeadingRowRecordSourceSource<String>(rowSourceSource);
        return recordSourceSource;
    }

    private void prevalidateSegments(InboundImportPrevalidation prevalidation, RecordSourceSource<String, String> recordSourceSource)
            throws ImportException
    {
        List<InboundImportPrevalidationSegment> segments = new ArrayList<>();
        
        while (recordSourceSource.next())
        {
            segments.add(prevalidateSegment(recordSourceSource.getRecordSource(), recordSourceSource.getRecordSourceName()));
        }
        
        prevalidation.setSegments(segments);
    }

    private InboundImportPrevalidationSegment prevalidateSegment(RecordSource<String,String> recordSource, String recordSourceName) throws ImportException
    {
        InboundImportPrevalidationSegment segment = new InboundImportPrevalidationSegment();
        segment.setName(recordSourceName);
        
        RecordContentSummarizer<String, String> summarizer = new RecordContentSummarizer<>(recordSource);
        summarizer.truncateRightmostEmptyColumns();
        
        if (summarizer.getFieldContentSummaries().size() == 0)
        {
            segment.addSegmentError("No content");
            return segment;
        }
        
        List<RecordContentSummarizer.FieldContentSummary<String>> fieldContentSummaries = summarizer.getFieldContentSummaries();
        
        List<InboundImportPrevalidationColumn> columns = new ArrayList<>(fieldContentSummaries.size());
        
        for (RecordContentSummarizer.FieldContentSummary<String> fieldContentSummary : fieldContentSummaries)
        {
            InboundImportPrevalidationColumn column = new InboundImportPrevalidationColumn();
            column.setName(fieldContentSummary.getFieldName());
            
            // Note that both empty and nonempty could be zero, if there are no rows. EMPTY is the right choice in that case.
            if (fieldContentSummary.getNonEmptyCount() == 0)
                column.setStatus(ImportPrevalidationColumnStatus.EMPTY);
            else if (fieldContentSummary.getEmptyCount() == 0)
                column.setStatus(ImportPrevalidationColumnStatus.FULL);
            else
                column.setStatus(ImportPrevalidationColumnStatus.PARTIAL);
            columns.add(column);
        }
        
        segment.setRowCount(summarizer.getRecordCount());
        segment.setNonblankRowCount(summarizer.getRecordCount() - summarizer.getBlankRecordCount());
        segment.setColumns(columns);
        return segment;
    }

    private void chooseInitialSettings(InboundImportPrevalidation prevalidation, GBAppContext appContext)
    {
        Map<String, AttributeDesc> attributeDescMap = createInitialImportAttributeMap(appContext);
        for (InboundImportPrevalidationSegment segment : prevalidation.getSegments())
        {
            InboundImportPrevalidationSegmentSettings settings = chooseInitialSettingsSegment(segment, attributeDescMap);
            segment.setSettings(settings);
        }
    }

    private InboundImportPrevalidationSegmentSettings chooseInitialSettingsSegment(InboundImportPrevalidationSegment segment,
                                                                                   Map<String, AttributeDesc> attributeDescMap)
    {
        InboundImportPrevalidationSegmentSettings settings = new InboundImportPrevalidationSegmentSettings();
        if (segment.getSegmentErrors() != null && !segment.getSegmentErrors().isEmpty())
            settings.setEnabled(false);
        else
        {
            List<String> columnMapping = new ArrayList<>();
            for (InboundImportPrevalidationColumn column : segment.getColumns())
            {
                String columnName = column.getName();
                if (columnName != null && columnName.toLowerCase().equals(PRIMARY_KEY_ATTR_NAME))
                    columnMapping.add(PRIMARY_KEY_ATTR_NAME);
                else
                {
                    AttributeDesc desc = columnName == null ? null : attributeDescMap.get(columnName.toLowerCase());  // columnName could be null, null result in that case
                    if (desc == null)
                        columnMapping.add(null);
                    else
                        columnMapping.add(desc.getName());
                }
            }
            settings.setColumnMappings(columnMapping);
            settings.setEnabled(true);  // TODO: pending other validations
        }
        return settings;
    }
    
    private void validateSegmentSettings(InboundImportPrevalidation prevalidation)
    {
        for (InboundImportPrevalidationSegment segment : prevalidation.getSegments())
        {
            validateSegmentSettings(segment);
        }
    }
    
    private void validateSegmentSettings(InboundImportPrevalidationSegment segment)
    {
        InboundImportPrevalidationSegmentSettings settings = segment.getSettings();
        if (settings.isEnabled() && settings.getColumnMappings() != null)
        {
            segment.setSegmentErrors(null);
            Set<String> attrNames = new HashSet<>();
            for (String mapping : settings.getColumnMappings())
            {
                if (mapping != null)
                {
                    if (attrNames.contains(mapping))
                        segment.addSegmentError("Duplicate mapping for " + mapping);
                    else
                        attrNames.add(mapping);
                }
            }
            if (!attrNames.contains(PRIMARY_KEY_ATTR_NAME))
                segment.addSegmentError("Missing mapping for " + PRIMARY_KEY_ATTR_NAME);
        }
    }

    public Import importChangeSettings(GBAppContext appContext,
            String importId, List<? extends ImportPrevalidationSegmentSettings> newSettings) throws IOException, GlobalBrokerException, ImportException
    {
        ImportRecord importRecord = m_gbDao.getImportRecordByGlnAndId(appContext.getGbAccount().getGln(), importId);
        if (importRecord == null)
            throw new NoSuchImportException();
        
        if (!(importRecord.getStatus() == ImportStatus.UPLOADED || importRecord.getStatus() == ImportStatus.VALIDATED))
            throw new GBIllegalStateException();
        
        InboundImportPrevalidation prevalidation = (InboundImportPrevalidation)importRecord.getPrevalidation();
        if (newSettings != null)
            processNewSettingsList(prevalidation, appContext, newSettings);
        validateSegmentSettings(prevalidation);
        if (prevalidationHasErrors(prevalidation))
            importRecord.setStatus(ImportStatus.UPLOADED);
        else
        {
            ImportFile importFile = m_gbDao.getImportFile(importRecord.getImportFileId());
            // TODO: what if importFile is null?
            
            importRecord.setValidation(doImport(true, appContext, importRecord.getFormat(), importFile.getContent(), importRecord.getPrevalidation()));
            importRecord.setStatus(ImportStatus.VALIDATED);
        }
        importRecord.setValidatedDate(m_clockService.now()); // Need to change something to get a database update
        m_gbDao.updateImportRecord(importRecord);
        
        return importRecord;
  
    }


    private void processNewSettingsList(InboundImportPrevalidation prevalidation,
            GBAppContext appContext,
            List<? extends ImportPrevalidationSegmentSettings> newSettingsList) throws GBIllegalArgumentException
    {
        if (newSettingsList.size() != prevalidation.getSegments().size())
            throw new GBIllegalArgumentException("Settings has wrong number of segments");
        
        Map<String, AttributeDesc> attributeDescMap = createAttributeDescMap(appContext);
        
        int i = 0;
        for (InboundImportPrevalidationSegment segment : prevalidation.getSegments())
        {
            ImportPrevalidationSegmentSettings newSettings = newSettingsList.get(i);
            processNewSettings(segment, attributeDescMap, newSettings);
            i++;
        }
        
        // If we got here, new settings has no errors
        i = 0;
        for (InboundImportPrevalidationSegment segment : prevalidation.getSegments())
        {
            ImportPrevalidationSegmentSettings newSettings = newSettingsList.get(i);
            segment.setSettings((InboundImportPrevalidationSegmentSettings)newSettings);
            i++;
        }
    }

    private void processNewSettings(InboundImportPrevalidationSegment segment,
            Map<String, AttributeDesc> attributeDescMap,
            ImportPrevalidationSegmentSettings newSettings) throws GBIllegalArgumentException
    {
        if (segment.getColumns() == null || segment.getColumns().size() == 0)
        {
            if (newSettings.isEnabled())
                throw new GBIllegalArgumentException("Cannot enable a segment that has no columns");
        }
        else if (newSettings.isEnabled())
        {
            if (segment.getColumns().size() != newSettings.getColumnMappings().size())
                throw new GBIllegalArgumentException("Wrong number of mappings for segment");
            
            for (String mapping : newSettings.getColumnMappings())
            {
                if (mapping != null)
                {
                    if (!isValidAttrName(mapping, attributeDescMap))
                        throw new GBIllegalArgumentException("Invalid mapping");
                }
            }
        }
        
    }

    private boolean isValidAttrName(String mapping, Map<String, AttributeDesc> attributeDescMap)
    {
        return mapping.equals(PRIMARY_KEY_ATTR_NAME) || attributeDescMap.containsKey(mapping);
        
    }

    private boolean prevalidationHasErrors(
            InboundImportPrevalidation prevalidation)
    {
        if (prevalidation.getFileError() != null)
            return true;
        for (ImportPrevalidationSegment segment : prevalidation.getSegments())
        {
            if (segment.getSettings().isEnabled() && segment.getSegmentErrors() != null && !segment.getSegmentErrors().isEmpty())
                return true;
        }
        return false;
    }

    public Import importConfirm(GBAppContext appContext,
            String importId) throws IOException, GlobalBrokerException, ImportException
    {
        ImportRecord importRecord = m_gbDao.getImportRecordByGlnAndId(appContext.getGbAccount().getGln(), importId);
        if (importRecord == null)
            throw new NoSuchImportException();
        
        if (!(importRecord.getStatus() == ImportStatus.VALIDATED))
            throw new GBIllegalStateException();
        
        ImportFile importFile = m_gbDao.getImportFile(importRecord.getImportFileId());
        // TODO: what if importFile is null?
        
        byte[] content = importFile.getContent();
        importRecord.setValidation(doImport(false, appContext, importRecord.getFormat(), content, importRecord.getPrevalidation()));
        importRecord.setStatus(ImportStatus.PROCESSED);
        importRecord.setConfirmedDate(m_clockService.now());
        m_gbDao.updateImportRecord(importRecord);
        
        return importRecord;
  
    }

    private ImportValidation doImport(boolean validateOnly, GBAppContext appContext, String format, byte[] content, ImportPrevalidation prevalidation) throws IOException, GlobalBrokerException, ImportException
    {
        RecordSourceSource<String,String> recordSourceSource = recordSourceSourceForContent(format, content);

        return doImport(validateOnly, appContext, recordSourceSource, prevalidation);
    }

    private ImportValidation doImport(boolean validateOnly, GBAppContext appContext,
                                      RecordSourceSource<String, String> recordSourceSource, ImportPrevalidation prevalidation)
                                              throws ImportException, GlobalBrokerException
    {
        Map<String,AttributeDesc> attributeDescMap = createAttributeDescMap(appContext);
        Iterator<? extends ImportPrevalidationSegment> prevalidationSegmentIterator = prevalidation.getSegments().iterator();
        List<ImportValidationProductImpl> validationProducts = new ArrayList<>();

        while (recordSourceSource.next())
        {
            RecordSource<String,String> recordSource = recordSourceSource.getRecordSource();
            ImportPrevalidationSegment prevalidationSegment = prevalidationSegmentIterator.next();
            ImportPrevalidationSegmentSettings segmentSettings = prevalidationSegment.getSettings();
            if (segmentSettings.isEnabled())
            {
                doImportSegment(validateOnly, appContext, attributeDescMap, recordSource, segmentSettings, validationProducts);
            }
        }
        
        if (validationProducts.size() > 0)
        {
            ImportValidationImpl importValidation = new ImportValidationImpl();
            importValidation.setValidationProducts(validationProducts);
            return importValidation;
        }
        else
            return null;
    }

    private void doImportSegment(boolean validateOnly, GBAppContext appContext,
            Map<String, AttributeDesc> attributeDescMap,
            RecordSource<String,String> recordSource, 
            ImportPrevalidationSegmentSettings segmentSettings,
            List<ImportValidationProductImpl> validationProducts)
        throws ImportException, GlobalBrokerException
    {
        List<String> attributeDescs = mapColumns(attributeDescMap, segmentSettings);
        RowKeyFactory<String,String> keyFactory = keyFactoryForHeadings(segmentSettings);
        Map<String,String> constantAttributeValueMap = mapConstantAttributes(attributeDescMap, segmentSettings);
        if (attributeDescs != null && keyFactory != null)
        {
            PropertySetSource<String, String, String> propertySetSource = new RecordSourcePropertySetSource<String,String,String>(recordSource, keyFactory, attributeDescs, constantAttributeValueMap);
            int i = 0;
            while (propertySetSource.next())
            {
                long start = System.currentTimeMillis();
                String gtin = propertySetSource.getKey();
                if (gtin == null)
                    validationProducts.add(null);
                else
                {
                    InboundProduct product = new InboundProduct();
                    product.setAttributes(new InboundAttributeSet());
                    product.getAttributes().setAttributes(new HashMap<String,String>());
                    product.setGtin(gtin);
                    product.setDataAccuracyAckUser(appContext.getUsername());
                    
                    for (Iterator<AttributeDesc> attrDescIterator = 
                            appContext.getProductAttributeSchema().selectedAttributesIterator(new PropertyMapAdapter(propertySetSource.getPropertyMap()));
                            attrDescIterator.hasNext();)
                    {
                        AttributeDesc attrDesc = attrDescIterator.next();
                        doImportAttribute(product, propertySetSource, attrDesc);
                        if (attrDesc.getType() == AttributeType.MEASUREMENT)
                            doImportAttribute(product, propertySetSource, attributeDescMap.get(attrDesc.getName() + "_uom"));
                    }
                    ProductStatus productStatus =
                            (validateOnly ? 
                                           m_productOpsManager.validateProductInTransaction(appContext, product, false) :
                                               m_productOpsManager.putProductInTransaction(appContext, product, false));
                    ImportValidationProductImpl validationProduct = new ImportValidationProductImpl();
                    validationProduct.setGtin(gtin);
                    validationProduct.setStatus(productStatus);
                    validationProducts.add(validationProduct);
                }
                long end = System.currentTimeMillis();
                System.out.format("%d: %d ms\n", i, end - start);
                i++;
            }
        }
    }
    
    private static class PropertyMapAdapter implements HasAttributes
    {
        private Map<String, String> m_propertyMap;
        private AttributeSet m_attributeSet;
        public PropertyMapAdapter(Map<String, String> propertyMap)
        {
            m_propertyMap = propertyMap;
            m_attributeSet = new AttributeSet(){

                @Override
                public Map<String, String> getAttributes()
                {
                    return m_propertyMap;
                }

                @Override
                public void setAttributes(Map<String, String> attributes)
                {
                    throw new UnsupportedOperationException();
                }
                
            };
        }
        @Override
        public AttributeSet getAttributes()
        {
            return m_attributeSet;
        }
        
    }

    private void doImportAttribute(
            InboundProduct product,
            PropertySetSource<String, String, String> propertySetSource,
            AttributeDesc attrDesc)
    {
        String value = propertySetSource.getPropertyMap().get(attrDesc.getName());
        if (value != null)
        {
            product.getAttributes().setAttribute(attrDesc, value);
        }
    }

    private Map<String, String> mapConstantAttributes(Map<String, AttributeDesc> attributeDescMap, ImportPrevalidationSegmentSettings segmentSettings)
    {
        Map<String,String> constantAttributeValueMap = segmentSettings.getConstantAttributeValueMap();
        if (constantAttributeValueMap == null || constantAttributeValueMap.isEmpty())
            return null;
        else
        {
            Map<String,String> result = new HashMap<>();
            for (Map.Entry<String,String> entry : constantAttributeValueMap.entrySet())
            {
                AttributeDesc desc = attributeDescMap.get(entry.getKey());
                if (desc != null)
                    result.put(desc.getName(), entry.getValue());
            }
            return result;
        }
    }

    private List<String> mapColumns(Map<String,AttributeDesc> attributeDescMap, ImportPrevalidationSegmentSettings segmentSettings)
    {
        List<String> columnMappings = segmentSettings.getColumnMappings();
        List<String> result = new ArrayList<>(columnMappings.size());
        for (String mapping : columnMappings)
        {
            AttributeDesc desc = mapping == null ? null : attributeDescMap.get(mapping);
            result.add(desc == null ? null : desc.getName());  
        }
        return result;
    }

    private Map<String, AttributeDesc> createInitialImportAttributeMap(GBAppContext appContext)
    {
        Map<String, AttributeDesc> attributeDescMap = createAttributeDescMap(appContext);
        Map<String,AttributeDesc> result = new LinkedHashMap<String, AttributeDesc>();
        for (AttributeDesc attrDesc : attributeDescMap.values())
        {
            createInitialImportAttributeMapAttribute(result, attrDesc);
        }
        return result;
    }

    private void createInitialImportAttributeMapAttribute(
            Map<String, AttributeDesc> result, AttributeDesc attrDesc)
    {
        String[] importHeadings = attrDesc.getImportHeadings();
        if (importHeadings == null)
            result.put(attrDesc.getName().toLowerCase(), attrDesc); 
        else
        {
            for (String importHeading : importHeadings)
                result.put(importHeading, attrDesc);
        }
    }
    /*
    private Map<String, AttributeDesc> createAttributeDescMap(GBAppContext appContext)
    {
        Map<String,AttributeDesc> result = new LinkedHashMap<String, AttributeDesc>();
        for (AppSubscription sub : appContext.getSubs())
        {
            ModuleDesc productModuleDesc = sub.getAppDesc().getProductModuleDesc();
            if (productModuleDesc.getSelectionAttribute() != null)
                result.put(productModuleDesc.getSelectionAttribute().getName(), productModuleDesc.getSelectionAttribute());
            for (AttributeDesc attrDesc : productModuleDesc.getUserAttributeDescs())
            {
                result.put(attrDesc.getName(), attrDesc);
                if (attrDesc.getType() == AttributeType.MEASUREMENT)
                {
                    AttributeDesc uomDesc = new UomAttributeDescHack(attrDesc);
                    result.put(uomDesc.getName(), uomDesc);
                }

            }
        }
        return result;
    }
    */
    private Map<String, AttributeDesc> createAttributeDescMap(GBAppContext appContext)
    {
        Map<String,AttributeDesc> result = new LinkedHashMap<String, AttributeDesc>();
        for (AttributeDesc attrDesc : appContext.getProductAttributeSchema())
        {
            result.put(attrDesc.getName(), attrDesc);
            if (attrDesc.getType() == AttributeType.MEASUREMENT)
            {
                AttributeDesc uomDesc = new UomAttributeDescHack(attrDesc);
                result.put(uomDesc.getName(), uomDesc);
            }
        }
        return result;
    }

    private RowKeyFactory<String, String> keyFactoryForHeadings(ImportPrevalidationSegmentSettings importPrevalidationSegmentSettings)
    {
        // TODO: more general?
        final int gtinIndex = findColumnNamed(importPrevalidationSegmentSettings.getColumnMappings(), PRIMARY_KEY_ATTR_NAME);
        if (gtinIndex < 0)
            return null;
        else
            return new RowKeyFactory<String, String>()
            {

                @Override
                public String createKey(List<String> values)
                {
                    if (values != null && gtinIndex < values.size())
                        return values.get(gtinIndex);
                    else
                        return null;
                }
            }; 
    }

    private int findColumnNamed(Collection<String> columnNames, String columnName)
    {
        int i = 0;
        for (String candidate : columnNames)
        {
            if (columnName.equals(candidate))
                return i;
            i++;
        }
        return -1;
    }

    public Import getImport(AgentUser principal, GBAccount gbAccount, String importId)
    {
        return m_gbDao.getImportRecordByGlnAndId(gbAccount.getGln(), importId);
    }

    public Collection<? extends Import> getImports(AgentUser principal, GBAccount gbAccount)
    {
        return m_gbDao.getImportRecordsByGln(gbAccount.getGln());
    }

    public void deleteImport(AgentUser principal, GBAccount gbAccount, String importId) throws NoSuchImportException
    {
        ImportRecord importRecord = m_gbDao.getImportRecordByGlnAndId(gbAccount.getGln(), importId);
        if (importRecord == null)
            throw new NoSuchImportException();
        if (importRecord.getImportFileId() != null)
        {
            ImportFile importFile = m_gbDao.getImportFile(importRecord.getImportFileId());
            m_gbDao.deleteImportFile(importFile);
        }
        m_gbDao.deleteImportRecord(importRecord);
    }
    
    private static class UomAttributeDescHack implements AttributeDesc
    {
        private String m_name;
        private String[] m_importHeadings;
        
        

        public UomAttributeDescHack(AttributeDesc underlying)
        {
            super();
            m_name = underlying.getName() + "_uom";
            if (underlying.getImportHeadings() != null)
            {
                m_importHeadings = new String[underlying.getImportHeadings().length * 3];
                for (int i = 0; i < underlying.getImportHeadings().length; i++)
                {
                    String heading = underlying.getImportHeadings()[i];
                    m_importHeadings[3*i]   = heading + "uom";
                    m_importHeadings[3*i+1] = heading + " uom";
                    m_importHeadings[3*i+2] = heading + "_uom";
                }
            }
        }

        @Override
        public String getName()
        {
            return m_name;
        }

        @Override
        public String getTitle()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String[] getImportHeadings()
        {
            return m_importHeadings;
        }

        @Override
        public String getGroupHeading()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public AttributeType getType()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends AttributeEnumValue> getEnumValues()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isRequired()
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getActions()
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public String getEntryInstructions()
        {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
