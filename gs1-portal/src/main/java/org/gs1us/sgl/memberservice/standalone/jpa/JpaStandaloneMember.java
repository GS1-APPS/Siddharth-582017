package org.gs1us.sgl.memberservice.standalone.jpa;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgl.memberservice.standalone.StandaloneMember;

@Entity
@Table(name="standalonemember")
public class JpaStandaloneMember extends StandaloneMember
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;
    
    @Basic
    private String m_gln;
    
    @Basic
    private String m_companyName;
    
    @Basic
    private String m_address1;
    
    @Basic
    private String m_address2;
    
    @Basic
    private String m_city;
    
    @Basic
    private String m_state;
    
    @Basic
    private String m_postalCode;
    
    @Basic
    private String m_memberId;
    
    @Basic
    private String m_gcps;
    
    @Basic
    private String m_brandOwnerAgreementSignedByUser;
    
    @Basic
    private Date m_brandOwnerAgreementSignedDate;

    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getGln()
    {
        return m_gln;
    }

    public void setGln(String gln)
    {
        m_gln = gln;
    }

    public String getCompanyName()
    {
        return m_companyName;
    }

    public void setCompanyName(String companyName)
    {
        m_companyName = companyName;
    }
    
    

    public String getAddress1()
    {
        return m_address1;
    }

    public void setAddress1(String address1)
    {
        m_address1 = address1;
    }

    public String getAddress2()
    {
        return m_address2;
    }

    public void setAddress2(String address2)
    {
        m_address2 = address2;
    }

    public String getCity()
    {
        return m_city;
    }

    public void setCity(String city)
    {
        m_city = city;
    }

    public String getState()
    {
        return m_state;
    }

    public void setState(String state)
    {
        m_state = state;
    }

    public String getPostalCode()
    {
        return m_postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        m_postalCode = postalCode;
    }

    public String getMemberId()
    {
        return m_memberId;
    }

    public void setMemberId(String memberId)
    {
        m_memberId = memberId;
    }

    public String[] getGcps()
    {
        return m_gcps.split(",");
    }

    public void setGcps(String[] gcps)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < gcps.length; i++)
        {
            if (i > 0)
                buf.append(",");
            buf.append(gcps[i]);
        }
        m_gcps = buf.toString();
    }

    public String getBrandOwnerAgreementSignedByUser()
    {
        return m_brandOwnerAgreementSignedByUser;
    }

    public void setBrandOwnerAgreementSignedByUser(
            String brandOwnerAgreementSignedByUser)
    {
        m_brandOwnerAgreementSignedByUser = brandOwnerAgreementSignedByUser;
    }

    public Date getBrandOwnerAgreementSignedDate()
    {
        return m_brandOwnerAgreementSignedDate;
    }

    public void setBrandOwnerAgreementSignedDate(
            Date brandOwnerAgreementSignedDate)
    {
        m_brandOwnerAgreementSignedDate = brandOwnerAgreementSignedDate;
    }
 
    
}
