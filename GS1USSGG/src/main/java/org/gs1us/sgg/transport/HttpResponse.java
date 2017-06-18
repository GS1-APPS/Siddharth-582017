package org.gs1us.sgg.transport;

import java.util.List;

public class HttpResponse
{
    private int m_responseCode;
    private byte[] m_content;
    private List<HttpHeader> m_headers;
    
    public HttpResponse(int responseCode, byte[] content, List<HttpHeader> headers)
    {
        super();
        m_responseCode = responseCode;
        m_content = content;
        m_headers = headers;
    }

    public int getResponseCode()
    {
        return m_responseCode;
    }

    public byte[] getContent()
    {
        return m_content;
    }

    public List<HttpHeader> getHeaders()
    {
        return m_headers;
    }
}
