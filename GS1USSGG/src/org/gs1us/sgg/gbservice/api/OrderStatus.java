package org.gs1us.sgg.gbservice.api;

public enum OrderStatus
{
    /**
     * The order has been entered into the system (generally due to a user action that places an order for something),
     * but no action has yet been taken with respect to any required payments.
     */
    ORDERED,
    
    /**
     * An invoice has been generated for this order. One invoice may cover multiple orders.
     */
    INVOICED,
    
    /**
     * The invoice has been sent to the customer.
     */
    BILLED,
    
    /**
     * The customer has committed a payment; e.g., because a transaction has been executed through a payment system. One payment
     * committment may cover multiple invoices.
     */
    PAYMENT_COMMITTED,
    
    /**
     * The payment has been received by the Global Broker.
     */
    PAID,
    
    /**
     * The order has been settled by forwarding all payments to the consignee(s). One settlement may include multiple payments.
     */
    SETTLED
}
