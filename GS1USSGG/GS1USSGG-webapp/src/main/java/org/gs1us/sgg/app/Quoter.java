package org.gs1us.sgg.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gs1us.sgg.commerce.OrderableItemDesc;
import org.gs1us.sgg.commerce.QuotationImpl;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppDesc.Scope;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.ModuleDesc;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.Quotation;
import org.gs1us.sgg.util.Util;
import org.gs1us.sgg.validation.ProductValidationErrorImpl;


class Quoter
{
    private Map<String, List<Taxer>> m_taxers = new HashMap<>();
    
    public void addTaxer(String lineItemCode, Taxer taxer)
    {
        List<Taxer> taxers = m_taxers.get(lineItemCode);
        if (taxers == null)
        {
            taxers = new ArrayList<>();
            m_taxers.put(lineItemCode, taxers);
        }
        taxers.add(taxer);
    }
    
    /*
    public Quotation validateTerms(Product newProduct, Product oldProduct, boolean renew, List<ProductValidationError> validationErrors, Date now, Collection<? extends AppDescImpl> subs) throws GlobalBrokerException
    {
        Date today = DateUtil.roundToDay(now);
        validateTermsForNewProduct(subs, newProduct, validationErrors, today);
        if (validationErrors.size() > 0)
            return null;
        
        if (oldProduct != null)
        {
            validateTermsForUpdatedProduct(subs, newProduct, oldProduct, validationErrors, today);
            if (validationErrors.size() > 0)
                return null;
        }
        
        return createQuotation(subs, newProduct, oldProduct, renew, today);
    }
    */

    public Quotation validateTerms(GBAppContext appContext, Product newProduct, Product oldProduct, boolean renew, List<ProductValidationError> validationErrors, Date now) throws GlobalBrokerException
    {
        AttributeSet newProductAttributes = newProduct.getAttributes();
        AttributeSet oldProductAttributes = oldProduct == null ? null : oldProduct.getAttributes();
        AppDesc.Scope scope = AppDesc.Scope.PRODUCT;
        String lineItemParameter = newProduct.getGtin();
        
        return validateTermsForModules(appContext, scope, lineItemParameter, newProductAttributes, oldProductAttributes, renew, validationErrors, now);
    }
    
    private Quotation validateTermsForModules(GBAppContext appContext, AppDesc.Scope scope, String lineItemParameter, AttributeSet newProductAttributes, AttributeSet oldProductAttributes, boolean renew, List<ProductValidationError> validationErrors, Date now) throws GlobalBrokerException
    {
        Date today = DateUtil.roundToDay(now);
        Date oldStartDate = oldProductAttributes == null ? null : oldProductAttributes.getDateAttribute(appContext.getSubs().iterator().next().getAppDesc().getModuleDesc(scope).getStartDateAttribute());
        Date defaultStartDate = oldStartDate == null ? today : oldStartDate;
        slideBackBasicStart(appContext, scope, newProductAttributes, oldStartDate, today);
        validateTermsForNewModules(appContext, scope, newProductAttributes, defaultStartDate, today, validationErrors);
        if (validationErrors.size() > 0)
            return null;
        
        if (oldProductAttributes != null)
        {
            validateTermsForUpdatedModules(appContext, scope, newProductAttributes, oldProductAttributes, today, validationErrors);
            if (validationErrors.size() > 0)
                return null;
        }
        
        return createQuotationForModules(appContext, scope, lineItemParameter, newProductAttributes, oldProductAttributes, renew, today);
    }

    private void slideBackBasicStart(GBAppContext appContext, Scope scope,
            AttributeSet newAttributes, Date oldStartDate, Date today)
    {
        // The first subscription is taken to be the basic module
        boolean isBasicSub = true;
        
        Date basicStart = null;
        ModuleDesc basicModuleDesc = null;
        for (AppSubscription sub : appContext.getSubs())
        {
            AppDesc appDesc = sub.getAppDesc();
            ModuleDesc moduleDesc = appDesc.getModuleDesc(scope);
            if (isBasicSub)
            {
                basicModuleDesc = moduleDesc;
                basicStart = moduleDesc.getStartDate(newAttributes);
            }
            else 
            {
                //if (basicStart != null)
                {
                    Date moduleStart = moduleDesc.getStartDate(newAttributes);
                    if (moduleStart != null && (basicStart == null || moduleStart.before(basicStart)) && !(oldStartDate != null && oldStartDate.before(today)))
                    {
                        basicStart = moduleStart;
                        basicModuleDesc.setStartDate(newAttributes, basicStart);
                    }
                }
            }
            
            // The first subscription is taken to be the product itself
            isBasicSub = false;
        }
    }

    private void validateTermsForNewModules(GBAppContext appContext,
            AppDesc.Scope scope, AttributeSet newAttributes, Date defaultStartDate, Date today,
            List<ProductValidationError> validationErrors)
    {
        // The first subscription is taken to be the basic module
        boolean isBasicSub = true;
        
        Date basicStart = null;
        Date basicEnd = null;
        for (AppSubscription sub : appContext.getSubs())
        {
            AppDesc appDesc = sub.getAppDesc();
            ModuleDesc moduleDesc = (ModuleDesc)appDesc.getModuleDesc(scope);
            String title = appDesc.getTitle();

            if (isBasicSub)
            {
                validateTermsForNewModuleBasic(moduleDesc, title, newAttributes, validationErrors, defaultStartDate, today);
                basicStart = moduleDesc.getStartDate(newAttributes);
                basicEnd   = moduleDesc.getEndDate(newAttributes);
            }
            else 
            {
                validateTermsForNewModuleOther(moduleDesc, title,
                                               newAttributes,
                                               basicStart, basicEnd, today,
                                               validationErrors);
            }
            
            // The first subscription is taken to be the product itself
            isBasicSub = false;
        }
    }

    private void validateTermsForNewModuleBasic(ModuleDesc productModuleDesc, String title, AttributeSet newProductAttributes, List<ProductValidationError> validationErrors,
            Date defaultStartDate, Date today)
    {
        Date productStart = productModuleDesc.getStartDate(newProductAttributes);
        Date productEnd = productModuleDesc.getEndDate(newProductAttributes);
        
        // Product start defaults to today
        if (productStart == null)
            productStart = defaultStartDate;
        
        // Product end must be omitted or after the start
        if (productEnd != null && !productEnd.after(productStart))
            validationErrors.add(new ProductValidationErrorImpl(productModuleDesc.getEndDatePath(), "Termination date must be later than the launch date."));
        
        // Product end must be omitted or in the future (if not, it means we're updating a product that has already ended)
        if (productEnd != null && !productEnd.after(today))
            validationErrors.add(new ProductValidationErrorImpl(productModuleDesc.getEndDatePath(), "Termination date must be later than today."));
        
        productModuleDesc.setStartDate(newProductAttributes, productStart);
        productModuleDesc.setEndDate(newProductAttributes, productEnd);
    }

    private void validateTermsForNewModuleOther(
            ModuleDesc moduleDesc, String title,
            AttributeSet attributes, Date basicStart,
            Date basicEnd, Date today, List<ProductValidationError> validationErrors)
    {
        if (moduleDesc.isSelected(attributes))
        {
            Date moduleStart = moduleDesc.getStartDate(attributes);
            Date moduleEnd   = moduleDesc.getEndDate(attributes);

            // App start defaults to product start or today, whichever is later
            if (moduleStart == null)
            {
                if (basicStart.before(today))
                    moduleStart = today;
                else
                    moduleStart = basicStart;
            }

            // App end defaults to product end (which could be indefinite)
            if (moduleEnd == null)
                moduleEnd = basicEnd;  // which could be null

            // App start must be >= product start
            if (moduleStart.before(basicStart))
                validationErrors.add(new ProductValidationErrorImpl(moduleDesc.getStartDatePath(), "Launch date for " + title + " must not be earlier than the product launch date"));

            // App end must be <= product end
            if (basicEnd != null && !basicEnd.after(moduleStart))
                validationErrors.add(new ProductValidationErrorImpl(moduleDesc.getEndDatePath(), "Termination date for " + title + " must not be later than the product termination date"));

            // App end must fall within product start,end
            if (moduleEnd != null && !moduleEnd.after(moduleStart))
                validationErrors.add(new ProductValidationErrorImpl(moduleDesc.getEndDatePath(), "Termination date for " + title + " must not be earlier than the launch date for " + title));
            if (moduleEnd != null && basicEnd != null && moduleEnd.after(basicEnd))
                validationErrors.add(new ProductValidationErrorImpl(moduleDesc.getEndDatePath(), "Termination date for " + title + " must not be later than the product termination date"));

            moduleDesc.setStartDate(attributes, moduleStart);
            moduleDesc.setEndDate(attributes, moduleEnd);
        }
    }
    
    

    // Renew for a period
    // newPaidThru = max(oldpaidThru + 1 period, oldTermination)
    // bill for newPaidThru - oldPaidThru
    
    
    private void validateTermsForUpdatedModules(GBAppContext appContext,
            AppDesc.Scope scope, AttributeSet newAttributes,
            AttributeSet oldAttributes, Date today,
            List<ProductValidationError> validationErrors)
    {
        for (AppSubscription sub : appContext.getSubs())
        {
            AppDesc appDesc = sub.getAppDesc();
            ModuleDesc productModuleDesc = (ModuleDesc)appDesc.getModuleDesc(scope);
            String title = appDesc.getTitle();

            validateTermsForUpdatedModule(productModuleDesc, title,
                                          oldAttributes,
                                          newAttributes, today,
                                          validationErrors);
        }
    }

    private void validateTermsForUpdatedModule(
            ModuleDesc productModuleDesc, String title,
            AttributeSet oldAttributes,
            AttributeSet newAttributes, Date today,
            List<ProductValidationError> validationErrors)
    {
        if (productModuleDesc.isSelected(newAttributes) && productModuleDesc.isSelected(oldAttributes))
        {
            Date oldAppStartDate = productModuleDesc.getStartDate(oldAttributes);
            Date newAppStartDate = productModuleDesc.getStartDate(newAttributes);
   
            // Can't update start dates after launch
            if (oldAppStartDate!=null && oldAppStartDate.before(today))
            {
                if (newAppStartDate!=null && !newAppStartDate.equals(oldAppStartDate))
                {
                    // Disable this check so that dmStartDate can be changed after launch
                    //validationErrors.add(new ProductValidationErrorImpl(productModuleDesc.getStartDatePath(), "Cannot change launch date for " + title + " after launch."));
                }
            }
        }
    }

    private Quotation createQuotationForModules(GBAppContext appContext,
            AppDesc.Scope scope, String lineItemParameter,
            AttributeSet newProductAttributes,
            AttributeSet oldProductAttributes, boolean renew, Date today)
    {
        List<OrderLineItem> lineItems = new ArrayList<>();

        for (AppSubscription sub : appContext.getSubs())
        {
            AppDesc appDesc = sub.getAppDesc();
            ModuleDescImpl moduleDesc = (ModuleDescImpl)appDesc.getModuleDesc(scope);
            OrderableItemDesc annualItemDesc = moduleDesc.getPricer().getAnnualItemDesc(appContext.getGbAccount());
            if (annualItemDesc != null)
            {
                if (moduleDesc.isSelected(newProductAttributes) && !moduleDesc.isSelected(oldProductAttributes))
                {
                    // Create
                    // bill for a period, paidthru = max(today, start) + period
                    // Date startDate = moduleDesc.getStartDate(newProductAttributes);
                    // Date paidThruStart = DateUtil.dateMax(today, startDate);
                    Date paidThruStart = today;
                    Date paidThru = DateUtil.oneYearLater(paidThruStart);
                    //String description = String.format("%s for GTIN %s", appDesc.getTitle(), gtin);
                    lineItems.add(annualItemDesc.createLineItem(1, lineItemParameter, Util.DATE_FORMAT.format(paidThru)));
                    Date oldPaidThru = oldProductAttributes == null ? null : moduleDesc.getPaidThruDate(oldProductAttributes);
                    moduleDesc.setPaidThruDate(newProductAttributes, oldPaidThru);
                    moduleDesc.setPendingPaidThruDate(newProductAttributes, paidThru);
                }
                else if (moduleDesc.isSelected(newProductAttributes) && moduleDesc.isSelected(oldProductAttributes))
                {
                    Date oldPaidThru = moduleDesc.getPaidThruDate(oldProductAttributes);
                    moduleDesc.setPaidThruDate(newProductAttributes, oldPaidThru);
                    moduleDesc.setPendingPaidThruDate(newProductAttributes, moduleDesc.getPendingPaidThruDate(oldProductAttributes));
                    if (renew)
                    {
                        if (oldPaidThru != null)
                        {
                            Date newPaidThru = DateUtil.oneYearLater(oldPaidThru);
                            lineItems.add(annualItemDesc.createLineItem(1, lineItemParameter, Util.DATE_FORMAT.format(newPaidThru)));
                            moduleDesc.setPendingPaidThruDate(newProductAttributes, newPaidThru);
                        }
                    }
                }
                else if (!moduleDesc.isSelected(newProductAttributes) 
                        && moduleDesc.isSelected(oldProductAttributes) 
                        && moduleDesc.getPricer().getProRateItemDesc(appContext.getGbAccount()) != null)
                {
                    // Delete
                    // refund paidthru - max(today, oldstart)
                    Date oldStartDate = moduleDesc.getStartDate(oldProductAttributes);
                    Date terminationDate = DateUtil.dateMax(today, oldStartDate);
                    Date appPaidThruDate = moduleDesc.getPaidThruDate(oldProductAttributes);
                    BigDecimal proRataFactor = new BigDecimal(DateUtil.proRataFactor(appPaidThruDate, terminationDate)).setScale(2, RoundingMode.HALF_EVEN);
                    Amount refund = annualItemDesc.getDefaultPrice().multiplyBy(proRataFactor);
                    // String description = String.format("ProRate %s for GTIN %s", appDesc.getTitle(), gtin);
                    if (!refund.isZero())
                    {
                        OrderableItemDesc proRateItemDesc = moduleDesc.getPricer().getProRateItemDesc(appContext.getGbAccount());
                        lineItems.add(proRateItemDesc.createLineItem(1, refund.negate(), lineItemParameter, Util.DATE_FORMAT.format(terminationDate)));
                    }
                }
            }
        }
        
        // Now, figure out an appropriate paid thru date for non-paid apps. 
        // This is equal to the earliest of the paid apps, using pending paidthru where appropriate,
        // or one year from now if no paid apps.
        // TODO: what if a pending paidthru is cancelled?
        
        Date nonPaidPaidThru = null;
        for (AppSubscription sub : appContext.getSubs())
        {
            AppDesc appDesc = sub.getAppDesc();
            ModuleDescImpl moduleDesc = (ModuleDescImpl)appDesc.getModuleDesc(scope);
            OrderableItemDesc annualItemDesc = moduleDesc.getPricer().getAnnualItemDesc(appContext.getGbAccount());
            if (annualItemDesc != null && moduleDesc.isSelected(newProductAttributes))
            {
                Date pendingPaidThru = moduleDesc.getPendingPaidThruDate(newProductAttributes);
                Date paidThru = moduleDesc.getPaidThruDate(newProductAttributes);
                
                if (pendingPaidThru != null)
                    nonPaidPaidThru = DateUtil.dateMin(nonPaidPaidThru, pendingPaidThru, false);
                else if (paidThru != null)
                    nonPaidPaidThru = DateUtil.dateMin(nonPaidPaidThru, paidThru, false);
            }
        }
        
        if (nonPaidPaidThru == null)
            nonPaidPaidThru = DateUtil.oneYearLater(today);
        
        for (AppSubscription sub : appContext.getSubs())
        {
            AppDesc appDesc = sub.getAppDesc();
            ModuleDescImpl moduleDesc = (ModuleDescImpl)appDesc.getModuleDesc(scope);
            OrderableItemDesc annualItemDesc = moduleDesc.getPricer().getAnnualItemDesc(appContext.getGbAccount());
            if (annualItemDesc == null && moduleDesc.isSelected(newProductAttributes))
            {
                moduleDesc.setPaidThruDate(newProductAttributes, nonPaidPaidThru);
            }
        }
        
        int lineItemCount = lineItems.size();
        for (int i = 0; i < lineItemCount; i++)
        {
            OrderLineItem lineItem = lineItems.get(i);
            List<Taxer> taxers = m_taxers.get(lineItem.getItemCode());
            if (taxers != null)
            {
                for (Taxer taxer : taxers)
                {
                    List<OrderLineItem> taxLineItems = taxer.computeTax(appContext.getGbAccount(), lineItem);
                    if (taxLineItems != null)
                        lineItems.addAll(taxLineItems);
                }
            }
        }
        
        if (lineItems.size() > 0)
            return new QuotationImpl(lineItems);
        else
            return null;
    }
}