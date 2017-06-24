package org.gs1us.sgg.dao.jpa;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

class StringArray 
{
    public static String[] toArrayOrNull(StringArray sa)
    {
        if (sa == null)
            return null;
        else
            return sa.getElts();
    }
    
    public static StringArray fromArrayOrNull(String[] a)
    {
        if (a == null)
            return null;
        else
            return new StringArray(a);
    }
    
    private String[] m_elts;
    
    public StringArray(String[] elts)
    {
        m_elts = elts;
    }
    
    public String[] getElts()
    {
        return m_elts;
    }

    String serialize()
    {
        StringWriter writer = new StringWriter();
        JsonGenerator g = Json.createGenerator(writer);
        g.writeStartArray();
        for (String elt : m_elts)
        {
            if (elt != null)
                g.write(elt);
        }
        g.writeEnd();
        g.flush();
        return writer.toString();
    }
    
    static StringArray deserialize(String s)
    {
        List<String> list = new ArrayList<>();
        
        JsonParser p = Json.createParser(new StringReader(s));
        if (p.hasNext() && p.next() == Event.START_ARRAY)
        {
            while (p.hasNext() && p.next() == Event.VALUE_STRING)
            {
                String elt = p.getString();
                list.add(elt);
            }
        }
        return new StringArray(list.toArray(new String[0]));
    }

}
