package org.gs1us.sgg.dao.jpa;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

class StringSet extends HashSet<String>
{

    public StringSet()
    {
        super();
    }

    public StringSet(Collection<? extends String> c)
    {
        super(c);
    }

    String serialize()
    {
        StringWriter writer = new StringWriter();
        JsonGenerator g = Json.createGenerator(writer);
        g.writeStartArray();
        for (String elt : this)
        {
            if (elt != null)
                g.write(elt);
        }
        g.writeEnd();
        g.flush();
        return writer.toString();
    }
    
    static StringSet deserialize(String s)
    {
        StringSet result = new StringSet();
        
        JsonParser p = Json.createParser(new StringReader(s));
        if (p.hasNext() && p.next() == Event.START_ARRAY)
        {
            while (p.hasNext() && p.next() == Event.VALUE_STRING)
            {
                String elt = p.getString();
                result.add(elt);
            }
        }
        return result;
    }

}
