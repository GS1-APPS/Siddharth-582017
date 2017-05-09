package org.gs1us.sgg.gbservice.api;

public interface InvoiceExtra
{
    public String getItemCode();
    public String getItemDescription();
    public String[] getItemParameters();
    public Amount getTotal();

}
