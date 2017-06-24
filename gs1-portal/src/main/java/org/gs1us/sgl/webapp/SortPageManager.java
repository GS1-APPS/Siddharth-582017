package org.gs1us.sgl.webapp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriComponentsBuilder;

public class SortPageManager
{
    public static final String ORDER_PARAMETER_NAME = "o";
    
    private SortOrder m_sortOrder;
    private UriComponentsBuilder m_baseBuilder;
    
    public SortPageManager(HttpServletRequest request, UriComponentsBuilder baseBuilder)
    {
        String orderParameter = request.getParameter(ORDER_PARAMETER_NAME);
        
        if (orderParameter != null)
            m_sortOrder = SortOrder.deserialize(orderParameter);
        
        m_baseBuilder = baseBuilder;
    }

    public String getSortKey()
    {
        if (m_sortOrder == null)
            return null;
        else
            return m_sortOrder.getKeyName(0);
    }

    public SortOrder.Direction getSortDirection()
    {
        if (m_sortOrder == null)
            return null;
        else
            return m_sortOrder.getDirection(0);
    }
    
    public int getOffset()
    {
        return -1; // TODO: implement
    }
    
    public int getPageSize()
    {
        return -1; // TODO: implement
    }
    
    public String getColumnLink(String key)
    {
        SortOrder newSortOrder = isSortedBy(key) ? m_sortOrder.invert() : new SortOrder(key, SortOrder.Direction.ASC);
        UriComponentsBuilder newBuilder = m_baseBuilder.replaceQueryParam(ORDER_PARAMETER_NAME, newSortOrder.serialize());
        return newBuilder.toUriString();
    }
    
    public boolean isSortedBy(String key)
    {
        return key.equals(getSortKey()); // Note that getSortKey could return null
    }
    
    public boolean isAscending()
    {
        return SortOrder.Direction.ASC.equals(getSortDirection()); // Note that getSortDirection could return null
    }
    
    public boolean isDescending()
    {
        return SortOrder.Direction.DESC.equals(getSortDirection()); // Note that getSortDirection could return null
      
    }

}
