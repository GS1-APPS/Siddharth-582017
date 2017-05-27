package org.gs1us.sgg.gbservice.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;

import org.gs1us.sgg.app.AppManager;
import org.gs1us.sgg.app.GBAppContext;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.commerce.CommerceManager;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.dao.SalesOrderRecord;
import org.gs1us.sgg.dao.jpa.JpaProductRecord;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AttributeSchema;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ProductState;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;
import org.gs1us.sgg.gbservice.api.Quotation;
import org.gs1us.sgg.gbservice.api.UploadValidationProduct;
import org.gs1us.sgg.gbservice.json.InboundProductAttribute;
import org.gs1us.sgg.gbservice.json.InboundUploadValidationProduct;
import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgg.validation.ProductValidationErrorImpl;
import org.gs1us.sgg.validation.Validator;
import org.springframework.transaction.annotation.Propagation;

public class ProductOpsManager
{
    @Resource
    private ClockService m_clockService;

    @Resource
    private ProductManager m_productManager;

    @Resource
    private AppManager m_appManager;

    @Resource
    private CommerceManager m_commerceManager;

    @org.springframework.transaction.annotation.Transactional(propagation=Propagation.REQUIRES_NEW)
    public ProductStatus validateProductInTransaction(GBAppContext appContext, Product unpaddedProduct, boolean renew)
            throws GlobalBrokerException
    {
        return validateProduct(appContext, unpaddedProduct, renew);
    }

    public ProductStatus validateProduct(GBAppContext appContext, Product unpaddedNewProduct, boolean renew)
            throws GlobalBrokerException
    {
        PaddedGtinProduct newProduct = new PaddedGtinProduct(unpaddedNewProduct);
        Product oldProduct = getProductByGtin(appContext.getGbAccount(), newProduct.getGtin());
        Action action = (renew ? 
                                Action.RENEW : 
                                    oldProduct == null ?
                                                        Action.CREATE :
                                                            Action.UPDATE);
        nullsToOldValues(oldProduct, newProduct);
        return validateProductInternal(appContext, oldProduct, newProduct, action);
    }

    private ProductStatus validateProductInternal(GBAppContext appContext, Product oldProduct, PaddedGtinProduct newProduct, Action action) throws GlobalBrokerException
    {
        AttributeSchema attributeSchema = appContext.getProductAttributeSchema();
        String[] gcps = ((GBAccountRecord)appContext.getGbAccount()).getGcps();
        
        List<ProductValidationError> validationErrors = new ArrayList<>();
        
        Validator validator = new Validator();
        validator.validateProductData(attributeSchema, gcps, newProduct.getUnpaddedProduct(), action, validationErrors);
        if (validationErrors.size() == 0)
        {
            Quotation quotation = m_appManager.validateTerms(appContext, newProduct, oldProduct, action == Action.RENEW, validationErrors);

            if (validationErrors.size() > 0)
                return new ProductStatusImpl(ProductState.INVALID, validationErrors, null);
            else if (quotation == null)
                return new ProductStatusImpl(ProductState.AWAITING_FULFILLMENT, null, null);
            else
                return new ProductStatusImpl(ProductState.AWAITING_PAYMENT, null, quotation);
        }
        else
            return new ProductStatusImpl(ProductState.INVALID, validationErrors, null);
    }
    
    public ProductStatus createProduct(final GBAppContext appContext,
            final PurchaseOrder po, Product unpaddedProduct)
                    throws GlobalBrokerException
    {
        PaddedGtinProduct product = new PaddedGtinProduct(unpaddedProduct);

        nullsToOldValues(null, product);
        ProductStatus status = validateProductInternal(appContext, null, product, Action.CREATE);
        //printProduct(product, "create");
        if (status.getState() == ProductState.INVALID)
            return status;
        else if (getProductByGtin(appContext.getGbAccount(), product.getGtin()) != null)
        {
            return new ProductStatusImpl(ProductState.INVALID, singletonError("gtin", "This GTIN already exists"), null);
        }
        else if (!reconcilePOToQuotation(po, status.getQuotation()))
            return new ProductStatusImpl(ProductState.AWAITING_PAYMENT, null, status.getQuotation());
        else
        {
            // Important to do the commerce *after* the product is created, so that commerce handlers can operate on the product
            m_productManager.createProduct(appContext.getAgentUser(), appContext.getUsername(), appContext.getGbAccount(), product, new ProductManager.Callback(){
                public void callback(ProductRecord productRecord)
                {
                    String orderId = createSalesOrder(appContext, po);
                }
            });
            return new ProductStatusImpl(ProductState.COMPLETED);
        }
    }
    
    public ProductStatus updateProduct(final GBAppContext appContext, Product unpaddedProduct, boolean renew, final PurchaseOrder po)
                    throws GlobalBrokerException
    {
        PaddedGtinProduct product = new PaddedGtinProduct(unpaddedProduct);
        Product oldProduct = getProductByGtin(appContext.getGbAccount(), product.getGtin());
        nullsToOldValues(oldProduct, product);
        ProductStatus status = validateProductInternal(appContext, oldProduct, product, renew ? Action.RENEW : Action.UPDATE);
        //printProduct(product, "update");
        if (status.getState() == ProductState.INVALID)
            return status;
        else if (oldProduct == null)
        {
            return new ProductStatusImpl(ProductState.INVALID, singletonError("gtin", "No such GTIN"), null);
        }
        else if (!reconcilePOToQuotation(po, status.getQuotation()))
            return new ProductStatusImpl(ProductState.AWAITING_PAYMENT, null, status.getQuotation());
        else
        {
            // Important to do the commerce *after* the product is updated, so that commerce handlers will see the updated product
            m_productManager.updateProduct(appContext.getAgentUser(), appContext.getUsername(), appContext.getGbAccount(), product, new ProductManager.Callback(){
                public void callback(ProductRecord productRecord)
                {
                    String orderId = createSalesOrder(appContext, po);
                }
            });
            //printProduct(getProductByGtin(gbAccount, product.getGtin()), "updated db");
            return new ProductStatusImpl(ProductState.COMPLETED);
        }
    }

    private String createSalesOrder(GBAppContext appContext, PurchaseOrder po)
    {
        if (po == null)
            return null;
        else
        {
            SalesOrderRecord salesOrder = m_commerceManager.createSalesOrder(appContext.getAgentUser(), appContext.getUsername(), appContext.getGbAccount(), po, m_clockService.now());
            return salesOrder.getOrderId();
        }
    }
    
    @org.springframework.transaction.annotation.Transactional(propagation=Propagation.REQUIRES_NEW)
    public ProductStatus putProductInTransaction(GBAppContext appContext, Product unpaddedProduct, boolean renew)
            throws GlobalBrokerException
    {
        return putProduct(appContext, unpaddedProduct, renew);
    }

    public ProductStatus putProduct(final GBAppContext appContext, Product unpaddedProduct, boolean renew)
            throws GlobalBrokerException
    {
        PaddedGtinProduct product = new PaddedGtinProduct(unpaddedProduct);
        Product oldProduct = getProductByGtin(appContext.getGbAccount(), product.getGtin());
        Action action = (renew ? 
                                Action.RENEW : 
                                    oldProduct == null ?
                                                        Action.CREATE :
                                                            Action.UPDATE);
        nullsToOldValues(oldProduct, product);
        ProductStatus status = validateProductInternal(appContext, oldProduct, product, action);
        //printProduct(product, "update");
        if (status.getState() == ProductState.INVALID)
            return status;
        else
        {
            final PurchaseOrder po = quotationToPurchaseOrder(status.getQuotation());
            ProductManager.Callback callback = new ProductManager.Callback(){
                public void callback(ProductRecord productRecord)
                {
                    String orderId = createSalesOrder(appContext, po);
                }
            };
            // Important to do the commerce *after* the product is created, so that commerce handlers will see the updated product
            if (action == Action.CREATE)
                m_productManager.createProduct(appContext.getAgentUser(), appContext.getUsername(), appContext.getGbAccount(), product, callback);
            else
                m_productManager.updateProduct(appContext.getAgentUser(), appContext.getUsername(), appContext.getGbAccount(), product, callback);
            long end = System.currentTimeMillis();
            //printProduct(getProductByGtin(gbAccount, product.getGtin()), "updated db");
            return new ProductStatusImpl(ProductState.COMPLETED, null, status.getQuotation());
        }
    }

    public ProductStatus deleteProduct(AgentUser principal,
            String username, String gtin, GBAccount gbAccount) throws GlobalBrokerException
    {
        // TODO: deal with the PO
        Product product = getProductByGtin(gbAccount, gtin);
        if (product == null)
        {
            return new ProductStatusImpl(ProductState.INVALID, singletonError("gtin", "No such GTIN"), null);
        }
        else
        {
            m_productManager.deleteProduct(principal, username, gbAccount, product, null);
            return new ProductStatusImpl(ProductState.COMPLETED, null, null);
        }
    }
    
    

    public List<UploadValidationProduct> bulkUploadProducts(final GBAppContext appContext, List<? extends InboundProductAttribute> productAttrList)
            throws GlobalBrokerException
    {
    
    	List<UploadValidationProduct> uploadProductResultList = new ArrayList<UploadValidationProduct>();
    	
	    for (InboundProductAttribute productAttr:productAttrList) {
	       	
	    	System.out.println("Calling product creation for :" + productAttr.getGtin());
	
	        String gtin = UserInputUtil.trimToNull((String) productAttr.getGtin() );
	        // Hack to avoid error with null GTIN -- validation will flag this
	        if (gtin == null)
	            gtin = "0";
	        
	        System.out.println("Bulk Upload Step 2 :");
	        Product unpaddedProduct = m_productManager.newProduct(gtin);
	        
	        unpaddedProduct.setDataAccuracyAckUser(appContext.getUsername());
	        
	        //This is not needed as there is no GTIN in path parameter.
	        //checkGtinAgreement(product.getGtin(), product);
	    	  
	        AttributeSet attrSet = unpaddedProduct.getAttributes();

	        //TODO: Prepare productObj....	        
	        //TODO: Fix this method....

	        convertProductAttributes(attrSet, productAttr);

	        
	        System.out.println("Bulk Upload Step 3 : Put prroduct status in transaction");
	        ProductStatus productStatus =  putProductInTransaction(appContext, unpaddedProduct, false);
	    	
	        InboundUploadValidationProduct inboundUploadValidationProduct = new InboundUploadValidationProduct();
	        inboundUploadValidationProduct.setGtin(gtin);
	        inboundUploadValidationProduct.setStatus(productStatus);
	        //TODO: Enhance this method
	        inboundUploadValidationProduct.setStatusCode(productStatus.getState() != ProductState.INVALID ? "200":"400");
	        
	        uploadProductResultList.add(inboundUploadValidationProduct);
	    	
	    	
	    }

	    return uploadProductResultList;
    }

    
    
    public Product getProductByGtin(GBAccount gbAccount, String gtin)
            throws GlobalBrokerException
    {
        Product product = m_productManager.getProductByGtin(gbAccount, gtin);
        if (product == null)
            return null;
        else
            // TODO: Not needed after API is done
            return new JpaProductRecord((JpaProductRecord)product);
    }

    private Collection<ProductValidationError> singletonError(String path, String errorMessage)
    {
        return Collections.singleton((ProductValidationError)new ProductValidationErrorImpl(path, errorMessage));
    }

    /*
        private void printProduct(Product product, String heading)
        {
            System.out.format("GTIN %s, %s\n", product.getGtin(), heading);
            for (Entry<ProductAttributeDesc, String> entry : product.getAttributeMap().entrySet())
            {
                System.out.format("%s:\t%s\n", entry.getKey().getName(), entry.getValue());
            }
        }
     */

    private boolean reconcilePOToQuotation(PurchaseOrder po, Quotation quotation)
    {
        if (po == null && quotation == null)
            return true;
        else if (po != null && quotation != null)
        {
            // TODO: For now, we expect first N items in the PO to match all the items in the quotation, in order. (expecting that remaining
            // items in PO are taxes) Could relax this, perhaps?
            Iterator<? extends OrderLineItem> poIterator = po.getLineItems().iterator();
            for (OrderLineItem quotationLineItem : quotation.getLineItems())
            {
                if (!poIterator.hasNext())
                    return false;

                OrderLineItem poLineItem = poIterator.next();
                if (!quotationLineItem.equals(poLineItem))
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    private void nullsToOldValues(Product oldProduct, Product newProduct)
    {
        AttributeSet newAttributes = newProduct.getAttributes();
        AttributeSet oldAttributes = oldProduct == null ? null : oldProduct.getAttributes();

        newAttributes.nullsToOldValues(oldAttributes);
    }

    
    private AttributeSet convertProductAttributes( AttributeSet attrSet, InboundProductAttribute productAttr){
    	//TODO: Use ObjectMapper to convert from json to AttributeSet.
 
    	System.out.println("Bulk Upload Step 4 : Put prroduct status in transaction");
    	
    	attrSet.setAttribute("itemDataLanguage", productAttr.getItemDataLanguage());
    	attrSet.setAttribute("brandName", productAttr.getBrandName());
    	attrSet.setAttribute("additionalTradeItemDescription", productAttr.getAdditionalTradeItemDescription());
    	attrSet.setAttribute("targetMarket", productAttr.getTargetMarket());
    	attrSet.setAttribute("gpcCategoryCode", productAttr.getGpcCategoryCode());
    	attrSet.setAttribute("informationProviderGLN", productAttr.getInformationProviderGLN());
    	attrSet.setAttribute("itemDataLanguage", productAttr.getItemDataLanguage());
    	attrSet.setAttribute("companyName", productAttr.getCompanyName());
    	attrSet.setAttribute("uriProductImage", productAttr.getUriProductImage());
    	
    	System.out.println("Bulk Upload Step 4 :" + attrSet.toString());
    	
    	//product.setGpcCategoryCode(productAttr.getGpcCategoryCode()); not needed being set in createProduct
    	//product.setTargetCountryCode(productAttr.getTargetMarket()); not needed being set in createProduct
    	//product.setAttributes(productAttr);
    	//product.setDataAccuracyAckUser(null); //Hardcode to null for now
    	    	
    	return attrSet;
    }


    private PurchaseOrder quotationToPurchaseOrder(Quotation quotation)
    {
        if (quotation == null)
            return null;
        else
            return new PurchaseOrderImpl(null, m_clockService.now(), quotation.getLineItems());
    }

    public static String padGtin(String unpaddedGtin)
    {
        int len = unpaddedGtin.length();
        if (len == 13 || len == 12 || len == 8)
            return "00000000000000".substring(0, 14-len) + unpaddedGtin;
        else
            return unpaddedGtin;
    }

    public static class PaddedGtinProduct implements Product
    {
        private Product m_unpaddedProduct;

        public PaddedGtinProduct(Product product)
        {
            m_unpaddedProduct = product;
        }

        public Product getUnpaddedProduct()
        {
            return m_unpaddedProduct;
        }

        public String getGtin()
        {
            String unpaddedGtin = m_unpaddedProduct.getGtin();
            return padGtin(unpaddedGtin);
        }

        public Integer getTargetCountryCode()
        {
            return m_unpaddedProduct.getTargetCountryCode();
        }

        public String getGpcCategoryCode()
        {
            return m_unpaddedProduct.getGpcCategoryCode();
        }
        
        public String getDataAccuracyAckUser()
        {
            return m_unpaddedProduct.getDataAccuracyAckUser();
        }

        public void setDataAccuracyAckUser(String dataAccuracyAckUser)
        {
            m_unpaddedProduct.setDataAccuracyAckUser(dataAccuracyAckUser);
        }

        public AttributeSet getAttributes()
        {
            return m_unpaddedProduct.getAttributes();
        }

        public Date getRegisteredDate()
        {
            return m_unpaddedProduct.getRegisteredDate();
        }

        public Date getModifiedDate()
        {
            return m_unpaddedProduct.getModifiedDate();
        }

        public Date getNextActionDate()
        {
            return m_unpaddedProduct.getNextActionDate();
        }

        public Date getPendingNextActionDate()
        {
            return m_unpaddedProduct.getPendingNextActionDate();
        }

        public String[] getPendingOrderIds()
        {
            return m_unpaddedProduct.getPendingOrderIds();
        }


    }


}
