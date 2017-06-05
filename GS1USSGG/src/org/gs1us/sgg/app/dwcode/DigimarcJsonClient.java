package org.gs1us.sgg.app.dwcode;

import java.io.IOException;

import org.gs1us.sgg.json.GenericJsonHttpClient;
import org.gs1us.sgg.transport.HttpResponse;
import org.gs1us.sgg.transport.HttpTransport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DigimarcJsonClient extends GenericJsonHttpClient<DigimarcException>
{

    public DigimarcJsonClient(String urlPrefix, String username, String password, HttpTransport transport, ObjectMapper objectMapper)
    {
        super(urlPrefix, username, password, transport, null, null, null, true, objectMapper);
    }

    @Override
    protected <T> T processErrorResponse(HttpResponse response)
        throws DigimarcException, JsonProcessingException, IOException
    {
        // TODO Auto-generated method stub
        throw new DigimarcException();
    }

    @Override
    protected <T> T throwServiceException(Exception e) throws DigimarcException
    {
        throw new DigimarcException(e);
    }

}
