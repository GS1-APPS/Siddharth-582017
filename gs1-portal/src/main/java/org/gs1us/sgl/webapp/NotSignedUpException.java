package org.gs1us.sgl.webapp;

import org.gs1us.sgg.gbservice.api.GlobalBrokerException;

public class NotSignedUpException extends GlobalBrokerException
{

    public NotSignedUpException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public NotSignedUpException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public NotSignedUpException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public NotSignedUpException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public NotSignedUpException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
