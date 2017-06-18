package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class AccountCreateRequest
{
    // Company name of account (required),
    private String m_companyName; 
    
    private String m_brandOwner;
    
    // Industry (string(64)): Industry enumeration of account: Advertising, BrochuresCollateral, DirectMail, FreeStandingInserts, GreetingCards, GeneralSignage, Packaging, PublicationMagazine, PublicationNewspaper, PublicationOther, PromotionalItems, Other,
    
    // PrintEnvironment (string(64)): Print Environment enumeration of account: Commercial, Newspaper, HomeOffice,
    
    // CompanySize (string(32)): Company size enumeration of account: Unknown, LessThanTen, TenToFifty, FiftyOneToOneHundred, OneHundredOneToFiveHundred, MoreThanFiveHundred,
    
    // (boolean, optional):true if the account has opted out of receiving promotional email and newsletters; false otherwise,
    private boolean m_emailOptOut;
    
    // LeadSource (string(32)): Lead source enumeration of account,
    
    // UserPassword (string(64)): Existing user password,
    
    // IsDeveloperAccount (boolean(64),optional): Set to true if the account creator is a developer,
    
    // (ContactCreateRequest):Holds ContactCreateRequest (required),
    private ContactCreateRequest m_contactInfo;
    
    // (string(64)): Account number of this account in another system. Only needed for calls from an integrator account., 
    // This the GLN for us.
    private String m_accountNumber;

    public String getCompanyName()
    {
        return m_companyName;
    }

    public void setCompanyName(String companyName)
    {
        m_companyName = companyName;
    }
    
    

    public String getBrandOwner()
    {
        return m_brandOwner;
    }

    public void setBrandOwner(String brandOwner)
    {
        m_brandOwner = brandOwner;
    }

    public boolean isEmailOptOut()
    {
        return m_emailOptOut;
    }

    public void setEmailOptOut(boolean emailOptOut)
    {
        m_emailOptOut = emailOptOut;
    }

    public ContactCreateRequest getContactInfo()
    {
        return m_contactInfo;
    }

    public void setContactInfo(ContactCreateRequest contactInfo)
    {
        m_contactInfo = contactInfo;
    }

    public String getAccountNumber()
    {
        return m_accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        m_accountNumber = accountNumber;
    }

    

}
