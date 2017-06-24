package org.gs1us.sgg.json;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gs1us.sgg.transport.HttpHeader;
import org.gs1us.sgg.transport.HttpRequest;
import org.gs1us.sgg.transport.HttpResponse;
import org.gs1us.sgg.transport.HttpTransport;
import org.gs1us.sgg.transport.HttpTransport.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
    //private HttpHeader m_authorizationHeader;
    private String m_authObjectClassName;
    private String m_authUseridMethodName;
    private String m_authApikeyMethodName;
    private boolean m_configurationBasedAuthEnabled;

    public GenericJsonHttpClient(String urlPrefix, String username, String password, HttpTransport transport,
    								String authObjectClassName, String authUseridMethodName, String authApikeyMethodName, 
    								boolean configurationBasedAuthEnabled, ObjectMapper objectMapper)
    {
        m_urlPrefix = urlPrefix;
        m_objectMapper = objectMapper;
        m_transport = transport;
        m_username = username;
        m_password = password;
        m_authObjectClassName = authObjectClassName;
        m_authUseridMethodName = authUseridMethodName;
        m_authApikeyMethodName  = authApikeyMethodName;
        m_configurationBasedAuthEnabled = configurationBasedAuthEnabled;
        
        /*
         * Commented to use actual Users identity - Username, API Key instead of config values.
         *  
         *  
 	        if (m_username != null && m_password != null)
	        {
	            String encodedUserPass = Base64.BASE64.encode(username + ":" + password);
	            m_authorizationHeader = new HttpHeader("Authorization", "Basic " + encodedUserPass);
	        }
        */
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
            
            // getAuthHeader determines if config based authorization is enabled.
           	HttpHeader m_authorizationHeader = getAuthHeader();
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
    
    /*
     * Fetch the Logged in Users Authentication object and build Authorization Header for HTTP Restful API call to GG module
     * 
     */
    protected HttpHeader getAuthHeader() {
    	String authenticatedUsername = null;
    	String authenticatedApiKey= null;
    	HttpHeader m_authorizationHeader = null;
    	
        if (m_configurationBasedAuthEnabled) {
	        if (m_username != null && m_password != null)
	        {
	            String encodedUserPass = Base64.BASE64.encode(m_username + ":" + m_password);
	            m_authorizationHeader = new HttpHeader("Authorization", "Basic " + encodedUserPass);
	            return m_authorizationHeader;
	        }
        }
    	
        /*
         * Proceed only if configuration based Auth is disabled
         */
    	try {
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	 
	    	Class<?> c = Class.forName(m_authObjectClassName);

	    	Object principal = authentication.getPrincipal();
	    	
	    	if (principal.getClass().getName().equals(m_authObjectClassName) ) {
	    		// Get the Authenticated username for HttpHeader from principal
	    		Method  method = c.getDeclaredMethod(m_authUseridMethodName);
	    		authenticatedUsername = (String) method.invoke(principal);	    		
	    		// Get the Authenticated Users API Key for HttpHeader from principal
	    		Method  methodApiKey = c.getDeclaredMethod(m_authApikeyMethodName);
	    		authenticatedApiKey = (String) methodApiKey.invoke(principal);
	    	}
	    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
	    	    if (authenticatedUsername != null && authenticatedApiKey != null)
	            {
	                String encodedUserPass = Base64.BASE64.encode(authenticatedUsername + ":" + authenticatedApiKey);
	                m_authorizationHeader = new HttpHeader("Authorization", "Basic " + encodedUserPass);
	                return m_authorizationHeader;
	            }    	    
	    	}
    	} catch(Exception e) {
    		System.out.println(e);
    		e.printStackTrace(System.out);
    		return null;
    	}
    	return null;
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