package org.gs1us.sgg.dao.mock;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.dao.SalesOrderRecord;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

class MockSalesOrderRecord extends SalesOrderRecord implements DaoElement<MockSalesOrderRecord>, HasGln
{
    private String m_id;
    private Date m_date;
    private String m_orderId;
    private String m_gbAccountGln;
    private String m_poId;
    private Collection<? extends OrderLineItem> m_lineItems;
    private String m_summary;
    private Amount m_total;
    private Amount m_balanceDue;
    private String m_invoiceId;
    
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
    }
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public String getOrderId()
    {
        return m_orderId;
    }
    public void setOrderId(String orderId)
    {
        m_orderId = orderId;
    }
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public String getPOId()
    {
        return m_poId;
    }
    public void setPOId(String poId)
    {
        m_poId = poId;
    }

    public Collection<? extends OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }
    public void setLineItems(Collection<? extends OrderLineItem> lineItems)
    {
        m_lineItems = lineItems;
    }
    
    public String getSummary()
    {
        return m_summary;
    }
    public void setSummary(String summary)
    {
        m_summary = summary;
    }
    public Amount getTotal()
    {
        return m_total;
    }
    public void setTotal(Amount total)
    {
        m_total = total;
    }
    public Amount getBalanceDue()
    {
        return m_balanceDue;
    }
    public void setBalanceDue(Amount balanceDue)
    {
        m_balanceDue = balanceDue;
    }
    
    public String getInvoiceId()
    {
        return m_invoiceId;
    }
    public void setInvoiceId(String invoiceId)
    {
        m_invoiceId = invoiceId;
    }
    @Override
    public MockSalesOrderRecord newInstance()
    {
        return new MockSalesOrderRecord();
    }
    @Override
    public MockSalesOrderRecord updateFrom(MockSalesOrderRecord from)
    {
        setId(from.getId());
        setDate(from.getDate());
        setOrderId(from.getOrderId());
        setGBAccountGln(from.getGBAccountGln());
        setPOId(from.getPOId());
        setLineItems(from.getLineItems()); // not really right
        setSummary(from.getSummary());
        setTotal(from.getTotal());
        setBalanceDue(from.getBalanceDue());
        setInvoiceId(from.getInvoiceId());
        return this;
    }
    


}
