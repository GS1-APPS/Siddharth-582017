package org.gs1us.sgg.app.dwcode;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.gs1us.sgg.app.AppEventHandler;
import org.gs1us.sgg.attribute.AttributeDescImpl;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.AppSubscriptionRecord;
import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AttributeType;
import org.gs1us.sgg.gbservice.api.GBAccount;

public class DWCodeIntegrationAppHandler extends DWCodeIntegrationHandler implements AppEventHandler
{
    private static final Logger s_logger = Logger.getLogger("org.gs1us.sgg.app.dwcode.DWCodeIntegrationAppHandler");
    
    private GBDao m_gbDao;

    public DWCodeIntegrationAppHandler(DigimarcService digimarcService, GBDao gbDao)
    {
        super(digimarcService);
        m_gbDao = gbDao;
    }
    

    @Override
    public void handleSubscribe(AgentUser agentUser, String username, GBAccount gbAccount, AppSubscriptionRecord appSubscriptionRecord)
    {
        String licenseType = getLicenseType(gbAccount);
        if (!suppressDigimarcOperations(agentUser) && !"DMCustomer".equals(licenseType))
        {
            ContactCreateRequest contact = new ContactCreateRequest();
            contact.setEmail(username);
            
            String brandOwnerName = gbAccount.getAttributes().getAttribute("brandOwnerName");
            if (brandOwnerName == null)
                brandOwnerName = gbAccount.getName();

            AccountCreateRequest acr = new AccountCreateRequest();
            acr.setContactInfo(contact);
            acr.setAccountNumber(gbAccount.getGln());
            acr.setCompanyName(gbAccount.getName());
            acr.setBrandOwner(brandOwnerName);
            acr.setEmailOptOut(true);
            
            ProjectCreateRequest pcr = new ProjectCreateRequest();
            pcr.setName("DWCode inbound");
            pcr.setDescription("New DWCodes created via the DWCode portal will arrive in this project");

            try
            {
                AccountCreateResponse acrResponse = getDigimarcService().createAccount(acr);
                int accountId = acrResponse.getId();
                
                ProjectCreateResponse pcrResponse = getDigimarcService().createProject(accountId, pcr);
                int projectId = pcrResponse.getId();
                
                GBAccountRecord gbAccountRecord = (GBAccountRecord)gbAccount;
                gbAccountRecord.getAttributes().setIntAttribute(ACCOUNT_ID_ATTR_DESC, accountId);
                gbAccountRecord.getAttributes().setIntAttribute(PROJECT_ID_ATTR_DESC, projectId);
                m_gbDao.updateGBAccount(gbAccountRecord);
            }
            catch (DigimarcException e)
            {
                s_logger.log(Level.SEVERE, "Error during handleSubscribe", e);
            }
        }

    }


    private String getLicenseType(GBAccount gbAccount)
    {
        AttributeSet attributes = gbAccount.getAttributes();
        if (attributes == null)
            return null;
        else
            return attributes.getAttribute("dmLicenseType");
    }

}
