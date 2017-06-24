package org.gs1us.sgg.gbservice.json;

import java.util.List;

import org.gs1us.sgg.gbservice.api.PaymentReceipt;

public class InboundPayInvoicesInfo
{
    private InboundPaymentReceipt m_paymentReceipt;
    private List<String> m_invoiceIds;
    public PaymentReceipt getPaymentReceipt()
    {
        return m_paymentReceipt;
    }
    public void setPaymentReceipt(InboundPaymentReceipt paymentReceipt)
    {
        m_paymentReceipt = paymentReceipt;
    }
    public List<String> getInvoiceIds()
    {
        return m_invoiceIds;
    }
    public void setInvoiceIds(List<String> invoiceIds)
    {
        m_invoiceIds = invoiceIds;
    }
    
}
