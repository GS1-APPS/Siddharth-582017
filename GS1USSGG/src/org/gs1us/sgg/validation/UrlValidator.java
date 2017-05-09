package org.gs1us.sgg.validation;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.util.Util;

public class UrlValidator extends SimpleAttributeValidator
{
    private static final String[] DEFAULT_PREFIXES = new String[]{"http", "https"};
    
    private String[] m_prefixes;
    
    public UrlValidator(String[] prefixes)
    {
        m_prefixes = prefixes;
    }
    public UrlValidator()
    {
        this(DEFAULT_PREFIXES);
    }
    
    @Override
    public boolean validate(AttributeDesc attrDesc, String value, List<ProductValidationError> validationErrors)
    {
        if (value == null)
            return true;
        
        try
        {
            URL parsed = new URL(value);
            if (!isValidPrefix(value))
            {
                validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "Please enter a valid http or https URL (including a hostname)"));
                return false;
            }
            return true;
        }
        catch (MalformedURLException e)
        {
            // Try putting an http: in front
            try
            {
                URL parsedAgain = new URL("http://" + value);
                return true;
            }
            catch (MalformedURLException ee)
            {
                
            }
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "Please enter a valid http or https URL"));
            return false;
        }
    }
    
    private boolean isValidPrefix(String value)
    {
        String lowercaseValue = value.toLowerCase();
        for (String prefix : m_prefixes)
        {
            if (lowercaseValue.startsWith(prefix + "://"))
                return true;
        }
        return false;
    }
    
}
