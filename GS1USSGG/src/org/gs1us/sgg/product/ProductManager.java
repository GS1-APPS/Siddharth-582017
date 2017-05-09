package org.gs1us.sgg.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gs1us.sgg.attribute.AttributeDescImpl;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.AuditEventRecord;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.dao.jpa.ProductAttributesConverter;
import org.gs1us.sgg.gbservice.api.AttributeEnumValue;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AuditEventType;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeType;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.validation.AttributeValidator;

public class ProductManager
{
    @Resource
    private ClockService m_clockService;

    @Resource
    private GBDao m_gbDao;
    
    public interface Callback
    {
        public void callback(ProductRecord product);
    }
    
    private List<ProductEventHandler> m_productPreCallbackEventHandlers = new ArrayList<>();
    private List<ProductEventHandler> m_productPostCallbackEventHandlers = new ArrayList<>();
    
    public void addProductPreCallbackEventHandler(ProductEventHandler handler)
    {
        m_productPreCallbackEventHandlers.add(handler);
    }

    public void addProductPostCallbackEventHandler(ProductEventHandler handler)
    {
        m_productPostCallbackEventHandlers.add(handler);
    }

    public AttributeDesc newProductAttributeDesc(String name, String title, String[] importHeadings, String groupHeading, AttributeType type,
            boolean isRequired, String entryInstructions, AttributeValidator[] validators)
    {
        return new AttributeDescImpl(name, title, importHeadings, groupHeading, type, isRequired, entryInstructions, validators);
    }
    
    public AttributeDesc newProductAttributeDesc(String name, String title, String[] importHeadings, String groupHeading, AttributeType type,
            List<AttributeEnumValue> enumValues,
            boolean isRequired, int actions, String entryInstructions, AttributeValidator[] validators)
    {
        return new AttributeDescImpl(name, title, importHeadings, groupHeading, type, enumValues, isRequired, actions, entryInstructions, validators);
    }
    
    public Collection<? extends Product> getProducts(GBAccount gbAccount)
        throws GlobalBrokerException
    {
        return m_gbDao.getProductsByGln(gbAccount.getGln());
    }

    public Collection<? extends Product> getProductsForReport()
            throws GlobalBrokerException
    {
        return m_gbDao.getProductsForReport();
    }
        
    public Product getProductByGtin(GBAccount gbAccount, String gtin)
        throws GlobalBrokerException
    {
        if (gbAccount == null)
            return m_gbDao.getProductByGtin(gtin);
        else
            return m_gbDao.getProductByGlnAndGtin(gbAccount.getGln(), gtin);
    }

    public Product newProduct(String gtin)
    {
        ProductRecord productRecord = m_gbDao.newProduct();
        productRecord.setGtin(gtin);
        return productRecord;
    }

    public void createProduct(AgentUser agentUser, String username, GBAccount gbAccount, Product product, Callback callback)
    {
        ProductRecord productRecord = m_gbDao.newProduct();
        Date registeredDate = m_clockService.now();
        productRecord.setGtin(product.getGtin());
        productRecord.setGBAccountGln(gbAccount.getGln());
        productRecord.setRegisteredDate(registeredDate);
        productRecord.setModifiedDate(registeredDate);
        productRecord.setAttributes(product.getAttributes());
        
        // TODO: temporary hack! Need to generalize and do it right.
        productRecord.getAttributes().setAttribute("brandOwnerName", gbAccount.getName());
        
        for (ProductEventHandler handler : m_productPreCallbackEventHandlers)
            handler.handleCreate(agentUser, username, gbAccount, productRecord);
        // Have to do an update before callback in case the callback fetches the product record
        m_gbDao.updateProduct(productRecord);
        if (callback != null)
            callback.callback(productRecord);
        for (ProductEventHandler handler : m_productPostCallbackEventHandlers)
            handler.handleCreate(agentUser, username, gbAccount, productRecord);
        m_gbDao.updateProduct(productRecord);
        
        AuditEventRecord event = m_gbDao.newAuditEvent();
        event.setType(AuditEventType.CREATE_PRODUCT);
        event.setDate(registeredDate);
        event.setAgentUsername(agentUser.getUsername());
        event.setUsername(username);
        event.setGBAccountGln(gbAccount.getGln());
        event.setGtin(product.getGtin());
        m_gbDao.updateAuditEvent(event);
    }

    public void updateProduct(AgentUser agentUser, String username, GBAccount gbAccount, Product product, Callback callback)
    {
        Date modifiedDate = m_clockService.now();
        
        ProductRecord productRecord = m_gbDao.getProductByGlnAndGtin(gbAccount.getGln(), product.getGtin());
        if (productRecord != null)
        {
            //String auditDetails = auditDetailsForProductUpdate(productRecord, product);
            productRecord.setModifiedDate(modifiedDate);
            productRecord.setAttributes(product.getAttributes());
            internalUpdateProduct(agentUser, username, gbAccount, productRecord, callback);
            
            AuditEventRecord event = m_gbDao.newAuditEvent();
            event.setType(AuditEventType.UPDATE_PRODUCT);
            event.setDate(modifiedDate);
            if (agentUser != null)
                event.setAgentUsername(agentUser.getUsername());
            event.setUsername(username);
            event.setGBAccountGln(gbAccount.getGln());
            event.setGtin(productRecord.getGtin());
            //event.setDetails(auditDetails);
            m_gbDao.updateAuditEvent(event);
        }
        // TODO: else?
    }
/*
    private String auditDetailsForProductUpdate(ProductRecord oldProduct,
            Product newProduct)
    {
        List<String> added = new ArrayList<>();
        List<String> modified = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        
        for (Map.Entry<String,String> entry : newProduct.getAttributes().getAttributes().entrySet())
        {
            String name = entry.getKey();
            String value = entry.getValue();
            
            if (value != null && value.length() > 0)
            {
                String oldValue = oldProduct.getAttributes().getAttribute(name);
                if (oldValue == null || oldValue.length() == 0)
                    added.add(name);
                else if (!oldValue.equals(value))
                    modified.add(name);
            }
        }
        for (Map.Entry<String,String> entry : oldProduct.getAttributes().getAttributes().entrySet())
        {
            String name = entry.getKey();
            String value = entry.getValue();
            
            if (value != null && value.length() > 0)
            {
                String newValue = newProduct.getAttributes().getAttribute(name);
                if (newValue == null || newValue.length() == 0)
                    removed.add(name);
            }
        }
        
        if (added.isEmpty() && modified.isEmpty() && removed.isEmpty())
            return null;
        
        StringBuffer result = new StringBuffer();
        if (!added.isEmpty())
        {
            result.append("Added ");
            appendList(result, added);
            result.append(".");
        }
        if (!modified.isEmpty())
        {
            if (result.length() > 0)
                result.append(' ');
            result.append("Changed ");
            appendList(result, modified);
            result.append(".");
        }
        if (!removed.isEmpty())
        {
            if (result.length() > 0)
                result.append(' ');
            result.append("Removed ");
            appendList(result, removed);
            result.append(".");
        }
        return result.toString();
    }

    private void appendList(StringBuffer result, List<String> l)
    {
        boolean first = true;
        for (String elt : l)
        {
            if (!first)
                result.append(", ");
            result.append(elt);
            first = false;
        }
        
    }
*/
    public void internalUpdateProduct(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord, Callback callback)
    {
        for (ProductEventHandler handler : m_productPreCallbackEventHandlers)
            handler.handleUpdate(agentUser, username, gbAccount, productRecord);
        if (callback != null)
            callback.callback(productRecord);
        for (ProductEventHandler handler : m_productPostCallbackEventHandlers)
            handler.handleUpdate(agentUser, username, gbAccount, productRecord);
        m_gbDao.updateProduct(productRecord);
    }

    public void deleteProduct(AgentUser agentUser, String username, GBAccount gbAccount, Product product, Callback callback)
    {
        ProductRecord productRecord = m_gbDao.getProductByGlnAndGtin(gbAccount.getGln(), product.getGtin());
        if (productRecord != null)
        {
            for (ProductEventHandler handler : m_productPreCallbackEventHandlers)
                handler.handleDelete(agentUser, username, gbAccount, productRecord);
            if (callback != null)
                callback.callback(productRecord);
            for (ProductEventHandler handler : m_productPostCallbackEventHandlers)
                handler.handleDelete(agentUser, username, gbAccount, productRecord);
            m_gbDao.deleteProduct(productRecord);
        }
    }



}
