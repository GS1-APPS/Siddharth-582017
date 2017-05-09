package org.gs1us.sgg.dao.jpa;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;


public class JpaAttributeSet extends AttributeSet implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 2491091543039025184L;
    
    private Map<String, String> m_map;
    
    public JpaAttributeSet()
    {
        m_map = new HashMap<String, String>();
    }
    
    public JpaAttributeSet(AttributeSet attributes)
    {
        if (attributes == null || attributes.getAttributes() == null)
            m_map = new HashMap<>();
        else
            m_map = new HashMap<>(attributes.getAttributes());
    }

    
    @Override
    public Map<String, String> getAttributes()
    {
        return m_map;
    }

    @Override
    public void setAttributes(Map<String, String> attributes)
    {
        m_map = new HashMap<>(attributes);        
    }

    /*    
    String serialize()
    {
        StringBuffer buf = new StringBuffer();
        {
            for (Map.Entry<String, String> entry : m_map.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();
                
                if (value != null)
                {
                    buf.append(key);
                    buf.append("=");
                    buf.append(value);
                    buf.append(";");
                }
            }
        }
        return buf.toString();
    }
    
    static JpaProductAttributes deserialize(String s)
    {
        JpaProductAttributes result = new JpaProductAttributes();
        String[] clauses = s.split(";");
        for (String clause : clauses)
        {
            String[] parts = clause.split("=");
            if (parts.length == 2)
            {
                result.m_map.put(parts[0], parts[1]);
            }
        }
        return result;
    }
*/
    String serialize()
    {
        if (m_map.size() == 0)
            return null;
        
        StringWriter writer = new StringWriter();
        JsonGenerator g = Json.createGenerator(writer);
        g.writeStartObject();
        for (Map.Entry<String, String> entry : m_map.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            
            if (value != null)
                g.write(key, value);
        }
        g.writeEnd();
        g.flush();
        return writer.toString();
    }
    
    static JpaAttributeSet deserialize(String s)
    {
        JpaAttributeSet result = new JpaAttributeSet();
        
        if (s != null)
        {
            JsonParser p = Json.createParser(new StringReader(s));
            if (p.hasNext() && p.next() == Event.START_OBJECT)
            {
                while (p.hasNext() && p.next() == Event.KEY_NAME)
                {
                    String key = p.getString();
                    if (p.next() != Event.VALUE_STRING)
                        break;
                    String value = p.getString();
                    result.m_map.put(key, value);
                }
            }
        }
        
        return result;
    }
}
