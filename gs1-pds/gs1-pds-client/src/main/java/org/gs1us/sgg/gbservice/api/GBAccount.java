package org.gs1us.sgg.gbservice.api;

/**
 * A Global Broker account representing a GS1 Member.
 * @author kt
 *
 */
public interface GBAccount extends HasAttributes
{
    public String getGln();
    public String getName();
    public String[] getGcps();
    public AttributeSet getAttributes();
}
