package org.gs1us.sgg.commerce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.AuditEventRecord;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.dao.PaymentRecord;
import org.gs1us.sgg.dao.SalesOrderRecord;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AuditEventType;
import org.gs1us.sgg.gbservice.api.BillingTransactionType;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.NoSuchInvoiceException;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;
import org.gs1us.sgg.gbservice.api.PaymentException;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;
import org.gs1us.sgg.gbservice.api.SalesOrder;

public class CommerceManager
{
    @Resource
    private GBDao m_gbDao;
    
    private Map<String, OrderableItemDesc> m_orderableItemDescMap = new HashMap<String, OrderableItemDesc>();
    
    private Map<String, Collection<CommerceEventHandler>> m_eventHandlerMap = new HashMap<>();
    
    public void addOrderableItemDesc(OrderableItemDesc oid)
    {
        m_orderableItemDescMap.put(oid.getItemCode(), oid);
    }
    
    public OrderLineItem createLineItem(String itemCode, int quantity, String... itemParameters)
    {
        return createLineItem(itemCode, quantity, null, itemParameters);
    }
    
    public OrderLineItem createLineItem(String itemCode, int quantity, Amount price, String... itemParameters)
    {
        OrderableItemDesc oid = m_orderableItemDescMap.get(itemCode);
        if (oid == null)
            return null;
        else
            return oid.createLineItem(quantity, price, itemParameters);
    }
    
    public void addEventHandler(String itemCode, CommerceEventHandler handler)
    {
        Collection<CommerceEventHandler> handlers = m_eventHandlerMap.get(itemCode);
        if (handlers == null)
        {
            handlers = new ArrayList<>();
            m_eventHandlerMap.put(itemCode, handlers);
        }
        handlers.add(handler);
       
    }

    // No longer used since we've separated ordering and invoicing
    public SalesOrderRecord createSalesOrderAndInvoice(GBAccount gbAccount, PurchaseOrder po, Date date)
    {
        String invoiceId = m_gbDao.newInvoiceId();

        SalesOrderRecord salesOrder = createSalesOrder(gbAccount, po, date,
                                                       invoiceId);
        
        InvoiceRecord invoiceRecord = m_gbDao.newInvoiceRecord();
        invoiceRecord.setOrderStatus(OrderStatus.INVOICED);
        invoiceRecord.setDate(date);
        invoiceRecord.setDueDate(date);
        invoiceRecord.setGBAccountGln(gbAccount.getGln());
        invoiceRecord.setSummary(lineItemsToSummary(salesOrder.getLineItems()));
        invoiceRecord.setTotal(salesOrder.getTotal());
        invoiceRecord.setBalanceDue(salesOrder.getTotal());
        invoiceRecord.setInvoiceId(invoiceId);
        
        m_gbDao.updateInvoiceRecord(invoiceRecord);
        
        processHandlers(OrderStatus.INVOICED, date, salesOrder, invoiceRecord, null);
        
        return salesOrder;
    }

    public SalesOrderRecord createSalesOrder(AgentUser agentUser, String username, GBAccount gbAccount, PurchaseOrder po, Date date)
    {
        SalesOrderRecord salesOrder = createSalesOrder(gbAccount, po, date, null);
        processHandlers(OrderStatus.ORDERED, date, salesOrder, null, null);
        return salesOrder;        
    }
    
    private SalesOrderRecord createSalesOrder(GBAccount gbAccount, PurchaseOrder po, Date date, String invoiceId)
    {
        SalesOrderRecord salesOrder = m_gbDao.newSalesOrder();
        
        salesOrder.setDate(date);
        salesOrder.setPOId(po.getPOId());
        salesOrder.setGBAccountGln(gbAccount.getGln());
        salesOrder.setLineItems(po.getLineItems());
        salesOrder.setSummary(lineItemsToSummary(po.getLineItems()));
        salesOrder.setOrderId(m_gbDao.newSalesOrderId());
        salesOrder.setInvoiceId(invoiceId);
        
        Amount total = Amount.ZERO;
        for (OrderLineItem lineItem : po.getLineItems())
            total = total.add(lineItem.getTotal());
        salesOrder.setTotal(total);
        salesOrder.setBalanceDue(total);
        
        m_gbDao.updateSalesOrder(salesOrder);
        return salesOrder;
    }
    
    public SalesOrderRecord getSalesOrder(String salesOrderId)
    {
        return m_gbDao.getSalesOrderBySalesOrderId(salesOrderId);
    }
    
    public InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total)
    {
        return m_gbDao.newInvoiceExtra(itemCode, itemDescription, itemParameters, total);
    }


    public InvoiceRecord createInvoice(AgentUser agentUser, String username, GBAccount gbAccount, Date date, String[] salesOrderIds, List<? extends InvoiceExtra> extras) throws GlobalBrokerException
    {
        String invoiceId = m_gbDao.newInvoiceId();

        List<SalesOrderRecord> salesOrders = new ArrayList<>(salesOrderIds.length);
        List<OrderLineItem> lineItems = new ArrayList<>();
        
        Amount grandTotal = Amount.ZERO;
        for (String salesOrderId : salesOrderIds)
        {
            SalesOrderRecord salesOrder = m_gbDao.getSalesOrderBySalesOrderId(salesOrderId);
            if (salesOrder != null)
            {
                if (!salesOrder.getGBAccountGln().equals(gbAccount.getGln()))
                    throw new GlobalBrokerException();
                
                salesOrders.add(salesOrder);
                grandTotal = grandTotal.add(salesOrder.getTotal());
                lineItems.addAll(salesOrder.getLineItems());
            }
        }
        
        for (InvoiceExtra extra : extras)
        {
            grandTotal = grandTotal.add(extra.getTotal());
        }

        InvoiceRecord invoiceRecord = m_gbDao.newInvoiceRecord();
        invoiceRecord.setOrderStatus(OrderStatus.INVOICED);
        invoiceRecord.setDate(date);
        invoiceRecord.setDueDate(date);
        invoiceRecord.setGBAccountGln(gbAccount.getGln());
        invoiceRecord.setSummary(lineItemsToSummary(lineItems)); // TODO: fix up!
        invoiceRecord.setTotal(grandTotal);
        invoiceRecord.setBalanceDue(grandTotal);
        invoiceRecord.setInvoiceId(invoiceId);
        invoiceRecord.setExtras(extras);
        
        m_gbDao.updateInvoiceRecord(invoiceRecord);
        
        for (SalesOrderRecord salesOrder : salesOrders)
        {
            salesOrder.setInvoiceId(invoiceId);
            processHandlers(OrderStatus.INVOICED, date, salesOrder, invoiceRecord, null);
            m_gbDao.updateSalesOrder(salesOrder);
        }
        
        AuditEventRecord event = m_gbDao.newAuditEvent();
        event.setType(AuditEventType.CREATE_INVOICE);
        event.setDate(date);
        event.setAgentUsername(agentUser.getUsername());
        event.setUsername(username);
        event.setGBAccountGln(gbAccount.getGln());
        event.setInvoiceId(invoiceRecord.getInvoiceId());
        m_gbDao.updateAuditEvent(event);
        
        return invoiceRecord;
    }

    
    private String lineItemsToSummary(Collection<? extends OrderLineItem> lineItems)
    {
        StringBuffer buf = new StringBuffer();
        int limit = 3;
        for (OrderLineItem lineItem : lineItems)
        {
            if (!lineItem.getItemCode().startsWith("@"))
            {
                if (limit <= 0)
                {
                    buf.append(", ...");
                    break;
                }
                if (buf.length() > 0)
                    buf.append(", ");
                buf.append(lineItem.getItemDescription());
                limit--;
            }
        }
        return buf.toString();
    }

    public Collection<? extends SalesOrderRecord> getUninvoicedSalesOrderRecords(GBAccount gbAccount)
    {
        if (gbAccount == null)
            return m_gbDao.getUninvoicedSalesOrders();
        else
            return m_gbDao.getUninvoicedSalesOrdersByGln(gbAccount.getGln());
    }

    public Collection<? extends SalesOrder> getInvoiceSalesOrderRecords(
            String invoiceId)
    {
        return m_gbDao.getSalesOrdersByInvoiceId(invoiceId);
    }

    public InvoiceRecord getInvoiceRecordByInvoiceId(String invoiceId)
    {
        return m_gbDao.getInvoiceRecordByInvoiceId(invoiceId);
    }

    public Collection<? extends InvoiceRecord> getAllInvoiceRecords(GBAccount gbAccount, OrderStatus orderStatus)
    {
        if (gbAccount == null)
        {
            if (orderStatus == null)
                return m_gbDao.getAllInvoiceRecords();
            else
                return m_gbDao.getAllInvoiceRecordsByOrderStatus(orderStatus);
        }
        else
        {
            if (orderStatus == null)
                return m_gbDao.getInvoiceRecordsByGln(gbAccount.getGln());
            else
                return m_gbDao.getInvoiceRecordsByGlnAndOrderStatus(gbAccount.getGln(), orderStatus);
        }
    }

    public Collection<OrderLineItem> getConsolidatedLineItems(InvoiceRecord invoiceRecord)
    {
        // TODO: consolidate things like sales tax
        Collection<? extends SalesOrderRecord> salesOrders = m_gbDao.getSalesOrdersByInvoiceId(invoiceRecord.getInvoiceId());
        Collection<OrderLineItem> result = new ArrayList<>();
        for (SalesOrderRecord salesOrder : salesOrders)
        {
            result.addAll(salesOrder.getLineItems());
        }
        return result;
    }

    public Collection<? extends PaymentRecord> getAllPayments(GBAccount gbAccount)
    {
        return m_gbDao.getPaymentsByGln(gbAccount.getGln());
    }
    
    public void setInvoiceBilled(AgentUser agentUser, String username, GBAccount account, String invoiceId, Date date, String billingReference) throws GlobalBrokerException
    {
        InvoiceRecord invoiceRecord = m_gbDao.getInvoiceRecordByInvoiceId(invoiceId);
        if (invoiceRecord == null || !invoiceRecord.getGBAccountGln().equals(account.getGln()))
            throw new NoSuchInvoiceException();
        
        invoiceRecord.setBillingReference(billingReference);
        invoiceRecord.setOrderStatus(OrderStatus.BILLED);
        invoiceRecord.setBilledDate(date);
        m_gbDao.updateInvoiceRecord(invoiceRecord);
        
        for (SalesOrderRecord salesOrder : m_gbDao.getSalesOrdersByInvoiceId(invoiceRecord.getInvoiceId()))
            processHandlers(OrderStatus.BILLED, date, salesOrder, invoiceRecord, null);
        
        AuditEventRecord event = m_gbDao.newAuditEvent();
        event.setType(AuditEventType.BILL_INVOICE);
        event.setDate(date);
        event.setAgentUsername(agentUser.getUsername());
        event.setUsername(username);
        event.setGBAccountGln(account.getGln());
        event.setInvoiceId(invoiceId);
        m_gbDao.updateAuditEvent(event);

    }

    public Payment pay(AgentUser agentUser, String username, GBAccount gbAccount, PaymentReceipt receipt, List<String> invoiceIds, Date date) throws GlobalBrokerException
    {
        List<InvoiceRecord> invoiceRecords = new ArrayList<>(invoiceIds.size());
        
        Amount total = Amount.ZERO;
        for (String invoiceId : invoiceIds)
        {
            InvoiceRecord invoiceRecord = m_gbDao.getInvoiceRecordByInvoiceId(invoiceId);
            if (invoiceRecord == null)
                throw new NoSuchInvoiceException();
            total = total.add(invoiceRecord.getTotal());
            invoiceRecords.add(invoiceRecord);
        }

        if (!receipt.getAmount().equals(total))
            throw new PaymentException("Payment amount does not match invoice total");

        if (invoiceIds.size() == 0)
            return null;

        StringBuffer buf = new StringBuffer();
        buf.append("Paid invoice ");
        boolean paidAny = false;
        for (String invoiceId : invoiceIds)
        {
            {
                if (paidAny)
                    buf.append(", ");
                buf.append(invoiceId);
                paidAny = true;
            }
        }

        String paymentId = m_gbDao.newPaymentId();
        PaymentRecord payment = m_gbDao.newPayment();
        payment.setGBAccountGln(gbAccount.getGln());
        payment.setPaymentId(paymentId);
        payment.setDate(date);
        payment.setDescription(buf.toString());
        payment.setPaymentReceiptDate(receipt.getDate());
        payment.setPaymentReceiptId(receipt.getPaymentId());
        payment.setAmount(total);
        payment.setStatus(OrderStatus.PAYMENT_COMMITTED);
        m_gbDao.updatePayment(payment);

        for (InvoiceRecord invoiceRecord : invoiceRecords)
        {
            invoiceRecord.setBalanceDue(Amount.ZERO);
            invoiceRecord.setOrderStatus(OrderStatus.PAYMENT_COMMITTED);
            invoiceRecord.setPaymentCommittedDate(receipt.getDate());
            invoiceRecord.setPaymentId(paymentId);
            m_gbDao.updateInvoiceRecord(invoiceRecord);
            
            for (SalesOrderRecord salesOrder : m_gbDao.getSalesOrdersByInvoiceId(invoiceRecord.getInvoiceId()))
                processHandlers(OrderStatus.PAYMENT_COMMITTED, date, salesOrder, invoiceRecord, payment);
            
            AuditEventRecord event = m_gbDao.newAuditEvent();
            event.setType(AuditEventType.PAY_INVOICE);
            event.setDate(date);
            event.setAgentUsername(agentUser.getUsername());
            event.setUsername(username);
            event.setGBAccountGln(gbAccount.getGln());
            event.setInvoiceId(invoiceRecord.getInvoiceId());
            m_gbDao.updateAuditEvent(event);
        }
        
        return payment;
    }
    
    public Payment getPaymentRecordByPaymentId(String paymentId)
    {
        return m_gbDao.getPaymentByPaymentId(paymentId);
    }

    public Collection<? extends Payment> getAllPaymentsByStatus(OrderStatus status)
    {
        return m_gbDao.getAllPaymentsByStatus(status);
    }

    public void setPaymentPaid(AgentUser agentUser, String username, GBAccount gbAccount, String paymentId, Date date, String paidReference)
    {
        PaymentRecord payment = m_gbDao.getPaymentByPaymentId(paymentId);
        if (payment == null)
            return;
        
        payment.setStatus(OrderStatus.PAID);
        payment.setPaidReference(paidReference);
        m_gbDao.updatePayment(payment);
        
        for (InvoiceRecord invoiceRecord : m_gbDao.getInvoiceRecordsByPaymentId(payment.getPaymentId()))
        {
            invoiceRecord.setOrderStatus(OrderStatus.PAID);
            invoiceRecord.setPaidDate(date);
            m_gbDao.updateInvoiceRecord(invoiceRecord);
            
            for (SalesOrderRecord salesOrder : m_gbDao.getSalesOrdersByInvoiceId(invoiceRecord.getInvoiceId()))
                processHandlers(OrderStatus.PAID, date, salesOrder, invoiceRecord, payment);
            
            AuditEventRecord event = m_gbDao.newAuditEvent();
            event.setType(AuditEventType.PAY_PAYMENT);
            event.setDate(date);
            event.setAgentUsername(agentUser.getUsername());
            event.setUsername(username);
            event.setGBAccountGln(gbAccount.getGln());
            event.setInvoiceId(invoiceRecord.getInvoiceId());
            m_gbDao.updateAuditEvent(event);
        }
    }
    

    private void processHandlers(OrderStatus status, Date date, SalesOrderRecord salesOrder, InvoiceRecord invoiceRecord, PaymentRecord payment)
    {
        for (OrderLineItem lineItem : salesOrder.getLineItems())
        {
            Collection<CommerceEventHandler> handlers = m_eventHandlerMap.get(lineItem.getItemCode());
            if (handlers != null)
            {
                for (CommerceEventHandler handler : handlers)
                    handler.handle(status, date, lineItem, salesOrder, invoiceRecord, payment);
            }
        }
    }

}

