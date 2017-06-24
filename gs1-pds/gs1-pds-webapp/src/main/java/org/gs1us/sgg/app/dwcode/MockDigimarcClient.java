package org.gs1us.sgg.app.dwcode;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gs1us.sgg.clockservice.ClockService;

public class MockDigimarcClient implements DigimarcService
{
    @Resource
    private ClockService m_clockService;
    
    private static class SequencedTable<T> extends HashMap<Integer,T>
    {
        private int m_nextId = 1;
        
        public synchronized int add(T elt)
        {
            int id = m_nextId;
            m_nextId++;
            put(id, elt);
            return id;
        }
    }
    
    private static class AccountRecord
    {
        private AccountReadResponse m_accountReadResponse;
        private List<Integer> m_serviceIds = new ArrayList<>();
        
        
        public AccountRecord(AccountReadResponse accountReadResponse)
        {
            super();
            m_accountReadResponse = accountReadResponse;
        }
        public AccountReadResponse getAccountReadResponse()
        {
            return m_accountReadResponse;
        }
        public List<Integer> getServiceIds()
        {
            return m_serviceIds;
        }
        
        
    }
    
    private SequencedTable<AccountRecord> m_accountTable = new SequencedTable<>();
    private SequencedTable<ProjectCreateRequest> m_projectTable = new SequencedTable<>();
    private SequencedTable<ServiceReadResponse> m_serviceTable = new SequencedTable<>();
    
    
    @Override
    public boolean isEnabled()
    {
        return true;
    }
    
    

    @Override
    public UserAccountsReadResponse getAccounts() throws DigimarcException
    {
        List<UserAccountInfo> accountInfos = new ArrayList<>();
        for (Map.Entry<Integer, AccountRecord> entry : m_accountTable.entrySet())
        {
            UserAccountInfo info = new UserAccountInfo();
            info.setAccountId(entry.getKey());
            AccountReadResponse arr = entry.getValue().getAccountReadResponse();
            info.setCompanyName(arr.getCompanyName());
            info.setCreateDate(arr.getCreateDate());
            accountInfos.add(info);
        }
        
        UserAccountsReadResponse response = new UserAccountsReadResponse();
        response.setAccounts(accountInfos);
        
        return response;
    }



    @Override
    public AccountReadResponse getAccount(int accountId) throws DigimarcException
    {
        AccountRecord record = m_accountTable.get(accountId);
        if (record == null)
            throw new DigimarcException("No such account");
        return record.getAccountReadResponse();
    }

    @Override
    public synchronized AccountCreateResponse createAccount(
            AccountCreateRequest accountCreate) throws DigimarcException
    {
        ContactReadResponse crr = new ContactReadResponse();
        ContactCreateRequest ci = accountCreate.getContactInfo();
        crr.setCity(ci.getCity());
        crr.setEmail(ci.getEmail());
        crr.setFax(ci.getFax());
        crr.setFirstName(ci.getFirstName());
        crr.setLastName(ci.getLastName());
        crr.setPhone1(ci.getPhone1());
        crr.setPhone2(ci.getPhone2());
        crr.setPostalCode(ci.getPostalCode());
        crr.setStreet1(ci.getStreet1());
        crr.setStreet2(ci.getStreet2());

        AccountReadResponse arr = new AccountReadResponse();
        arr.setAccountNumber(accountCreate.getAccountNumber());
        arr.setCompanyName(accountCreate.getCompanyName());
        arr.setContactInfo(crr);
        arr.setEmailOptOut(accountCreate.isEmailOptOut());
        arr.setCreateDate(m_clockService.now());

        AccountRecord record = new AccountRecord(arr);
        int accountId = m_accountTable.add(record);
        arr.setId(accountId);
        AccountCreateResponse acr = new AccountCreateResponse();
        acr.setId(accountId);
        return acr;
    }

    @Override
    public ProjectCreateResponse createProject(int accountId,
            ProjectCreateRequest projectCreate) throws DigimarcException
    {
        int projectId = m_projectTable.add(projectCreate);
        ProjectCreateResponse pcr = new ProjectCreateResponse();
        pcr.setId(projectId);
        return pcr;

    }
    
    @Override
    public ServiceReadResponse getService(int serviceId, int accountId) throws DigimarcException
    {
        ServiceReadResponse result = m_serviceTable.get(serviceId);
        if (result == null)
            throw new DigimarcException("No such service");
        return result;
    }


    @Override
    public ServiceCreateResponse createService(int accountId,
            ServiceCreateRequest serviceCreate) throws DigimarcException
    {
        ServiceReadResponse srr = new ServiceReadResponse();
        updateServiceCreate(srr, serviceCreate);
        
        int serviceId = m_serviceTable.add(srr);
        srr.setId(serviceId);
       ServiceCreateResponse scr = new ServiceCreateResponse();
       scr.setId(serviceId);
       
       AccountRecord accountRecord = m_accountTable.get(accountId);
       if (accountRecord != null)
           accountRecord.getServiceIds().add(serviceId);
       
       return scr;


    }

    @Override
    public void updateService(int serviceId, int accountId,
            ServiceUpdateRequest serviceUpdate) throws DigimarcException
    {
        ServiceReadResponse serviceReadResponse = m_serviceTable.get(serviceId);
        if (serviceReadResponse == null)
            throw new DigimarcException("no such service");
        
        updateServiceCreateUpdate(serviceReadResponse, serviceUpdate);
    }
    
    



    @Override
    public void accountInternal(AccountInternalRequest accountInternalRequest)
        throws DigimarcException
    {
        Integer accountId = accountInternalRequest.getId();
        AccountRecord record = m_accountTable.get(accountId);
        if (record == null)
            throw new DigimarcException("Not found");
        
        for (Integer serviceId : record.getServiceIds())
        {
            m_serviceTable.remove(serviceId);
        }
        if ("delete".equals(accountInternalRequest.getOperation()))
            m_accountTable.remove(accountId);
    }



    private void updateServiceCreateUpdate(ServiceReadResponse srr, ServiceCreateUpdateRequest sur)
    {
        srr.setAutoExtend(sur.isAutoExtend());
        srr.setDescription(sur.getDescription());
        srr.setEndDate(sur.getEndDate());
        srr.setHumanReadableId(sur.getHumanReadableId());
        srr.setName(sur.getName());
        srr.setPayoffTitle(sur.getPayoffTitle());
        srr.setPayoffUrl(sur.getPayoffUrl());
        srr.setStartDate(sur.getStartDate());
        
        ProductDetailsRequest d = sur.getProductDetails();
        ProductDetailsReadResponse pd = new ProductDetailsReadResponse();
        
        pd.setBrand(d.getBrand());
        pd.setColor(d.getColor());
        pd.setDescription(d.getDescription());
        pd.setFlavor(d.getFlavor());
        pd.setImageUrl(d.getImageUrl());
        pd.setProductName(d.getProductName());
        pd.setQuantities(d.getQuantities());
        pd.setScent(d.getScent());
        pd.setSize(d.getSize());
        pd.setSubBrand(d.getSubBrand());
        
        srr.setProductDetails(pd);
        
    }
    
    private void updateServiceCreate(ServiceReadResponse srr, ServiceCreateRequest scr)
    {
        updateServiceCreateUpdate(srr, scr);
        srr.setProjectId(scr.getProjectId());
    }

}
