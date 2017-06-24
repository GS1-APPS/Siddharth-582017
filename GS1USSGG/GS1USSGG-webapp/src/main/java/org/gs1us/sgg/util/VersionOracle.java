package org.gs1us.sgg.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class VersionOracle
{
    private String m_version;

    public VersionOracle(String path)
    {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
                Reader r = new InputStreamReader(is))
        {
            StringBuffer buf = new StringBuffer();
            int c;
            while ((c = r.read()) > 0 && !Character.isWhitespace(c))
                buf.append((char)c);
            m_version = buf.toString();
        }
        catch (Exception e)
        {
            m_version = "[unknown]";
        }
    }

    public String getVersion()
    {
        return m_version;
    }
    
    
}
