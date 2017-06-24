package org.gs1us.sgg.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringArrayConverter implements AttributeConverter<StringArray, String>
{

    @Override
    public String convertToDatabaseColumn(StringArray s)
    {
        if (s == null)
            return null;
        else
            return s.serialize();        
    }

    @Override
    public StringArray convertToEntityAttribute(String s)
    {
        if (s == null)
            return null;
        else
            return StringArray.deserialize(s);
    }

}
