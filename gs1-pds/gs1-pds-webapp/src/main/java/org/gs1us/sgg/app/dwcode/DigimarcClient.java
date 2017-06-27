package org.gs1us.sgg.app.dwcode;

import org.gs1us.sgg.transport.HttpTransport;
import org.gs1us.sgg.transport.HttpTransport.HttpMethod;
import org.gs1us.sgg.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DigimarcClient implements DigimarcService
{
    private DigimarcJsonClient m_jsonClient;
    
    public boolean isEnabled()
    {
        return true;
    }
    
    public DigimarcClient(String urlPrefix, String username, String password, HttpTransport httpTransport)
    {
        ObjectMapper om = new ObjectMapper();
        om
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.WRITE_NULL_MAP_VALUES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        om.setDateFormat(Util.ISO8601_DATE_FORMAT_NO_MILLIS); 
        
        om.setPropertyNamingStrategy(new PropertyNamingStrategy.UpperCamelCaseStrategy());

        m_jsonClient = new DigimarcJsonClient(urlPrefix, username, password, httpTransport, om);
    }
    
    
    
    @Override
    public UserAccountsReadResponse getAccounts() throws DigimarcException
    {
        return m_jsonClient.doRequest(UserAccountsReadResponse.class, HttpMethod.GET, null, "/v2/user/accounts");
    }

    @Override
    public AccountReadResponse getAccount(int accountId) throws DigimarcException
    {
        return m_jsonClient.doRequest(AccountReadResponse.class, HttpMethod.GET, null, "/v2/account/%d", accountId);
    }

    @Override
    public AccountCreateResponse createAccount(AccountCreateRequest accountCreate) throws DigimarcException
    {
        return m_jsonClient.doRequest(AccountCreateResponse.class, HttpMethod.POST, accountCreate, "/v2/account");
    }

    @Override
    public ProjectCreateResponse createProject(int accountId, ProjectCreateRequest projectCreate) throws DigimarcException
    {
        return m_jsonClient.doRequest(ProjectCreateResponse.class, HttpMethod.POST, projectCreate, "/v2/project?account=%d", accountId);
    }
    
    

    @Override
    public ServiceReadResponse getService(int serviceId, int accountId) throws DigimarcException
    {
        return m_jsonClient.doRequest(ServiceReadResponse.class, HttpMethod.GET, null, "/v2/service/%d?account=%d", serviceId, accountId);
    }

    @Override
    public ServiceCreateResponse createService(int accountId, ServiceCreateRequest serviceCreate) throws DigimarcException
    {
        return m_jsonClient.doRequest(ServiceCreateResponse.class, HttpMethod.POST, serviceCreate, "/v2/service?account=%d", accountId);
    }

    @Override
    public void updateService(int serviceId, int accountId, ServiceUpdateRequest serviceUpdate) throws DigimarcException
    {
        m_jsonClient.doRequest(Void.class, HttpMethod.PUT, serviceUpdate, "/v2/service/%d?account=%d", serviceId, accountId);
    }

    @Override
    public void accountInternal(AccountInternalRequest accountInternalRequest)
        throws DigimarcException
    {
        m_jsonClient.doRequest(HttpMethod.PUT, accountInternalRequest, "/v2/internal/account");
    }
    
    

}
