package org.gs1us.sgg.app.dwcode;

public interface DigimarcService
{
    boolean isEnabled();
    
    UserAccountsReadResponse getAccounts() throws DigimarcException;
    
    AccountReadResponse getAccount(int accountId) throws DigimarcException;
    AccountCreateResponse createAccount(AccountCreateRequest accountCreate) throws DigimarcException;
    
    ProjectCreateResponse createProject(int accountId, ProjectCreateRequest projectCreate) throws DigimarcException;
    
    ServiceReadResponse getService(int serviceId, int accountId) throws DigimarcException;
    ServiceCreateResponse createService(int accountId, ServiceCreateRequest serviceCreate) throws DigimarcException;
    void updateService(int serviceId, int accountId, ServiceUpdateRequest serviceUpdate) throws DigimarcException;
    
    void accountInternal(AccountInternalRequest accountInternalRequest) throws DigimarcException;
}
