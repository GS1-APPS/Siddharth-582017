package org.gs1us.sgl.webapp;

import java.util.ArrayList;
import java.util.List;

public class SortOrder
{
    public enum Direction
    {
        ASC("asc", "A"),
        DESC("desc", "D");
        
        private static Direction deserialize(String serialized)
        {
            for (Direction d : Direction.values())
            {
                if (d.serialize().equals(serialized))
                    return d;
            }
            return null;
        }
        
        private String m_sql;
        private String m_serialized;
        
        private Direction(String sql, String serialized)
        {
            m_sql = sql;
            m_serialized = serialized;
        }
        
        private String getSql()
        {
            return m_sql;
        }
        
        private String serialize()
        {
            return m_serialized;
        }
        
        public Direction invert()
        {
            if (this == ASC)
                return DESC;
            else
                return ASC;
        }
    }
    
    public interface Transform
    {
        String transform(String key);
    }
    
    public static SortOrder deserialize(String serialized)
    {
        if (serialized == null)
            return null;
        
        String[] components = serialized.split(",");
        
        List<String> keyNames = new ArrayList<String>(components.length);
        List<Direction> directions = new ArrayList<Direction>(components.length);
        
        for (String component : components)
        {
            if (component.length() > 1)
            {
                String dir = component.substring(0, 1);
                Direction direction = Direction.deserialize(dir);
                if (direction != null)
                {
                    keyNames.add(component.substring(1));
                    directions.add(direction);
                }
            }
        }
        
        if (keyNames.size() > 0)
            return new SortOrder(keyNames.toArray(new String[0]), directions.toArray(new Direction[0]));
        else
            return null;
    }
    
    private String[] m_keyNames;
    private Direction[] m_directions;
    
    public SortOrder(String[] keyNames, Direction[] directions)
    {
        m_keyNames = keyNames;
        m_directions = directions;
    }
    
    public SortOrder(String keyName, Direction direction)
    {
        m_keyNames = new String[]{keyName};
        m_directions = new Direction[]{direction};
    }
    
    public String getKeyName(int index)
    {
        if (index < 0 || index >= m_keyNames.length)
            return null;
        else
            return m_keyNames[index];
    }
    
    public Direction getDirection(int index)
    {
        if (index < 0 || index >= m_directions.length)
            return null;
        else
            return m_directions[index];
    }
    
    public SortOrder invert()
    {
        Direction[] newDirections = new Direction[m_directions.length];
        for (int i = 0; i < m_directions.length; i++)
        {
            newDirections[i] = m_directions[i].invert();
        }
        return new SortOrder(m_keyNames, newDirections);
    }
    
    public String toSql(Transform transform)
    {
        StringBuffer buf = new StringBuffer();
        boolean first = true;
        for (int i = 0; i < m_keyNames.length; i++)
        {
            String keyName = transform.transform(m_keyNames[i]);
            Direction direction = m_directions[i];
            
            if (keyName != null)
            {
                if (!first)
                    buf.append(", ");
                buf.append(keyName);
                buf.append(" ");
                buf.append(direction.getSql());

                first = false;
            }
        }
        if (first)
            return null;
        else
            return buf.toString();
    }
    
    public String serialize()
    {
        StringBuffer buf = new StringBuffer();
        boolean first = true;
        for (int i = 0; i < m_keyNames.length; i++)
        {
            String keyName = m_keyNames[i];
            Direction direction = m_directions[i];

            if (!first)
                buf.append(",");
            buf.append(direction.serialize());
            buf.append(keyName);

            first = false;
        }
        if (first)
            return null;
        else
            return buf.toString();        
    }
}
