package org.gs1us.sgg.gbservice.api;

import java.util.Date;
import java.util.Map;

/**
 * A Product as registered with the Global Broker and its associated data and application associations.
 * @author kt
 *
 */
public interface Product extends HasAttributes
{
    public String getGtin();
    public String getDataAccuracyAckUser();
    public void setDataAccuracyAckUser(String dataAccuracyAckUser);
    public AttributeSet getAttributes();
    public Date getRegisteredDate();
    public Date getModifiedDate();
    public Date getNextActionDate();
    public Date getPendingNextActionDate();
    public String[] getPendingOrderIds();
    public Integer getTargetCountryCode();
    public String getGpcCategoryCode();
}
