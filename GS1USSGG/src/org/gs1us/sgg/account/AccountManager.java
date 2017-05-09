package org.gs1us.sgg.account;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.AuditEventRecord;
import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.GcpRecord;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AuditEventType;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.NoSuchAccountException;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.json.InboundAttributeSet;
import org.gs1us.sgg.gbservice.util.PutHandler;
import org.gs1us.sgg.validation.ProductValidationErrorImpl;

public class AccountManager
{
    @Resource
    private ClockService m_clockService;
    
    @Resource
    private GBDao m_gbDao;

    public GBAccount getAccount(String gln)
    {
        return m_gbDao.getGBAccountByGln(gln);
    }
    
    public GBAccountRecord newGBAccountRecord()
    {
        return m_gbDao.newGBAccountRecord();
    }
    
    public void validateGcps(GBAccount gbAccount, List<ProductValidationError> validationErrors)
    {
        String gln = gbAccount.getGln();
        for (String gcp : gbAccount.getGcps())
        {
            Collection<? extends GcpRecord> matches = m_gbDao.getMatchingGcpRecords(gcp);
            for (GcpRecord match : matches)
            {
                if (!gln.equals(match.getGln()))
                    validationErrors.add(new ProductValidationErrorImpl("gcps", 
                                                                        String.format("GCP %s conflicts with GCP %s held by GLN %s",
                                                                                      gcp,
                                                                                      match.getGcp(),
                                                                                      match.getGln())));
            }
        }
    }

    // TODO: probably different flavors: MO account, member account, partner account, etc?
    public void createAccount(AgentUser agentUser, String username, GBAccount gbAccount)
    {
        GBAccountRecord record = m_gbDao.newGBAccountRecord();
        record.updateFrom(gbAccount);
        if (record.getAttributes() == null)
            record.setAttributes(new InboundAttributeSet());
        m_gbDao.updateGBAccount(record);
        
        createGcpRecords(gbAccount);
        
        AuditEventRecord event = m_gbDao.newAuditEvent();
        event.setType(AuditEventType.CREATE_ACCOUNT);
        event.setDate(m_clockService.now());
        event.setAgentUsername(agentUser.getUsername());
        event.setUsername(username);
        event.setGBAccountGln(gbAccount.getGln());
        m_gbDao.updateAuditEvent(event);
        
        return;
    }

    private void createGcpRecords(GBAccount gbAccount)
    {
        String gln = gbAccount.getGln();
        for (String gcp : gbAccount.getGcps())
        {
            GcpRecord gcpRecord = m_gbDao.newGcpRecord(gcp, gln);
            m_gbDao.updateGcpRecord(gcpRecord);
        }
    }
    
    public void updateAccount(AgentUser agentUser, String username, GBAccount gbAccount) throws NoSuchAccountException
    {
        String gln = gbAccount.getGln();
        GBAccountRecord record = m_gbDao.getGBAccountByGln(gln);
        
        if (record == null)
            throw new NoSuchAccountException();
        
        record.updateFrom(gbAccount);
        m_gbDao.updateGBAccount(record);
        
        Set<String> oldGcps = new HashSet<>();
        Collection<? extends GcpRecord> oldGcpRecords = m_gbDao.getGcpRecordsByGln(gln);
        for (GcpRecord oldGcpRecord : oldGcpRecords)
            oldGcps.add(oldGcpRecord.getGcp());
        Set<String> newGcps = new HashSet<>();
        
        for (String gcp : gbAccount.getGcps())
        {
            newGcps.add(gcp);
            if (!oldGcps.contains(gcp))
            {
                GcpRecord gcpRecord = m_gbDao.newGcpRecord(gcp, gln);
                m_gbDao.updateGcpRecord(gcpRecord);
            }
        }

        for (GcpRecord oldGcpRecord : oldGcpRecords)
        {
            if (!newGcps.contains(oldGcpRecord.getGcp()))
                m_gbDao.deleteGcpRecord(oldGcpRecord);
        }
        
        AuditEventRecord event = m_gbDao.newAuditEvent();
        event.setType(AuditEventType.UPDATE_ACCOUNT);
        event.setDate(m_clockService.now());
        event.setAgentUsername(agentUser.getUsername());
        event.setUsername(username);
        event.setGBAccountGln(gbAccount.getGln());
        m_gbDao.updateAuditEvent(event);
        
        return;
    }
}
