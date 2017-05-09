package org.gs1us.sgg.daoutil.jpa;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class HibernateNamingStrategy extends ImprovedNamingStrategy
{
    

    @Override
    public String columnName(String columnName)
    {
        return removeHungarianPrefix(super.columnName(columnName));
    }

    @Override
    public String foreignKeyColumnName(String propertyName,
            String propertyEntityName, String propertyTableName,
            String referencedColumnName)
    {
        return removeHungarianPrefix(super.foreignKeyColumnName(propertyName, propertyEntityName,
                                          propertyTableName, referencedColumnName));
    }

    @Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable)
    {
        return removeHungarianPrefix(super.joinKeyColumnName(joinedColumn, joinedTable));
    }

    @Override
    public String logicalCollectionColumnName(String columnName,
            String propertyName, String referencedColumn)
    {
        return removeHungarianPrefix(super.logicalCollectionColumnName(columnName, propertyName,
                                                 referencedColumn));
    }

    @Override
    public String logicalColumnName(String columnName, String propertyName)
    {
        return removeHungarianPrefix(super.logicalColumnName(columnName, propertyName));
    }

    @Override
    public String propertyToColumnName(String propertyName)
    {
        return removeHungarianPrefix(super.propertyToColumnName(propertyName));
    }

    private String removeHungarianPrefix(String s)
    {
        if (s.length() >= 3 && s.charAt(1) == '_')
            return s.substring(2);
        else
            return s;
    }


}
