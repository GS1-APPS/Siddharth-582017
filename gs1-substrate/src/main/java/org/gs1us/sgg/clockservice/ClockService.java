package org.gs1us.sgg.clockservice;

import java.util.Date;

/**
 * The clock service provides the current time, allowing this to be set to a fixed time for testing purposes.
 * @author kt
 *
 */
public class ClockService
{
    private Date m_now;
    
    public Date now()
    {
        if (m_now == null)
            return new Date();
        else
            return m_now;
    }
    
    public void setNow(Date nowOrNull)
    {
        m_now = nowOrNull;
    }
    
    public boolean isNowFixed()
    {
        return m_now != null;
    }
    
    public Date fromNow(long deltaMillis)
    {
        return new Date(now().getTime() + deltaMillis);
    }

}
