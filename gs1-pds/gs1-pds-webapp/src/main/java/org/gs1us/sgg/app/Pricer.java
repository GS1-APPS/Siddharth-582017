package org.gs1us.sgg.app;

import org.gs1us.sgg.commerce.OrderableItemDesc;
import org.gs1us.sgg.gbservice.api.GBAccount;

interface Pricer
{
    public OrderableItemDesc getAnnualItemDesc(GBAccount gbAccount);
    public OrderableItemDesc getProRateItemDesc(GBAccount gbAccount);
}
