package org.gs1us.sgl.webapp;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegmentSettings;
import org.gs1us.sgg.gbservice.api.ImportStatus;
import org.gs1us.sgg.gbservice.api.ModuleDesc;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.json.InboundImportPrevalidationSegmentSettings;
import org.gs1us.sgg.util.UserInputUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@Transactional
@RequestMapping("/ui")
public class ImportController extends GBAwareController
{
    
    @RequestMapping(value = "/import/upload", method = RequestMethod.GET)
    public String importUploadGet(Model model, Principal principal) throws GlobalBrokerException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        
        return "/jsp/import/importUpload.jsp";
    }
    
    @RequestMapping(value = "/import/upload", method = RequestMethod.POST)
    public String importUploadPost(Model model, Principal principal, @RequestParam("file") MultipartFile file) throws GlobalBrokerException, IOException 
    {
        String gbAccountGln = getGBAccountGln(principal);

        if (file != null)
        {
            byte[] content = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String creator = principal.getName();
            
            if (content == null || content.length == 0)
            {
                model.addAttribute("reason", "Either no file was selected or the file you selected is empty.");
                return "/jsp/import/importUploadFailed.jsp";
            }

            Import importRecord = getGbService().importUpload(creator, gbAccountGln, originalFilename, file.getContentType(), content);
            //getGbService().importChangeSettings(creator, gbAccountGln, importRecord.getId(), null);
            //getGbService().importConfirm(creator, gbAccountGln, importRecord.getId());
            
            if (importRecord.getStatus() == ImportStatus.UPLOADED)
                return "redirect:/ui/import/" + importRecord.getId() + "/settings";
            else
            {
                model.addAttribute("reason", "Your file could not be uploaded.");
                return "/jsp/import/importUploadFailed.jsp";
            }
            
        }                   
        else        
            return "redirect:/ui/product";
    }

    
    @RequestMapping(value = "/import/{id}/settings", method = RequestMethod.GET)
    public String importSettingsGet(Model model, Principal principal, @PathVariable String id) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        Import importRecord = lookupImport(gbAccountGln, id);
        Map<String,AttributeDesc> attrDescMap = getAttributeDescMap(gbAccountGln);
        
        model.addAttribute("import", importRecord);
        model.addAttribute("attrDescMap", attrDescMap);
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importSettingsPost", null, null, id, null).toUriString());
        model.addAttribute("deleteUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importDeleteGet", null, null, id).toUriString());
        model.addAttribute("laterUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importShowAllGet", null, null).toUriString());
        
        
        return "/jsp/import/importSettings.jsp";
        
    }
    
    private Map<String,AttributeDesc> getAttributeDescMap(String gbAccountGln) throws GlobalBrokerException
    {
        Map<String,AttributeDesc> result = new LinkedHashMap<>();
        Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gbAccountGln, true);
        for (AppSubscription sub : subs)
        {
            ModuleDesc productModuleDesc = sub.getAppDesc().getProductModuleDesc();
            if (productModuleDesc.getSelectionAttribute() != null)
                result.put(productModuleDesc.getSelectionAttribute().getName(), productModuleDesc.getSelectionAttribute());

            for (AttributeDesc attrDesc : productModuleDesc.getUserAttributeDescs())
                result.put(attrDesc.getName(), attrDesc);
        }
        return result;
    }

    @RequestMapping(value = "/import/{id}/settings", method = RequestMethod.POST)
    public String importSettingsPost(Model model, Principal principal, @PathVariable String id, HttpServletRequest request) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        Import importRecord = lookupImport(gbAccountGln, id);
        
        List<? extends ImportPrevalidationSegmentSettings> settings = extractSettings(request);
        
        Import changedImportRecord = getGbService().importChangeSettings(principal.getName(), gbAccountGln, importRecord.getId(), settings);

        if (changedImportRecord.getStatus() == ImportStatus.UPLOADED)
            return "redirect:/ui/import/" + importRecord.getId() + "/settings";
        else
            return "redirect:/ui/import/" + importRecord.getId() + "/confirm";
    }

    
    private List<? extends ImportPrevalidationSegmentSettings> extractSettings(HttpServletRequest request)
    {
        List<InboundImportPrevalidationSegmentSettings> settings = new ArrayList<>();
        Map<String,String[]> parameterMap = request.getParameterMap();
        
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet())
        {
            String parameterName = entry.getKey();
            String[] parameterValues = entry.getValue();
            MappingParameter mappingParameter = MappingParameter.parse(parameterName, parameterValues);
            
            if (mappingParameter != null)
            {
                int segmentIndex = mappingParameter.getSegmentIndex();
                while (segmentIndex >= settings.size())
                {
                    InboundImportPrevalidationSegmentSettings segmentSettings = new InboundImportPrevalidationSegmentSettings();
                    segmentSettings.setColumnMappings(new ArrayList<String>());
                    settings.add(segmentSettings);
                    // Hack to avoid having the user do a separate mapping for dmChargesAck
                    Map<String,String> constantAttributeValueMap = new HashMap<String, String>();
                    constantAttributeValueMap.put("dmChargesAck", "true");
                    segmentSettings.setConstantAttributeValueMap(constantAttributeValueMap);
                }
                
                InboundImportPrevalidationSegmentSettings segmentSettings = settings.get(segmentIndex);
                int columnIndex = mappingParameter.getColumnIndex();
                if (columnIndex < 0) // implying it is an "enable" parameter rather than a column mapping parameter
                {
                    segmentSettings.setEnabled(mappingParameter.getParameterValue() != null);
                }
                else
                {
                    List<String> columnMappings = segmentSettings.getColumnMappings();
                    while (columnIndex >= columnMappings.size())
                        columnMappings.add(null);
                    String mapping = mappingParameter.getParameterValue();
                    if (mapping == null || mapping.equals("@ignore"))
                        columnMappings.set(columnIndex, null);
                    else
                        columnMappings.set(columnIndex, mapping);
    
                }
                
            }
            
        }
        // If just one sheet, make sure it is enabled, as we don't prompt user in that case.
        if (settings.size() == 1)
            settings.get(0).setEnabled(true);
        return settings;
    }
    
    private static class MappingParameter
    {
        public static MappingParameter parse(String parameterName, String[] parameterValues)
        {
            if (parameterName == null || !parameterName.startsWith("mapping-") || parameterValues == null || parameterValues.length != 1)
                return null;
            
            String parameterValue = UserInputUtil.trimToNull(parameterValues[0]);
            
            int secondDash = parameterName.indexOf('-', 8);
            int segmentIndexEnd = secondDash >= 0 ? secondDash : parameterName.length();
            String segmentIndexString = parameterName.substring(8, segmentIndexEnd);
            try
            {
                int segmentIndex = Integer.parseInt(segmentIndexString);
                if (segmentIndex < 0)
                    return null;
                
                if (secondDash >= 0)
                {
                    String columnIndexString = parameterName.substring(secondDash+1);
                    int columnIndex = Integer.parseInt(columnIndexString);
                    if (columnIndex < 0)
                        return null;
                    return new MappingParameter(segmentIndex, columnIndex, parameterValue);
                }
                else
                    return new MappingParameter(segmentIndex, -1, parameterValue);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }
        private int m_segmentIndex;
        private int m_columnIndex;
        private String m_parameterValue;
        public MappingParameter(int segmentIndex, int columnIndex,
                String parameterValue)
        {
            super();
            m_segmentIndex = segmentIndex;
            m_columnIndex = columnIndex;
            m_parameterValue = parameterValue;
        }
        public int getSegmentIndex()
        {
            return m_segmentIndex;
        }
        public int getColumnIndex()
        {
            return m_columnIndex;
        }
        public String getParameterValue()
        {
            return m_parameterValue;
        }
 
        
        
    }

    @RequestMapping(value = "/import/{id}/confirm", method = RequestMethod.GET)
    public String importConfirmGet(Model model, Principal principal, @PathVariable String id) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        Import importRecord = lookupImport(gbAccountGln, id);
        
        importConfirmModel(model, id, gbAccountGln, importRecord);
        
        return "/jsp/import/importConfirm.jsp";
        
    }

    private void importConfirmModel(Model model, String id,
            String gbAccountGln, Import importRecord)
        throws GlobalBrokerException
    {
        Map<String,AttributeDesc> attrDescMap = getAttributeDescMap(gbAccountGln);
        
        model.addAttribute("import", importRecord);
        model.addAttribute("attrDescMap", attrDescMap);
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importConfirmPost", null, null, id, null, null).toUriString());
        model.addAttribute("settingsUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importSettingsGet", null, null, id).toUriString());
        model.addAttribute("deleteUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importDeleteGet", null, null, id).toUriString());
        model.addAttribute("laterUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importShowAllGet", null, null).toUriString());
    }
    
    @RequestMapping(value = "/import/{id}/confirm", method = RequestMethod.POST)
    public String importConfirmPost(Model model, Principal principal, @PathVariable String id, @RequestParam String purchaseAck, @RequestParam String dataAccuracyAck) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        Import importRecord = lookupImport(gbAccountGln, id);
        
        boolean dataAccuracyAckIsChecked = dataAccuracyAck != null && dataAccuracyAck.length() > 0;
        boolean purchaseAckIsChecked = purchaseAck != null && purchaseAck.length() > 0;
        
        boolean error = false;
        
        if (!purchaseAckIsChecked)
        {
            model.addAttribute("purchaseError", "You must confirm this.");
            error = true;
        }
        if (!dataAccuracyAckIsChecked)
        {
            model.addAttribute("dataAccuracyError", "You must confirm the data accuracy acknowledgement.");
            error = true;
        }
        if (error)
        {
            importConfirmModel(model, id, gbAccountGln, importRecord);
            return "/jsp/import/importConfirm.jsp";
        }

        getGbService().importConfirm(principal.getName(), gbAccountGln, importRecord.getId());

        return "redirect:/ui/product";
    }

    
    private Import lookupImport(String gbAccountGln, String importId) throws GlobalBrokerException, NoSuchResourceException
    {
        Import importRecord = getGbService().getImport(gbAccountGln, importId);
        if (importRecord == null)
            throw new NoSuchResourceException();
        return importRecord;
    }
    
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String importShowAllGet(Model model, Principal principal) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        Collection<? extends Import> importRecords = getGbService().getImports(gbAccountGln);
        
        model.addAttribute("imports", importRecords);
        
        return "/jsp/import/importShowAll.jsp";
        
    }
    
    @RequestMapping(value = "/import/{id}/delete", method = RequestMethod.GET)
    public String importDeleteGet(Model model, Principal principal, @PathVariable String id) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        Import importRecord = lookupImport(gbAccountGln, id);
        
        model.addAttribute("import", importRecord);
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importDeletePost", null, null, id).toUriString());
        model.addAttribute("cancelUrl", MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importShowAllGet", null, null).toUriString());
        
        return "/jsp/import/importDelete.jsp";
        
    }
    
    @RequestMapping(value = "/import/{id}/delete", method = RequestMethod.POST)
    public String importDeletePost(Model model, Principal principal, @PathVariable String id) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        Import importRecord = lookupImport(gbAccountGln, id);
        
        getGbService().deleteImport(gbAccountGln, id);
        
        return "redirect:/ui/import";
        
    }



}
