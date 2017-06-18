package org.gs1us.sgg.app.dwcode;

public class DummyDigimarcClient implements DigimarcService
{
    public boolean isEnabled()
    {
        return false;
    }
    
    
    
    @Override
    public UserAccountsReadResponse getAccounts() throws DigimarcException
    {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public AccountReadResponse getAccount(int accountId)
        throws DigimarcException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccountCreateResponse createAccount(
            AccountCreateRequest accountCreate) throws DigimarcException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProjectCreateResponse createProject(int accountId, 
            ProjectCreateRequest projectCreate) throws DigimarcException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    

    @Override
    public ServiceReadResponse getService(int serviceId, int accountId)
        throws DigimarcException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServiceCreateResponse createService(int accountId, ServiceCreateRequest serviceCreate) throws DigimarcException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateService(int serviceId, int accountId,
            ServiceUpdateRequest serviceUpdate) throws DigimarcException
    {
        // TODO Auto-generated method stub

    }



    @Override
    public void accountInternal(AccountInternalRequest accountInternalRequest)
        throws DigimarcException
    {
        
    }

    
}
