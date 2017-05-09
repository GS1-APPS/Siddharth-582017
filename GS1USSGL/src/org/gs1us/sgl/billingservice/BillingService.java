package org.gs1us.sgl.billingservice;

import org.gs1us.sgg.gbservice.api.Quotation;
import org.gs1us.sgl.memberservice.Member;

public interface BillingService
{
    /*
    Collection<? extends Order> getAllOrders();
    Collection<? extends Order> getOrders(BillingStatus status);
    Collection<? extends Order> getOrders(String gln, BillingStatus status);
    */
    Order requisition(Member member, Quotation quotation);
    /*
    void invoice(GBAccount gbAccount, String orderId, String invoiceId); // assumes invoice matches the PO
    void enterPayment(String orderId, String paymentId);
    */
}
