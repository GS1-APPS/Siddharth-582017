package org.gs1us.sgg.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringSetConverter implements AttributeConverter<StringSet, String>
{

    @Override
    public String convertToDatabaseColumn(StringSet s)
    {
        if (s == null)
            return null;
        else
            return s.serialize();        
    }

    @Override
    public StringSet convertToEntityAttribute(String s)
    {
        if (s == null)
            return null;
        else
            return StringSet.deserialize(s);
    }

}
