package org.gs1us.sgg.json;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gs1us.sgg.transport.HttpHeader;
import org.gs1us.sgg.transport.HttpRequest;
import org.gs1us.sgg.transport.HttpResponse;
import org.gs1us.sgg.transport.HttpTransport;
import org.gs1us.sgg.transport.HttpTransport.HttpMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public abstract class GenericJsonHttpClient<E extends Exception>
{

    private static final Logger s_logger = Logger.getLogger("org.gs1us.sgg.json.JsonHttpClient");
    private HttpTransport m_transport;
    private ObjectMapper m_objectMapper;
    private String m_urlPrefix;
    private String m_username;
    private String m_password;
    private HttpHeader m_authorizationHeader;

    public GenericJsonHttpClient(String urlPrefix, String username, String password, HttpTransport transport, ObjectMapper objectMapper)
    {
        m_urlPrefix = urlPrefix;
        m_objectMapper = objectMapper;
        m_transport = transport;
        m_username = username;
        m_password = password;
        
        if (m_username != null && m_password != null)
        {
            String encodedUserPass = Base64.BASE64.encode(username + ":" + password);
            m_authorizationHeader = new HttpHeader("Authorization", "Basic " + encodedUserPass);
        }
    }
    
    protected ObjectMapper getObjectMapper()
    {
        return m_objectMapper;
    }

    public void doRequest(HttpMethod method, Object bodyObject, String urlFormat, Object... formatArgs)
        throws E
    {
         doRequest((ObjectReader)null, method, bodyObject, urlFormat, formatArgs);
    }

    public <T> T doRequest(Class<T> returnClass, HttpMethod method, Object bodyObject, String urlFormat, Object... formatArgs) throws E
    {
        ObjectReader returnReader = m_objectMapper.readerFor(returnClass);
        return doRequest(returnReader, method, bodyObject, urlFormat, formatArgs);
    }

    public <T extends Collection<Elt>, Elt> T doRequest(Class<T> returnCollectionClass, Class<Elt> returnEltClass, HttpMethod method, Object bodyObject, String urlFormat, Object... formatArgs)
        throws E
    {
        ObjectReader returnReader = m_objectMapper.readerFor(m_objectMapper.getTypeFactory().constructCollectionType(returnCollectionClass, returnEltClass));
        return doRequest(returnReader, method, bodyObject, urlFormat, formatArgs);
    }

    public void doRequest(HttpMethod method, String requestContentType, byte[] requestContent, String urlFormat, Object... formatArgs)
            throws E
    {
        doRequest((ObjectReader)null, method, requestContentType, requestContent, urlFormat, formatArgs);
    }

    public <T> T doRequest(Class<T> returnClass, HttpMethod method, String requestContentType, byte[] requestContent, String urlFormat, Object... formatArgs) throws E
    {
        ObjectReader returnReader = m_objectMapper.readerFor(returnClass);
        return doRequest(returnReader, method, requestContentType, requestContent, urlFormat, formatArgs);
    }

    public <T extends Collection<Elt>, Elt> T doRequest(Class<T> returnCollectionClass, Class<Elt> returnEltClass, HttpMethod method, byte[] requestContent, String urlFormat, Object... formatArgs)
            throws E
    {
        ObjectReader returnReader = m_objectMapper.readerFor(m_objectMapper.getTypeFactory().constructCollectionType(returnCollectionClass, returnEltClass));
        return doRequest(returnReader, method, requestContent, urlFormat, formatArgs);
    }

    public <T> T doRequest(ObjectReader returnReader, HttpMethod method, Object bodyObject, String urlFormat, Object... formatArgs) throws E
    {
        try
        {
            byte[] requestContent = null;
            if (bodyObject != null)
                requestContent = m_objectMapper.writeValueAsBytes(bodyObject);
            return doRequest(returnReader, method, "application/json", requestContent, urlFormat, formatArgs);
        }
        catch (JsonProcessingException e)
        {
            return throwServiceException(e);
        }
    }

    public <T> T doRequest(ObjectReader returnReader, HttpMethod method, String requestContentType, byte[] requestContent, String urlFormat, Object... formatArgs) throws E
    {
        Level logLevel = Level.FINE;
        try
        {
            for (int i = 0; i < formatArgs.length; i++)
                formatArgs[i] = escapeForUrl(formatArgs[i]);
    
            String urlString = String.format(m_urlPrefix + urlFormat, formatArgs);
            URL url = new URL(urlString);
    
            HttpRequest request = new HttpRequest(url, method);
            request.addHeader(new HttpHeader("Accept", "application/json"));
            if (m_authorizationHeader != null)
                request.addHeader(m_authorizationHeader);
            if (requestContent != null)
            {
                if (requestContentType != null)
                    request.addHeader(new HttpHeader("Content-Type", requestContentType));
                request.setContent(requestContent);
                s_logger.log(logLevel, "content is " + new String(requestContent));
            }
    
            HttpResponse response = m_transport.doRequest(request);
            int responseCode = response.getResponseCode();
            if (responseCode < 200 || responseCode > 299)
                logLevel = Level.WARNING;
            
            if (response.getContent() != null)
                s_logger.log(logLevel, "response is " + new String(response.getContent()));

    
            
            s_logger.log(logLevel, String.format("%s %s ==> %d\n", request.getMethod(), urlString, responseCode));
    
            if (responseCode >= 200 && responseCode <= 299)
            {
                if (returnReader == null || response.getContent() == null || response.getContent().length == 0)
                    return null;
                else
                {
                    T result = returnReader.readValue(response.getContent());
                    return result;
                }
            }
            else 
                return processErrorResponse(response);
        }
        catch (JsonProcessingException e)
        {
            return throwServiceException(e);
        }
        catch (IOException e)
        {
            return throwServiceException(e);
        }
    }
    
    protected abstract <T> T processErrorResponse(HttpResponse response) throws E, JsonProcessingException, IOException;
    protected abstract <T> T throwServiceException(Exception e) throws E;

    private Object escapeForUrl(Object o)
    {
        if (o == null)
            return null;
        if (o instanceof String)
        {
            StringBuffer buf = new StringBuffer();
            String s = o.toString();
            for (int i = 0; i < s.length(); i++)
            {
                char c = s.charAt(i);
                if (c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '-' || c == '.' || c == '_' || c == '~')
                    buf.append(c);
                else if (c >= 32 && c <= 127)
                {
                    buf.append('%');
                    buf.append(String.format("%02x", (int)c));
                }
                // Else just ignore non-ASCII character
            }
            return buf.toString();
        }
        else
            return o;
    }

}