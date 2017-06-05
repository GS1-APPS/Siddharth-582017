package org.gs1us.sgg.dao.memberservice;

import java.math.BigDecimal;
import java.util.Date;

public interface Member
{
    public String getGln();
    public String getCompanyName();
    public String getAddress1();
    public String getAddress2();
    public String getCity();
    public String getState();
    public String getPostalCode();
    public String getMemberId();
    public String[] getGcps();
    public String getBrandOwnerAgreementSignedByUser();
    public Date getBrandOwnerAgreementSignedDate();
}
