package org.gs1us.fileimport;

import java.util.Map;

/*
 * A stream of property sets, each of which associates a "key" with a set of properties, where each property associates
 * a property descriptor with a property value.
 */
public interface PropertySetSource<KeyType,PropertyDescType,PropertyValueType>
{
    /*
     * Advances to the first or next property set, returning true if there is one.
     */
    public boolean next() throws ImportException;
    
    /*
     * Returns the key for the current property set
     */
    public KeyType getKey();
    
    /*
     * Returns the properties of the current property set
     */
    public Map<PropertyDescType, PropertyValueType> getPropertyMap();
    
    /*
     * Returns a locator for each property in the set
     */
    public Map<PropertyDescType, ImportLocator> getLocatorMap();

}
