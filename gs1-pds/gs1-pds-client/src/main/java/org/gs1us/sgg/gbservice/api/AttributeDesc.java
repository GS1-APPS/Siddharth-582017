package org.gs1us.sgg.gbservice.api;

import java.util.List;

public interface AttributeDesc
{
    public String getName();
    public String getTitle();
    public String[] getImportHeadings();
    public String getGroupHeading();
    public AttributeType getType();
    public List<? extends AttributeEnumValue> getEnumValues();
    public boolean isRequired();
    public int getActions();
    public String getEntryInstructions();
}
