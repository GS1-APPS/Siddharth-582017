package org.gs1us.sgg.gbservice.api;

public enum ProductState
{
    /**
     * Indicates the GTIN or product data in the product transaction is invalid.
     */
    INVALID,
    
    /**
     * Indicates that the GTIN and product data are valid but that payments are needed to complete the transaction. 
     */
    AWAITING_PAYMENT,
    
    /**
     * Indicates that the GTIN, product data, and payments are all acceptable, and that the transaction is awaiting fulfillment with
     * the app provider(s).
     */
    AWAITING_FULFILLMENT,
    
    /**
     * Indicates that the transaction is complete and reflected in the current product data.
     */
    COMPLETED
}
