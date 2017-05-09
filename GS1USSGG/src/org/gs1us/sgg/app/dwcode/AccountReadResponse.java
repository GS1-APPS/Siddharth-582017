package org.gs1us.sgg.app.dwcode;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AccountReadResponse
{
 // Id (integer(32)): Unique identifier for the account,
    private int m_id;
    // 
    // CompanyName (string(64)): Company name of account,
    private String m_companyName;
    
    private String m_brandOwner;
    // 
    // Industry (string(64)): Industry enumeration of account: Advertising, BrochuresCollateral, DirectMail, FreeStandingInserts, GreetingCards, GeneralSignage, Packaging, PublicationMagazine, PublicationNewspaper, PublicationOther, PromotionalItems, Other,
    // 
    // PrintEnvironment (string(64)): Print Environment enumeration of account: Commercial, Newspaper, HomeOffice,
    // 
    // CompanySize (string(32)): Company size enumeration of account: Unknown, LessThanTen, TenToFifty, FiftyOneToOneHundred, OneHundredOneToFiveHundred, MoreThanFiveHundred,
    // 
    // EmailOptOut (boolean): true if the account has opted out of receiving promotional email and newsletters; false otherwise,
    private boolean m_emailOptOut;
    // 
    // GS1PrefixCount (integer(32)): (Deprecated, always set to zero) Count of GS1 prefixes associated with the account,
    // 
    // PaymentEnabled (boolean): Indicates whether the account has been set up with payment,
    // 
    // PaymentSetup (string): Payment setup date,
    // 
    // DefaultPayoffUrl (string(512)): The default service payoff URL for the account,
    // 
    // DefaultPayoffTitle (string(256)): The default service payoff title for the account,
    // 
    // DefaultImageEmbedStrength (integer(8), optional): The default image embed strength for the account,
    // 
    // ModifiedDate (string): Date and time of last update,
    // 
    // ModifiedBy (string(64)): User name used for last update,
    // 
    // ContactInfo (ContactReadResponse): Holds ContactCreateRequest,
    private ContactReadResponse m_contactInfo;
    // 
    // CreateDate (string): Date the account was created,
    private Date m_createDate;
    // 
    // DefaultBannerImageUrl (string(128)): The URL to the default banner image for the account,
    // 
    // AccountNumber (string(64)): Account number of this account in another system. Only needed for calls from an integrator account.,
    private String m_accountNumber;
    // 
    // IntegratorName (string(32)): The name of the integrator this account is related to if any.
    private String m_integratorName;
    public int getId()
    {
        return m_id;
    }
    public void setId(int id)
    {
        m_id = id;
    }
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
    public ContactReadResponse getContactInfo()
    {
        return m_contactInfo;
    }
    public void setContactInfo(ContactReadResponse contactInfo)
    {
        m_contactInfo = contactInfo;
    }
    public Date getCreateDate()
    {
        return m_createDate;
    }
    public void setCreateDate(Date createDate)
    {
        m_createDate = createDate;
    }
    public String getAccountNumber()
    {
        return m_accountNumber;
    }
    public void setAccountNumber(String accountNumber)
    {
        m_accountNumber = accountNumber;
    }
    public String getIntegratorName()
    {
        return m_integratorName;
    }
    public void setIntegratorName(String integratorName)
    {
        m_integratorName = integratorName;
    }
    
    
}
