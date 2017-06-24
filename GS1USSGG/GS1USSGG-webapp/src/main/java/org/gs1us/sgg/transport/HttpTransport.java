package org.gs1us.sgg.transport;

import java.io.IOException;
import java.util.concurrent.Future;

public interface HttpTransport
{
    public enum HttpMethod
    {
        DELETE,
        GET,
        HEAD,
        POST,
        PUT
    }
    
    HttpResponse doRequest(HttpRequest request) throws IOException;
    
    Future<HttpResponse> doRequestAsync(HttpRequest request);
}
