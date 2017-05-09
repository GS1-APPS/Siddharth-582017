package org.gs1us.sgg.gbservice.json;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeType;

public class InboundAttributeDesc implements AttributeDesc
{
    private String m_name;
    private String m_title;
    private String[] m_importHeadings;
    private String m_groupHeading;
    private AttributeType m_type;
    private List<InboundAttributeEnumValue> m_enumValues;
    private boolean m_required;
    private int m_actions;
    private String m_entryInstructions;
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public String getTitle()
    {
        return m_title;
    }
    public void setTitle(String title)
    {
        m_title = title;
    }
    
    public String[] getImportHeadings()
    {
        return m_importHeadings;
    }
    public void setImportHeadings(String[] importHeadings)
    {
        m_importHeadings = importHeadings;
    }
    public String getGroupHeading()
    {
        return m_groupHeading;
    }
    public void setGroupHeading(String groupHeading)
    {
        m_groupHeading = groupHeading;
    }
    public AttributeType getType()
    {
        return m_type;
    }
    public void setType(AttributeType type)
    {
        m_type = type;
    }
    
    public List<InboundAttributeEnumValue> getEnumValues()
    {
        return m_enumValues;
    }
    public void setEnumValues(List<InboundAttributeEnumValue> enumValues)
    {
        m_enumValues = enumValues;
    }
    public boolean isRequired()
    {
        return m_required;
    }
    public void setRequired(boolean required)
    {
        m_required = required;
    }
    public int getActions()
    {
        return m_actions;
    }
    public void setActions(int actions)
    {
        m_actions = actions;
    }
    public String getEntryInstructions()
    {
        return m_entryInstructions;
    }
    public void setEntryInstructions(String entryInstructions)
    {
        m_entryInstructions = entryInstructions;
    }
    
    
}
