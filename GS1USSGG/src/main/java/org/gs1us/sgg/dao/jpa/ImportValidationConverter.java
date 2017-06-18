package org.gs1us.sgg.dao.jpa;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.json.InboundImportValidation;
import org.gs1us.sgg.gbservice.json.ObjectMapperFactoryBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class ImportValidationConverter implements AttributeConverter<ImportValidation, String>
{
    private static final Logger s_logger = Logger.getLogger("org.gs1us.sgg.dao.jpa.ImportValidationConverter");
    
    private static ObjectMapper s_objectMapper = initObjectMapper();
    
    private static ObjectMapper initObjectMapper()
    {
        ObjectMapperFactoryBean factory = new ObjectMapperFactoryBean();
        return factory.getObject();
    }
    
    @Override
    public String convertToDatabaseColumn(ImportValidation a)
    {
        if (a == null)
            return null;
        
        try
        {
            return s_objectMapper.writeValueAsString(a);
        }
        catch (JsonProcessingException e)
        {
            s_logger.log(Level.SEVERE, "Unable to serialize", e);
            return null;
        }
    }

    @Override
    public ImportValidation convertToEntityAttribute(String s)
    {
        if (s == null)
            return null;
        
        try
        {
            return s_objectMapper.readValue(s, InboundImportValidation.class);
        }
        catch (JsonProcessingException e)
        {
            s_logger.log(Level.SEVERE, "Unable to serialize", e);
            return null;
        }
        catch (IOException e)
        {
            s_logger.log(Level.SEVERE, "Unable to serialize", e);
            return null;
        }

    }



}
