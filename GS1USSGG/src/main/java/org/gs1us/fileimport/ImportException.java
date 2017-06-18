package org.gs1us.fileimport;

public class ImportException extends Exception
{

    public ImportException()
    {
        super();
    }

    public ImportException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ImportException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ImportException(String message)
    {
        super(message);
    }

    public ImportException(Throwable cause)
    {
        super(cause);
    }

}
