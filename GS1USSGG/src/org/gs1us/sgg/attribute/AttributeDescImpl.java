package org.gs1us.sgg.attribute;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeEnumValue;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeType;
import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.validation.AttributeValidator;

public class AttributeDescImpl implements AttributeDesc
{
    private String m_name;
    private String m_title;
    private String[] m_importHeadings;
    private String m_groupHeading;
    private AttributeType m_type;
    private List<AttributeEnumValue> m_enumValues;
    private boolean m_isRequired;
    private int m_actions;
    private String m_entryInstructions;
    
    private AttributeValidator[] m_validators;
    
    public AttributeDescImpl(String name, String title, String importHeadings[], String groupHeading, AttributeType type,
            boolean isRequired, String entryInstructions, AttributeValidator[] validators)
    {
        this(name, title, importHeadings, groupHeading, type, null, isRequired, Action.ALL_ACTIONS_MASK, entryInstructions, validators);
    }
    
    public AttributeDescImpl(String name, String title, String[] importHeadings, String groupHeading, AttributeType type, List<AttributeEnumValue> enumValues,
            boolean isRequired, int actions, String entryInstructions, AttributeValidator[] validators)
    {
        super();
        m_name = name;
        m_title = title;
        m_importHeadings = importHeadings;
        m_groupHeading = groupHeading;
        m_type = type;
        m_enumValues = enumValues;
        m_isRequired = isRequired;
        m_actions = actions;
        m_entryInstructions = entryInstructions;
        m_validators = validators;
    }

    public String getName()
    {
        return m_name;
    }

    
    
    public String getTitle()
    {
        return m_title;
    }

    public String[] getImportHeadings()
    {
        return m_importHeadings;
    }
    
    public String getGroupHeading()
    {
        return m_groupHeading;
    }

    public AttributeType getType()
    {
        return m_type;
    }

    public List<AttributeEnumValue> getEnumValues()
    {
        return m_enumValues;
    }

    public boolean isRequired()
    {
        return m_isRequired;
    }
    
    

    public int getActions()
    {
        return m_actions;
    }

    public String getEntryInstructions()
    {
        return m_entryInstructions;
    }

    public AttributeValidator[] getValidators()
    {
        return m_validators;
    }

    public void validateAttribute(HasAttributes objectToValidate, List<ProductValidationError> validationErrors)
    {
        AttributeValidator[] validators = getValidators();
        if (validators != null)
        {
            for (AttributeValidator validator : validators)
            {
                if (!validator.validate(this, objectToValidate, validationErrors))
                    break;
            }
        }
    }
    


}
