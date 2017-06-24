package org.gs1us.sgg.transport;

public class HttpHeader
{
    private String m_name;
    private String m_value;
    
    public HttpHeader(String name, String value)
    {
        super();
        m_name = name;
        m_value = value;
    }
    public String getName()
    {
        return m_name;
    }
    public String getValue()
    {
        return m_value;
    }
}
