package org.gs1us.sgg.transport;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import org.gs1us.sgg.transport.HttpTransport.HttpMethod;

public class HttpRequest
{
    private URL m_url;
    private HttpTransport.HttpMethod m_method;
    private SSLSocketFactory m_sslSocketFactory;
    private List<HttpHeader> m_headers = new ArrayList<HttpHeader>();
    private byte[] m_content = null;
    
    public HttpRequest(URL url, HttpMethod method)
    {
        super();
        m_url = url;
        m_method = method;
    }

    public URL getUrl()
    {
        return m_url;
    }

    public void setUrl(URL url)
    {
        m_url = url;
    }

    public HttpTransport.HttpMethod getMethod()
    {
        return m_method;
    }

    public void setMethod(HttpTransport.HttpMethod method)
    {
        m_method = method;
    }
    
    public SSLSocketFactory getSslSocketFactory()
    {
        return m_sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory)
    {
        m_sslSocketFactory = sslSocketFactory;
    }

    public List<HttpHeader> getHeaders()
    {
        return m_headers;
    }

    public void addHeader(HttpHeader header)
    {
        m_headers.add(header);
    }

    public byte[] getContent()
    {
        return m_content;
    }

    public void setContent(byte[] content)
    {
        m_content = content;
    }
    
    
    
}
