package org.gs1us.sgg.dao.memberservice.standalone;

import java.math.BigDecimal;
import java.util.Date;

import org.gs1us.sgg.dao.memberservice.Member;
import org.gs1us.sgg.gbservice.api.AttributeSet;


public abstract class StandaloneMember implements Member
{

    @Override
    public abstract String getGln();
    public abstract void setGln(String gln);

    @Override
    public abstract String getCompanyName();
    public abstract void setCompanyName(String companyName);
    
    @Override
    public abstract String getAddress1();
    public abstract void setAddress1(String address1);
    
    @Override
    public abstract String getAddress2();
    public abstract void setAddress2(String address2);
    
    @Override
    public abstract String getCity();
    public abstract void setCity(String city);
    
    @Override
    public abstract String getState();
    public abstract void setState(String state);
    
    @Override
    public abstract String getPostalCode();
    public abstract void setPostalCode(String postalCode);
    
    @Override
    public abstract String getMemberId();
    public abstract void setMemberId(String memberId);

    @Override
    public abstract String[] getGcps();
    public abstract void setGcps(String[] gcps);
    
    @Override
    public abstract String getBrandOwnerAgreementSignedByUser();
    public abstract void setBrandOwnerAgreementSignedByUser(String brandOwnerAgreementSignedByUser);

    @Override
    public abstract Date getBrandOwnerAgreementSignedDate();
    public abstract void setBrandOwnerAgreementSignedDate(Date brandOwnerAgreementSignedDate);

   
    // Standalone only
    public abstract String getId();
    public abstract void setId(String id);

}
