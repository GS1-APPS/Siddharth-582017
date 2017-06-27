package org.gs1us.sgl.billingservice.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.Quotation;
import org.gs1us.sgl.billingservice.BillingService;
import org.gs1us.sgl.billingservice.Order;
import org.gs1us.sgl.billingservice.BillingStatus;
import org.gs1us.sgl.memberservice.Member;

public class MockBillingService implements BillingService
{
    @Resource
    private ClockService m_clockService;
/*    
    private Map<String, MockOrder> m_orderMap = new HashMap<String, MockOrder>();
    
    private int m_nextOrderNumber = 101;
    
    private synchronized String newOrderId()
    {
        String result = String.valueOf(m_nextOrderNumber);
        m_nextOrderNumber++;
        return result;
    }
    
    @Override
    public Collection<? extends Order> getAllOrders()
    {
        return m_orderMap.values();
    }



    @Override
    public Collection<? extends Order> getOrders(BillingStatus status)
    {
        Collection<MockOrder> result = new ArrayList<>();
        for (MockOrder order : m_orderMap.values())
        {
            if (order.getStatus().equals(status))
                result.add(order);
        }
        return result;
    }

    @Override
    public Collection<? extends Order> getOrders(String gln, BillingStatus status)
    {
        Collection<MockOrder> result = new ArrayList<>();
        for (MockOrder order : m_orderMap.values())
        {
            if (order.getMemberGln().equals(gln) && order.getStatus().equals(status))
                result.add(order);
        }
        return result;
    }

*/

    @Override
    public Order requisition(Member member, Quotation quotation)
    {
        Amount total = Amount.ZERO;
        Collection<OrderLineItem> lineItems = new ArrayList<>();
        for (OrderLineItem lineItem : quotation.getLineItems())
        {
            total = total.add(lineItem.getTotal());
            lineItems.add(lineItem);
        }
        Amount totalAfterTax = total;
        
        
        Date requisitionDate = m_clockService.now();
        MockOrder order = new MockOrder();
        //order.setId(newOrderId());
        order.setMemberGln(member.getGln());
        order.setStatus(BillingStatus.REQUISITIONED);
        order.setLineItems(lineItems);
        order.setTotal(totalAfterTax);
        order.setQuotationId(null);
        order.setRequisitionDate(requisitionDate);
        
        //m_orderMap.put(order.getId(), order);
        
        return order;
        
    }

    private Amount addTax(Amount total, Amount totalAfterTax, Collection<OrderLineItem> lineItems, BigDecimal rate, String description)
    {
        if (rate == null || rate.equals(BigDecimal.ZERO))
            return totalAfterTax;
        else
        {
            Amount tax = total.multiplyBy(rate);
            lineItems.add(new MockOrderLineItem(1, "@tax", description, null, tax, tax));
            return totalAfterTax.add(tax);
        }
    }
/*
    @Override
    public void invoice(GBAccount gbAccount, String orderId, String invoiceId)
    {
        MockOrder order = m_orderMap.get(orderId);
        if (order != null)
        {
            order.setInvoiceDate(m_clockService.now());
            order.setInvoiceId(invoiceId);
            order.setStatus(BillingStatus.INVOICED);
        }
        // TODO errors?

    }

    @Override
    public void enterPayment(String orderId, String paymentId)
    {
        MockOrder order = m_orderMap.get(orderId);
        if (order != null)
        {
            order.setPaymentDate(m_clockService.now());
            order.setPaymentId(paymentId);
            order.setStatus(BillingStatus.PAID);
        }
        // TODO errors?
       
    }
    */


}
