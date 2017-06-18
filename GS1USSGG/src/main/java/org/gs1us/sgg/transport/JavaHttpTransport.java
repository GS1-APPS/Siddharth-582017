package org.gs1us.sgg.transport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;

public class JavaHttpTransport implements HttpTransport
{
    private static Executor s_executor = Executors.newFixedThreadPool(10);

    @Override
    public HttpResponse doRequest(HttpRequest request) throws IOException
    {
        URL url = request.getUrl();
        URLConnection c = url.openConnection();
        if (c instanceof HttpURLConnection)
        {
            HttpURLConnection conn = (HttpURLConnection)c;
            
            if (request.getSslSocketFactory() != null && conn instanceof HttpsURLConnection)
                ((HttpsURLConnection)conn).setSSLSocketFactory(request.getSslSocketFactory());
            
            conn.setRequestMethod(genericToJavaHttpMethod(request.getMethod()));
            if (request.getHeaders() != null)
            {
                for (HttpHeader header : request.getHeaders())
                {
                    conn.setRequestProperty(header.getName(), header.getValue());
                }
            }
            conn.setDoInput(true);

            byte[] requestContent = request.getContent();
            if (requestContent == null)
                conn.setDoOutput(false);
            else
            {
                conn.setDoOutput(true);
                OutputStream requestStream = conn.getOutputStream();
                requestStream.write(requestContent);
                requestStream.close();
            }
            
            int responseCode = conn.getResponseCode();
            List<HttpHeader> responseHeaders = getResponseHeaders(conn);
            InputStream responseStream = null;
            try
            {
                responseStream = conn.getInputStream();
            }
            catch (IOException e)
            {
                responseStream = conn.getErrorStream();
            }
            // TODO:  could look at content length header to help out here.
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            if (responseStream != null)
            {
                int b;
                while ((b = responseStream.read()) >= 0)
                    buf.write(b);
                responseStream.close();
            }
            byte[] responseContent = buf.toByteArray();
            return new HttpResponse(responseCode, responseContent, responseHeaders);
        }
        else
            // TODO
            return null;
    }

    private String genericToJavaHttpMethod(HttpMethod method)
    {
        // Here taking advantage of the fact that the Java enum names in HttpMethod
        // happen to be exactly the strings that HttpURLConnection wants to see.
        return method.toString();
    }

    private List<HttpHeader> getResponseHeaders(HttpURLConnection conn)
    {
        List<HttpHeader> headers = new ArrayList<HttpHeader>();
        int i = 1;
        while (conn.getHeaderFieldKey(i) != null)
        {
            headers.add(new HttpHeader(conn.getHeaderFieldKey(i), conn.getHeaderField(i)));
            i++;
        }
        return headers;
    }

    @Override
    public Future<HttpResponse> doRequestAsync(final HttpRequest request)
    {
        FutureTask<HttpResponse> result =
            new FutureTask<HttpResponse>(new Callable<HttpResponse>(){

            @Override
            public HttpResponse call() throws Exception
            {
                return doRequest(request);
            }
        });
        s_executor.execute(result);
        return result;
    }

}
