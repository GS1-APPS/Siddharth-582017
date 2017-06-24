package org.gs1us.sgg.dao.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProductAttributesConverter implements AttributeConverter<JpaAttributeSet, String>
{

    @Override
    public String convertToDatabaseColumn(JpaAttributeSet a)
    {
        return a.serialize();
    }

    @Override
    public JpaAttributeSet convertToEntityAttribute(String s)
    {
        return JpaAttributeSet.deserialize(s);
    }



}
