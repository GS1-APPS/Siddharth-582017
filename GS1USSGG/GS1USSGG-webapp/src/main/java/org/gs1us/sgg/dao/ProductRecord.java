package org.gs1us.sgg.dao;

import java.util.Date;
import java.util.Map;

import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;

// TODO: Separate product from its attributes
public abstract class ProductRecord implements Product
{
    public abstract String getId();
    public abstract void setId(String id);
    
    public abstract String getGBAccountGln();
    public abstract void setGBAccountGln(String gln);
    
    @Override
    public abstract String getGtin();
    public abstract void setGtin(String gtin);
    
    @Override
    public abstract String getDataAccuracyAckUser();
    public abstract void setDataAccuracyAckUser(String dataAccuracyAckUser);

    @Override
    public abstract AttributeSet getAttributes();
    public abstract void setAttributes(AttributeSet attributes);

    @Override
    public abstract Date getRegisteredDate();
    public abstract void setRegisteredDate(Date registeredDate);

    @Override
    public abstract Date getModifiedDate();
    public abstract void setModifiedDate(Date modifiedDate);

    @Override
    public abstract Date getNextActionDate();
    public abstract void setNextActionDate(Date nextActionDate);

    @Override
    public abstract Date getPendingNextActionDate();
    public abstract void setPendingNextActionDate(Date nextActionDate);

    @Override
    public abstract Integer getTargetCountryCode();
    public abstract void setTargetCountryCode(Integer targetCountryCode);

    @Override
    public abstract String getGpcCategoryCode();
    public abstract void setGpcCategoryCode(String gpcCategoryCode);
        
    public abstract String[] getPendingOrderIds();
    public abstract void addPendingOrderId(String invoiceId);
    public abstract void removePendingOrderId(String invoiceId);
}
