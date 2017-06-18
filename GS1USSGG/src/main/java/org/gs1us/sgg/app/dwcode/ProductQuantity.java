package org.gs1us.sgg.app.dwcode;

import java.math.BigDecimal;

public class ProductQuantity
{
    private BigDecimal m_value;
    private String m_unit;
    public BigDecimal getValue()
    {
        return m_value;
    }
    public void setValue(BigDecimal value)
    {
        m_value = value;
    }
    public String getUnit()
    {
        return m_unit;
    }
    public void setUnit(String unit)
    {
        m_unit = unit;
    }
    
    
}
