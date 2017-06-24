package org.gs1us.sgg.app;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.gs1us.sgg.commerce.OrderLineItemImpl;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

public class Taxer
{
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private String m_taxPercentAttributeName;
    private String m_taxJurisdictionAttributeName;
    
    
    public Taxer(String taxPercentAttributeName,
            String taxJurisdictionAttributeName)
    {
        super();
        m_taxPercentAttributeName = taxPercentAttributeName;
        m_taxJurisdictionAttributeName = taxJurisdictionAttributeName;
    }


    public List<OrderLineItem> computeTax(GBAccount account, OrderLineItem item)
    {
        BigDecimal itemValue = item.getTotal().getValue();
        String taxPercentString = account.getAttributes() == null ? null : account.getAttributes().getAttribute(m_taxPercentAttributeName);
        if (taxPercentString != null && itemValue.compareTo(BigDecimal.ZERO) != 0)
        {
            String taxJurisdiction = account.getAttributes() == null ? null : account.getAttributes().getAttribute(m_taxJurisdictionAttributeName);
            try
            {
                BigDecimal taxPercent = new BigDecimal(taxPercentString);
                BigDecimal taxAmount = itemValue.multiply(taxPercent).divide(ONE_HUNDRED);
                OrderLineItem lineItem = 
                        new OrderLineItemImpl(1, 
                                              "@tax_" + taxJurisdiction, 
                                              taxJurisdiction + " sales tax",
                                              new String[]{taxJurisdiction}, 
                                              new Amount(taxAmount, item.getTotal().getCurrency()));
                return Collections.singletonList(lineItem);
            }
            catch (NumberFormatException e)
            {
                
            }
        }
        return null;
    }
}
