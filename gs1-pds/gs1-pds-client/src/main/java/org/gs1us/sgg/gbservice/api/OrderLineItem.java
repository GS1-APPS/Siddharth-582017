package org.gs1us.sgg.gbservice.api;

public interface OrderLineItem
{
    public int getQuantity();
    public String getItemCode();
    public String getItemDescription();
    public String[] getItemParameters();
    public Amount getPrice();
    public Amount getTotal();

}
