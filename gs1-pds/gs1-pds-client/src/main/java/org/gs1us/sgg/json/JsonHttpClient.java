package org.gs1us.sgg.json;

import java.io.IOException;
import java.util.List;

import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerServiceException;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.ValidationException;
import org.gs1us.sgg.gbservice.json.ExceptionInfo;
import org.gs1us.sgg.transport.HttpResponse;
import org.gs1us.sgg.transport.HttpTransport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHttpClient extends GenericJsonHttpClient<GlobalBrokerException>
{
    public JsonHttpClient(String urlPrefix, String username, String password, HttpTransport transport,
    						String authObjectClassName, String authUseridMethodName, String authApikeyMethodName,
    						boolean configurationBasedAuthEnabled, ObjectMapper objectMapper)
    {
        super(urlPrefix, username, password, transport, authObjectClassName, authUseridMethodName, authApikeyMethodName, configurationBasedAuthEnabled, objectMapper);
    }

    protected <T> T throwServiceException(Exception e)
        throws GlobalBrokerServiceException
    {
        throw new GlobalBrokerServiceException(e);
    }

    protected <T> T processErrorResponse(HttpResponse response)
        throws GlobalBrokerException, JsonProcessingException, IOException
    {
        int responseCode = response.getResponseCode();
        
        if (responseCode < 400 || responseCode > 599)
            throw new GlobalBrokerServiceException("HTTP response code " + responseCode);
        
        if (response.getContent() == null || response.getContent().length == 0)
            throw new GlobalBrokerServiceException();
        else
        {
            ExceptionInfo result = getObjectMapper().readValue(response.getContent(), ExceptionInfo.class);
            if (result != null && result.getException() != null)
            {
                try
                {
                    Class<?> exceptionClass = Class.forName("org.gs1us.sgg.gbservice.api." + result.getException());
                    GlobalBrokerException e = (GlobalBrokerException)exceptionClass.newInstance();
                    if (result.getException().equals("ValidationException"))
                    {
                        ValidationException ve = (ValidationException)e;
                        ve.setErrors((List<ProductValidationError>)result.getValidationErrors());
                    }
                    throw e;
                }
                catch (ClassNotFoundException e)
                {
                    throw new GlobalBrokerServiceException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new GlobalBrokerServiceException(e);
                }
                catch (InstantiationException e)
                {
                    throw new GlobalBrokerServiceException(e);
                }
                catch (ClassCastException e)
                {
                    throw new GlobalBrokerServiceException(e);
                }
            }
            else
                throw new GlobalBrokerServiceException();
        }
    }

}
