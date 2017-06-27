package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;


public class WhitelistedCharValidator extends SimpleAttributeValidator
{
    private static final byte[] WHITELISTED_CATEGORIES = 
        {
         Character.LOWERCASE_LETTER,
         Character.UPPERCASE_LETTER,
         Character.TITLECASE_LETTER,
         Character.OTHER_LETTER,
         Character.MODIFIER_LETTER,
         Character.DECIMAL_DIGIT_NUMBER,
         Character.CONNECTOR_PUNCTUATION,
         Character.DASH_PUNCTUATION,
         Character.START_PUNCTUATION,
         Character.END_PUNCTUATION,
         Character.INITIAL_QUOTE_PUNCTUATION,
         Character.FINAL_QUOTE_PUNCTUATION,
         Character.OTHER_PUNCTUATION,
         Character.CURRENCY_SYMBOL,
         Character.MODIFIER_SYMBOL,
         Character.NON_SPACING_MARK,
         Character.COMBINING_SPACING_MARK,
         Character.ENCLOSING_MARK
        };
    
    @Override
    public boolean validate(AttributeDesc attrDesc, String value, List<ProductValidationError> validationErrors)
    {
        if (value == null)
            return true;
        
        for (int i = 0; i < value.length(); i++)
        {
            char c = value.charAt(i);
                        
            if (!isValid(c))
            {
                validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), attrDesc.getTitle() + " contains a character (" + c + ") that is not allowed"));
                return false;
            }
        }
        return true;
    }

    private boolean isValid(char c)
    {
        if (Character.isWhitespace(c))
            return true;
        
        int category = Character.getType(c);
        for (byte allowedCategory : WHITELISTED_CATEGORIES)
        {
            if (category == allowedCategory)
                return true;
        }
        
        if (c == '+' || c == '=' || c == '~' || c == '|')
            return true;
        
        
        int specialSymbolAsInt = (int) c;

        if (specialSymbolAsInt == 174 || specialSymbolAsInt == 8482 || specialSymbolAsInt == 169 )
        {
        	return true;
        }
        
        return false;
    }
}
