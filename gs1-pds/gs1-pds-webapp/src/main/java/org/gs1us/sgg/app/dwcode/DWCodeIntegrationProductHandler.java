package org.gs1us.sgg.app.dwcode;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gs1us.sgg.app.AppManager;
import org.gs1us.sgg.attribute.AttributeDescImpl;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeEnumValue;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AttributeType;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.product.ProductEventHandler;
import org.gs1us.sgg.util.UPCE;
import org.gs1us.sgg.webapp.ProductInfoController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class DWCodeIntegrationProductHandler extends DWCodeIntegrationHandler implements ProductEventHandler
{
    private DigimarcOptions m_digimarcOptions;
    private ClockService m_clockService;
    
    private static final Logger s_logger = Logger.getLogger("org.gs1us.sgg.app.dwcode.DWCodeIntegrationProductHandler");
    
    private static final AttributeDesc SERVICE_ID_ATTR_DESC = 
            new AttributeDescImpl("dmServiceId", "Digimarc Service ID", null, null, AttributeType.INTEGER, true, "n/a", null);
    
    public DWCodeIntegrationProductHandler(DigimarcService digimarcService, DigimarcOptions digimarcOptions, ClockService clockService)
    {
        super(digimarcService);
        m_digimarcOptions = digimarcOptions;
        m_clockService = clockService;
    }
    
    @Override
    public void handleCreate(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord)
    {
        if (useDigimarc(agentUser, productRecord))
        {
            Integer accountId = accountId(gbAccount);
            if (accountId == null)
            {
                // TODO: create account?
                s_logger.severe("Error during handleCreate for GTIN " + productRecord.getGtin() + ": no Digimarc account");
                return;
            }
            ServiceCreateRequest serviceCreate = serviceCreateRequest(productRecord, projectId(gbAccount));
            try
            {
                ServiceCreateResponse response = getDigimarcService().createService(accountId, serviceCreate);
                int serviceId = response.getId();
                productRecord.getAttributes().setIntAttribute(SERVICE_ID_ATTR_DESC, serviceId);
            }
            catch (DigimarcException e)
            {
                s_logger.log(Level.SEVERE, "Error during handleCreate for GTIN " + productRecord.getGtin(), e);
            }
        }
    }

    @Override
    public void handleUpdate(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord)
    {
        if (useDigimarc(agentUser, productRecord))
        {
            Integer accountId = accountId(gbAccount);
            if (accountId == null)
            {
                // TODO: create account?
                s_logger.severe("Error during handleUpdate for GTIN " + productRecord.getGtin() + ": no Digimarc account");
                return;
            }
            
            Integer serviceId = productRecord.getAttributes().getIntAttribute(SERVICE_ID_ATTR_DESC);
            if (serviceId == null)
            {
                s_logger.warning("No service ID to update GTIN " + productRecord.getGtin() + "; creating a service instead");
                handleCreate(agentUser, username, gbAccount, productRecord);
            }
            else
            {
                ServiceUpdateRequest serviceUpdate = serviceUpdateRequest(productRecord);
                try
                {
                    getDigimarcService().updateService(serviceId, accountId, serviceUpdate);
                }
                catch (DigimarcException e)
                {
                    s_logger.log(Level.SEVERE, "Error during handleUpdate for GTIN " + productRecord.getGtin(), e);
                }                
            }
        }
    }

    private boolean useDigimarc(AgentUser agentUser, ProductRecord productRecord)
    {
        return !suppressDigimarcOperations(agentUser) && digimarcEnabled(productRecord);
    }

    private boolean digimarcEnabled(ProductRecord productRecord)
    {
        // We use Digimarc if DM is enabled and we have a paid-thru date. If we don't have a paid-thru date,
        // it's because GS1 US is waiting to receive payment from customer first. It's OK if we're past the
        // paid thru date; we still want to process updates in case we're operating under a grace period.
        // TODO: what about the end date?
        AttributeSet attributes = productRecord.getAttributes();
        boolean dwcodeIsEnabled = attributes.getBooleanAttribute(new DummyAttrDesc(AppManager.DWCODE_APP_NAME));
        boolean hasPaidThru = attributes.getAttribute("dmPaidThru") != null;
        return dwcodeIsEnabled && hasPaidThru;
    }

    @Override
    public void handleDelete(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord)
    {
        // TODO Auto-generated method stub
        
    }

    private Integer accountId(GBAccount gbAccount)
    {
        // return 3245;
        return gbAccount.getAttributes().getIntAttribute(ACCOUNT_ID_ATTR_DESC);
    }

    private Integer projectId(GBAccount gbAccount)
    {
        AttributeSet attributes = gbAccount.getAttributes();
        return attributes.getIntAttribute(PROJECT_ID_ATTR_DESC);
    }

    private ServiceCreateRequest serviceCreateRequest(ProductRecord productRecord, Integer projectId)
    {
        ServiceCreateRequest createRequest = new ServiceCreateRequest();
        createRequest.setProjectId(projectId);
        createRequest.setServiceType("PackagingType7");
        serviceCreateOrUpdateRequest(productRecord, createRequest);
        // DM requires start date to be present. If it is already present, then it is equal to our dmStartDate.
        // If not, set it to "now" and update dmStartDate to match.
        if (createRequest.getStartDate() == null)
        {
            Date newStartDate = m_clockService.now();
            createRequest.setStartDate(newStartDate);
            productRecord.getAttributes().setDateAttribute(new DummyAttrDesc("dmStartDate"), newStartDate);
        }
        return createRequest;
    }

    private ServiceUpdateRequest serviceUpdateRequest(ProductRecord productRecord)
    {
        ServiceUpdateRequest updateRequest = new ServiceUpdateRequest();
        serviceCreateOrUpdateRequest(productRecord, updateRequest);
        return updateRequest;
    }

    private void serviceCreateOrUpdateRequest(ProductRecord productRecord, ServiceCreateUpdateRequest request)
    {
        AttributeSet attributes = productRecord.getAttributes();

        String gtin = productRecord.getGtin();
        String digimarcHumanReadableId = gtin;
        if (attributes.getAttribute("dmUPCE") != null)
        {
            String upce = UPCE.gtinToUPCE(gtin);
            if (upce != null) // Validation should prevent it from ever being null, but let's be safe
                digimarcHumanReadableId = upce;
        }
        
        request.setName(digimarcHumanReadableId);
        request.setDescription("DWCode for " + attributes.getAttribute("productName"));
        // We omit start/end date if in past, FOR TESTING ONLY. In a live system, these dates will not be in the past if they really need updating.
        //request.setStartDate(omitIfInPast(attributes.getDateAttribute(new DummyAttrDesc("dmStartDate"))));
        //request.setEndDate(omitIfInPast(attributes.getDateAttribute(new DummyAttrDesc("dmEndDate"))));
        request.setStartDate(attributes.getDateAttribute(new DummyAttrDesc("dmStartDate")));
        request.setEndDate(attributes.getDateAttribute(new DummyAttrDesc("dmEndDate")));
        request.setAutoExtend(false);
        request.setHumanReadableId(digimarcHumanReadableId);
        String explicitExperienceUrl = attributes.getAttribute("dmExperienceUrl");
        if (explicitExperienceUrl == null)
            request.setPayoffUrl(defaultExperienceUrl(productRecord));
        else
            request.setPayoffUrl(explicitExperienceUrl);
        
        ProductDetailsRequest details = new ProductDetailsRequest();
        details.setBrand(nullToEmpty(attributes.getAttribute("brandName")));
        details.setSubBrand(nullToEmpty(attributes.getAttribute("subBrand")));
        details.setProductName(nullToEmpty(attributes.getAttribute("productName")));
        details.setDescription(nullToEmpty(attributes.getAttribute("description")));
        details.setColor(nullToEmpty(attributes.getAttribute("color")));
        details.setFlavor(nullToEmpty(attributes.getAttribute("flavor")));
        details.setScent(nullToEmpty(attributes.getAttribute("scent")));
        details.setSize(nullToEmpty(attributes.getAttribute("size")));
        details.setImageUrl(nullToEmpty(attributes.getAttribute("mobileDeviceImage")));
        
        List<ProductQuantity> quantities = new ArrayList<>();
        for (int i = 1; i <= 4; i++)
        {
            String attributeName = "netContent" + i;
            String quantity = attributes.getAttribute(attributeName);
            if (quantity != null)
            {
                ProductQuantity q = new ProductQuantity();
                q.setValue(new BigDecimal(quantity));
                q.setUnit(attributes.getAttribute(attributeName + "_uom"));
                quantities.add(q);
            }
        }
        // Important to send an empty array if no quantities!
        ProductQuantity[] quantitiesArray = quantities.toArray(new ProductQuantity[0]);
        details.setQuantities(quantitiesArray);
        
        request.setProductDetails(details);
    }
    
    private String defaultExperienceUrl(ProductRecord productRecord)
    {
        UriComponentsBuilder builder = 
                MvcUriComponentsBuilder.fromMethodName(ProductInfoController.class, "productInfoGet", null, productRecord.getGtin());
        
        if (m_digimarcOptions.getDefaultExperienceUrlHost() != null)
            builder.host(m_digimarcOptions.getDefaultExperienceUrlHost());
        
        if (m_digimarcOptions.getDefaultExperienceUrlPort() != null)
            builder.port(m_digimarcOptions.getDefaultExperienceUrlPort());

        return builder.toUriString(); 
    }

    private String nullToEmpty(String s)
    {
        if (s == null)
            return "";
        else
            return s;
    }
    
    private Date omitIfInPast(Date date)
    {
        if (date == null)
            return null;
        
        long dateMillis = date.getTime();
        Date now = m_clockService.now();
        long nowMillis = now.getTime();
        long millisFromNow = dateMillis - nowMillis;
        if (millisFromNow < -86400000L)
            return null;  // if more than one day in past
        else
            return date;
    }

    private static class DummyAttrDesc implements AttributeDesc
    {
        private String m_name;
        
        
        public DummyAttrDesc(String name)
        {
            super();
            m_name = name;
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
            // TODO Auto-generated method stub
            return null;
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

    private static class NoCloseOutputStream extends FilterOutputStream
    {

        public NoCloseOutputStream(OutputStream out)
        {
            super(out);
        }
        
        public void close()
        {
            // Do nothing
        }
        
    }

}
