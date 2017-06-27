package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class GtinValidator extends SimpleAttributeValidator
{
    private String[] m_gcps;
    
    
    public GtinValidator(String[] gcps)
    {
        super();
        m_gcps = gcps;
    }
    
    @Override
    public boolean validate(AttributeDesc attrDesc, String gtin, List<ProductValidationError> errors)
    {
        int oldCount = errors.size();
        if (gtin == null)
            errors.add(new ProductValidationErrorImpl("gtin", "Missing GTIN"));
        else if (!isAllDigits(gtin))
            errors.add(new ProductValidationErrorImpl("gtin", "GTIN must only contain digits"));
        else if (gtin.length() != 14 && gtin.length() != 13 && gtin.length() != 12 && gtin.length() != 8) 
            errors.add(new ProductValidationErrorImpl("gtin", "GTIN must be 14, 13, 12, or 8 digits"));
        else
        {
            String gtin14 = "000000".substring(0, 14 - gtin.length()) + gtin;
            if (!validCheckDigit(gtin14))
                errors.add(new ProductValidationErrorImpl("gtin", "GTIN check digit does not verify; check your GTIN for typographical errors"));
            else if (!authorizedGcp(gtin14, m_gcps))
                errors.add(new ProductValidationErrorImpl("gtin", "GTIN has a GS1 Company Prefix that you are not authorized to register"));
        }
        return errors.size() == oldCount;
    }
    private boolean isAllDigits(String gtin)
    {
        for (int i = 0; i < gtin.length(); i++)
        {
            char c = gtin.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

    private boolean validCheckDigit(String gtin14)
    {
        int sum = 0;
        for (int i = 0; i < 14; i++)
        {
            int multiplier = (i % 2) == 0 ? 3 : 1;
            sum += multiplier * (gtin14.charAt(i) - '0');
        }
        return (sum % 10) == 0;
    }

    private boolean authorizedGcp(String gtin14, String[] gcps)
    {
        if (gcps == null)
            return true;
        
        for (String gcp : gcps)
        {
            if (gcp.equals(gtin14.substring(1, 1+gcp.length())))
                return true;
        }
        return false;
    }


}
