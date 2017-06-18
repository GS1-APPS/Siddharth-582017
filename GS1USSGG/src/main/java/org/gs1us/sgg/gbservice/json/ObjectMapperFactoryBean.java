package org.gs1us.sgg.gbservice.json;

import java.math.BigDecimal;
import java.util.Collection;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportPrevalidation;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationColumn;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegment;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegmentSettings;
import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.api.ImportValidationProduct;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.ModuleDesc;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.Payment;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;
import org.gs1us.sgg.gbservice.api.Quotation;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgg.gbservice.api.UploadValidationProduct;
import org.gs1us.sgg.gbservice.api.AttributeEnumValue;
import org.gs1us.sgg.util.Util;
import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper>
{

    @Override
    public ObjectMapper getObject() 
    {
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.addFilter("Amount",
                          SimpleBeanPropertyFilter.filterOutAllExcept("value", "currency"));
        filters.addFilter("AppDesc",
                          SimpleBeanPropertyFilter.filterOutAllExcept("name", "title", "iconName", "description", "accountModuleDesc", "subscriptionModuleDesc", "productModuleDesc"));
        filters.addFilter("AppSubscription",
                          SimpleBeanPropertyFilter.filterOutAllExcept("appName", "appDesc", "subscribedByAgentUsername", "subscribedByUsername", "subscriptionDate", "appArgs"));
        filters.addFilter("AttributeDesc",
                          SimpleBeanPropertyFilter.filterOutAllExcept("name", "title", "importHeadings", "groupHeading", "type", "enumValues", "required", "actions", "entryInstructions"));
        filters.addFilter("BillingTransaction",
                          SimpleBeanPropertyFilter.filterOutAllExcept("type", "date", "reference", "description", "amount", "balance"));
        filters.addFilter("GBAccount",
                          SimpleBeanPropertyFilter.filterOutAllExcept("gln", "name", "gcps", "attributes"));
        filters.addFilter("Import",
                          SimpleBeanPropertyFilter.filterOutAllExcept("id", "gln", "uploadDate", "validatedDate", "confirmedDate", "filename", "format", "status", "prevalidation", "validation"));
        filters.addFilter("ImportPrevalidation",
                          SimpleBeanPropertyFilter.filterOutAllExcept("fileError", "segments"));
        filters.addFilter("ImportPrevalidationColumn",
                          SimpleBeanPropertyFilter.filterOutAllExcept("name", "status"));
        filters.addFilter("ImportPrevalidationSegment",
                          SimpleBeanPropertyFilter.filterOutAllExcept("columns", "name", "nonblankRowCount", "rowCount", "segmentErrors", "settings"));
        filters.addFilter("ImportPrevalidationSegmentSettings",
                          SimpleBeanPropertyFilter.filterOutAllExcept("columnMappings", "constantAttributeValueMap", "enabled"));
        filters.addFilter("ImportValidation",
                          SimpleBeanPropertyFilter.filterOutAllExcept("validationProducts"));
        filters.addFilter("ImportValidationProduct",
                          SimpleBeanPropertyFilter.filterOutAllExcept("gtin", "status"));
        filters.addFilter("Invoice",
                          SimpleBeanPropertyFilter.filterOutAllExcept("invoiceId", "gln", "date", "summary", "total", "orderStatus", "billingReference", "extras", "billedDate", "paymentCommittedDate", "paidDate"));
        filters.addFilter("InvoiceExtra",
                          SimpleBeanPropertyFilter.filterOutAllExcept("itemCode", "itemDescription", "itemParameters", "total"));
        filters.addFilter("ModuleDesc",
                          SimpleBeanPropertyFilter.filterOutAllExcept("selectionAttribute", "startDateAttribute", "endDateAttribute", "paidThruDateAttribute", "pendingPaidThruDateAttribute", "pkAttributeDescs", "userAttributeDescs"));
        filters.addFilter("OrderLineItem",
                          SimpleBeanPropertyFilter.filterOutAllExcept("quantity", "itemCode", "itemDescription", "itemParameters", "price", "total"));
        filters.addFilter("Payment",
                          SimpleBeanPropertyFilter.filterOutAllExcept("gln", "paymentId", "date", "paymentReceiptId", "amount", "status", "paidReference" ));
        filters.addFilter("PaymentReceipt",
                          SimpleBeanPropertyFilter.filterOutAllExcept("paymentId", "date", "amount"));
        filters.addFilter("Product",
                          SimpleBeanPropertyFilter.filterOutAllExcept("gtin", "dataAccuracyAckUser", "attributes", "registeredDate", "modifiedDate", "nextActionDate", "pendingNextActionDate", "pendingOrderIds" ));
        filters.addFilter("ProductStatus",
                          SimpleBeanPropertyFilter.filterOutAllExcept("state", "validationErrors", "quotation"));
        filters.addFilter("ProductValidationError",
                          SimpleBeanPropertyFilter.filterOutAllExcept("path", "errorMessage"));
        filters.addFilter("PurchaseOrder",
                          SimpleBeanPropertyFilter.filterOutAllExcept("poId", "date", "lineItems"));
        filters.addFilter("Quotation",
                          SimpleBeanPropertyFilter.filterOutAllExcept("lineItems"));
        filters.addFilter("SalesOrder",
                          SimpleBeanPropertyFilter.filterOutAllExcept("orderId", "gln", "date", "poId", "lineItems", "summary", "total", "invoiceId"));
        filters.addFilter("UploadValidationProduct",
                SimpleBeanPropertyFilter.filterOutAllExcept("gtin", "statusCode", "validationErrors"));
        filters.addFilter("Uom",
                          SimpleBeanPropertyFilter.filterOutAllExcept("code", "displayName"));


        ObjectMapper om = new ObjectMapper();
        om.setFilterProvider(filters);
        om
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.WRITE_NULL_MAP_VALUES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.addMixIn(Amount.class, AmountMixin.class);
        om.addMixIn(AppDesc.class, AppDescMixin.class);
        om.addMixIn(AppSubscription.class, AppSubscriptionMixin.class);
        om.addMixIn(AttributeDesc.class, AttributeDescMixin.class);
        om.addMixIn(BillingTransaction.class, BillingTransactionMixin.class);
        om.addMixIn(Import.class, ImportMixin.class);
        om.addMixIn(ImportPrevalidation.class, ImportPrevalidationMixin.class);
        om.addMixIn(ImportPrevalidationColumn.class, ImportPrevalidationColumnMixin.class);
        om.addMixIn(ImportPrevalidationSegment.class, ImportPrevalidationSegmentMixin.class);
        om.addMixIn(ImportPrevalidationSegmentSettings.class, ImportPrevalidationSegmentSettingsMixin.class);
        om.addMixIn(ImportValidation.class, ImportValidationMixin.class);
        om.addMixIn(ImportValidationProduct.class, ImportValidationProductMixin.class);
        om.addMixIn(GBAccount.class, GBAccountMixin.class);
        om.addMixIn(Invoice.class, InvoiceMixin.class);
        om.addMixIn(InvoiceExtra.class, InvoiceExtraMixin.class);
        om.addMixIn(ModuleDesc.class, ModuleDescMixin.class);
        om.addMixIn(OrderLineItem.class, OrderLineItemMixin.class);
        om.addMixIn(Payment.class, PaymentMixin.class);
        om.addMixIn(PaymentReceipt.class, PaymentReceiptMixin.class);
        om.addMixIn(Product.class, ProductMixin.class);
        om.addMixIn(ProductStatus.class, ProductStatusMixin.class);
        om.addMixIn(ProductValidationError.class, ProductValidationErrorMixin.class);
        om.addMixIn(PurchaseOrder.class, PurchaseOrderMixin.class);
        om.addMixIn(Quotation.class, QuotationMixin.class);
        om.addMixIn(SalesOrder.class, SalesOrderMixin.class);
        om.addMixIn(AttributeEnumValue.class, UomMixin.class);
        om.addMixIn(UploadValidationProduct.class, UploadValidationProductMixin.class);


        om.setDateFormat(Util.ISO8601_DATE_FORMAT); 

        return om;
    }

    @Override
    public Class<?> getObjectType()
    {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton()
    {
        return false;
    }
    
    @JsonFilter("Amount")
    public static abstract class AmountMixin 
    {
        @JsonCreator
        public AmountMixin(@JsonProperty("value") BigDecimal value, @JsonProperty("currency") String currency)
        {
            
        }

    }

    @JsonFilter("AppDesc")
    public static abstract class AppDescMixin 
    {
    }

    @JsonFilter("AppSubscription")
    public static abstract class AppSubscriptionMixin 
    {
        @JsonInclude(value=Include.NON_EMPTY)
        public abstract AppDesc getAppDesc();
        
        @JsonInclude(value=Include.NON_EMPTY)
        public abstract Object getAppArgs();
        
    }

    @JsonFilter("AttributeDesc")
    public static abstract class AttributeDescMixin 
    {
    }

    @JsonFilter("BillingTransaction")
    public static abstract class BillingTransactionMixin 
    {
    }

    @JsonFilter("GBAccount")
    public static abstract class GBAccountMixin 
    {
    }

    @JsonFilter("Import")
    public static abstract class ImportMixin 
    {
        @JsonProperty("gln")
        public abstract String getGBAccountGln();
    }

    @JsonFilter("ImportPrevalidation")
    public static abstract class ImportPrevalidationMixin 
    {
    }
    
    @JsonFilter("ImportPrevalidationColumn")
    public static abstract class ImportPrevalidationColumnMixin 
    {
    }
    
    @JsonFilter("ImportPrevalidationSegment")
    public static abstract class ImportPrevalidationSegmentMixin 
    {
    }
    
    @JsonFilter("ImportPrevalidationSegmentSettings")
    public static abstract class ImportPrevalidationSegmentSettingsMixin 
    {
    }
    
    @JsonFilter("ImportValidation")
    public static abstract class ImportValidationMixin 
    {
    }
    
    @JsonFilter("ImportValidationProduct")
    public static abstract class ImportValidationProductMixin 
    {
    }
    
    @JsonFilter("Invoice")
    public static abstract class InvoiceMixin 
    {
        @JsonProperty("gln")
        public abstract String getGBAccountGln();
    }

    @JsonFilter("InvoiceExtra")
    public static abstract class InvoiceExtraMixin 
    {
    }

    @JsonFilter("ModuleDesc")
    public static abstract class ModuleDescMixin 
    {
    }

    @JsonFilter("OrderLineItem")
    public static abstract class OrderLineItemMixin 
    {
    }


    @JsonFilter("Payment")
    public static abstract class PaymentMixin 
    {
        @JsonProperty("gln")
        public abstract String getGBAccountGln();
    }


    @JsonFilter("PaymentReceipt")
    public static abstract class PaymentReceiptMixin 
    {
    }


    @JsonFilter("Product")
    public static abstract class ProductMixin 
    {
    }

    @JsonFilter("ProductStatus")
    public static abstract class ProductStatusMixin 
    {
    }

    @JsonFilter("ProductValidationError")
    public static abstract class ProductValidationErrorMixin 
    {
    }

    @JsonFilter("PurchaseOrder")
    public static abstract class PurchaseOrderMixin 
    {
    }


    @JsonFilter("Quotation")
    public static abstract class QuotationMixin 
    {
    }


    @JsonFilter("SalesOrder")
    public static abstract class SalesOrderMixin 
    {
        @JsonProperty("gln")
        public abstract String getGBAccountGln();
    }

    @JsonFilter("Uom")
    public static abstract class UomMixin 
    {
    }

    @JsonFilter("UploadValidationProduct")
    @JsonPropertyOrder({ "gtin", "statusCode","validationErrors" })
    public static abstract class UploadValidationProductMixin 
    {
        @JsonProperty("gtin")
        public abstract String getGtin();

        @JsonProperty("statusCode")
        public abstract String getStatusCode();
        
        @JsonProperty("validationErrors")   
        //@JsonIgnoreProperties({"quotation","state"})
        @JsonInclude(Include.NON_NULL)
        public abstract Collection<? extends ProductValidationError> getValidationErrors();
        
    }
    


}
