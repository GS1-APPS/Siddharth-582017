package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ProductDetailsReadResponse extends ProductDetailsRequest
{
    private boolean m_certified;
    private String m_certificationNotes;
    public boolean isCertified()
    {
        return m_certified;
    }
    public void setCertified(boolean certified)
    {
        m_certified = certified;
    }
    public String getCertificationNotes()
    {
        return m_certificationNotes;
    }
    public void setCertificationNotes(String certificationNotes)
    {
        m_certificationNotes = certificationNotes;
    }
    
    
}
