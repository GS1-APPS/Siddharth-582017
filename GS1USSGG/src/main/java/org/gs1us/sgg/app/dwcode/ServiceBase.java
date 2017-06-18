package org.gs1us.sgg.app.dwcode;

import java.util.Date;

public class ServiceBase
{

    private String m_name;
    private String m_description;
    private String m_payoffUrl;
    private String m_payoffTitle;
    private Date m_startDate;
    private boolean m_autoExtend;
    private Date m_endDate;
    private String m_humanReadableId;

    public ServiceBase()
    {
        super();
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public String getDescription()
    {
        return m_description;
    }

    public void setDescription(String description)
    {
        m_description = description;
    }

    public String getPayoffUrl()
    {
        return m_payoffUrl;
    }

    public void setPayoffUrl(String payoffUrl)
    {
        m_payoffUrl = payoffUrl;
    }

    public String getPayoffTitle()
    {
        return m_payoffTitle;
    }

    public void setPayoffTitle(String payoffTitle)
    {
        m_payoffTitle = payoffTitle;
    }

    public Date getStartDate()
    {
        return m_startDate;
    }

    public void setStartDate(Date startDate)
    {
        m_startDate = startDate;
    }

    public boolean isAutoExtend()
    {
        return m_autoExtend;
    }

    public void setAutoExtend(boolean autoExtend)
    {
        m_autoExtend = autoExtend;
    }

    public Date getEndDate()
    {
        return m_endDate;
    }

    public void setEndDate(Date endDate)
    {
        m_endDate = endDate;
    }

    public String getHumanReadableId()
    {
        return m_humanReadableId;
    }

    public void setHumanReadableId(String humanReadableId)
    {
        m_humanReadableId = humanReadableId;
    }

}