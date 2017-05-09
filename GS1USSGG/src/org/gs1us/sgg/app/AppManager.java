package org.gs1us.sgg.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.gs1us.sgg.app.dwcode.DWCodeIntegrationAppHandler;
import org.gs1us.sgg.app.dwcode.DWCodeIntegrationProductHandler;
import org.gs1us.sgg.app.dwcode.DigimarcOptions;
import org.gs1us.sgg.app.dwcode.DigimarcService;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.commerce.CommerceEventHandler;
import org.gs1us.sgg.commerce.CommerceManager;
import org.gs1us.sgg.commerce.OrderableItemDesc;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.AppSubscriptionRecord;
import org.gs1us.sgg.dao.AuditEventRecord;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.dao.PaymentRecord;
import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.dao.SalesOrderRecord;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.AuditEventType;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeType;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.Quotation;
import org.gs1us.sgg.gbservice.api.AttributeEnumValue;
import org.gs1us.sgg.product.ProductEventHandler;
import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.validation.AffirmationValidator;
import org.gs1us.sgg.validation.AttributeValidator;
import org.gs1us.sgg.validation.DateValidator;
import org.gs1us.sgg.validation.DigitOnlyAttributeValidator;
import org.gs1us.sgg.validation.GLNAttributeValidator;
import org.gs1us.sgg.validation.LengthAttributeValidator;
import org.gs1us.sgg.validation.MeasurementAttributeValidator;
import org.gs1us.sgg.validation.OtherAttributeRequiredValidator;
import org.gs1us.sgg.validation.RequiredAttributeValidator;
import org.gs1us.sgg.validation.UPCEValidator;
import org.gs1us.sgg.validation.UrlValidator;
import org.gs1us.sgg.validation.WhitelistedCharValidator;

public class AppManager
{
    public static final String DWCODE_APP_NAME = "dwcode";

    @Resource
    private ClockService m_clockService;
    
    @Resource
    private ProductManager m_productManager;
    
    @Resource
    private CommerceManager m_commerceManager;
    
    @Resource
    private DigimarcService m_digimarcService;
    
    @Resource
    private DigimarcOptions m_digimarcOptions;
    
    @Resource
    private GBDao m_gbDao;
    
    private List<AttributeEnumValue> m_uoms;
    private List<AttributeEnumValue> m_stateTaxJurisdictions;
    private List<AttributeEnumValue> m_localTaxJurisdictions;
    private List<AttributeEnumValue> m_dmLicenseTypes;

    private AppDescImpl m_basicAppDesc;
    private List<AppDesc> m_appDescs;
    
    private AttributeDesc m_dmPaidThruAttribute;
    private AttributeDesc m_dmPendingPaidThruAttribute;
    private AttributeDesc m_dmLicenseTypeAttribute;
    
    private Quoter m_quoter;
    
    private Map<String,AppEventHandler> m_appEventHandlerMap = new HashMap<String, AppEventHandler>();
    
    public void addEventHandler(String appName, AppEventHandler handler)
    {
        m_appEventHandlerMap.put(appName, handler);
    }
    
    // public AppManager()
    @PostConstruct
    private void init()
    {
        initUoms();
        initStateTaxJurisdictions();
        initLocalTaxJurisdictions();
        initDmLicenseTypes();
        m_quoter = new Quoter();
        
        m_basicAppDesc = initBasicApp();
        
        AppDescImpl dmAppDesc = initDmApp();
        
        m_appDescs = new ArrayList<>();
        m_appDescs.add(m_basicAppDesc);
        m_appDescs.add(dmAppDesc);
        m_appDescs.add(initCloudPrototypeApp());
        
        DWCodeEventHandler dwCodeEventHandler = new DWCodeEventHandler();
        dwCodeEventHandler.add("50PerGtinCheck", OrderStatus.PAYMENT_COMMITTED);

        m_commerceManager.addEventHandler(DWCODE_APP_NAME, dwCodeEventHandler);
        m_commerceManager.addEventHandler(DWCODE_APP_NAME + "-refund", dwCodeEventHandler);
        
        m_productManager.addProductPostCallbackEventHandler(new NextActionDateHandler());
        DWCodeIntegrationProductHandler dwCodeProductEventHandler = new DWCodeIntegrationProductHandler(m_digimarcService, m_digimarcOptions, m_clockService);
        m_productManager.addProductPostCallbackEventHandler(dwCodeProductEventHandler);
        
        addEventHandler(DWCODE_APP_NAME, new DWCodeIntegrationAppHandler(m_digimarcService, m_gbDao));
    }

    private AppDescImpl initBasicApp()
    {
        List<AttributeDesc> basicPkAttributes = new ArrayList<>();
        basicPkAttributes.add(m_productManager.newProductAttributeDesc("gtin", "GTIN", new String[]{"gtin"}, null, AttributeType.STRING, true, 
                                                                       "The GTIN of the product, as printed on the package.",
                                                                       new AttributeValidator[]{new RequiredAttributeValidator()}));
        List<AttributeDesc> basicAttributes = new ArrayList<>();
        basicAttributes.add(m_productManager.newProductAttributeDesc("brandName", "Brand Name", new String[]{"brand name", "brandname", "brand"}, "Basic product info", AttributeType.STRING, true, 
                                                         "The brand of the product, as printed on the package.",
                                                         new AttributeValidator[]{new RequiredAttributeValidator(), new LengthAttributeValidator(70), new WhitelistedCharValidator()}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("subBrand", "Sub-brand Name", new String[]{"subbrand", "sub-brand", "subbrand name", "sub-brand name"}, "Basic product info", AttributeType.STRING, false, 
                "The sub-brand of the product, as printed on the package. Required when the product package includes both a brand name and a"
                + " sub brand within the main brand. Leave blank if not applicable.",
                new AttributeValidator[]{new LengthAttributeValidator(70), new WhitelistedCharValidator()}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("productName", "Product Name", new String[]{"productname", "product name", "name"}, "Basic product info", AttributeType.STRING, true, 
                "The product name as printed on the package.",
                new AttributeValidator[]{new RequiredAttributeValidator(), new LengthAttributeValidator(500), new WhitelistedCharValidator()}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("description", "Product Description", new String[]{"productdescription", "product description", "description"}, "Basic product info", AttributeType.STRING, false, 
                "The description of the product as printed on the package. Required if the package includes a description. Leave blank if not applicable.",
                new AttributeValidator[]{new LengthAttributeValidator(500), new WhitelistedCharValidator()}));
        AttributeDesc declaredQuantity1Desc = m_productManager.newProductAttributeDesc("netContent1", "Declared Quantity / Net Content 1",
                                                                                       new String[]{"declaredquantity", "declared quantity", "netcontent", "net content", "declared quantity / net content", "declaredquantity1", "declared quantity 1", "netcontent 1", "net content 1", "declared quantity / net content 1",},
                                                                                       "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK,
                "The first (or only) declared quantity or net content, as printed on the package.",
                new AttributeValidator[]{/*new RequiredAttributeValidator(), */new MeasurementAttributeValidator()});
        basicAttributes.add(declaredQuantity1Desc);
        AttributeDesc declaredQuantity2Desc = m_productManager.newProductAttributeDesc("netContent2", "Declared Quantity / Net Content 2", 
                                                                                       new String[]{"declaredquantity2", "declared quantity 2", "netcontent 2", "net content 2", "declared quantity / net content 2"},
                                                                                       "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK, 
                                                                     "The second declared quantity or net content, as printed on the package. Required if the package indicates two or more declared quantity or net content values.",
                                                                     new AttributeValidator[]{new MeasurementAttributeValidator(), new OtherAttributeRequiredValidator(declaredQuantity1Desc)});
        basicAttributes.add(declaredQuantity2Desc);
        AttributeDesc declaredQuantity3Desc = m_productManager.newProductAttributeDesc("netContent3", "Declared Quantity / Net Content 3", 
                                                                                       new String[]{"declaredquantity3", "declared quantity 3", "netcontent 3", "net content 3", "declared quantity / net content 3"},
                                                                                       "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK, 
                                                                     "The third declared quantity or net content, as printed on the package. Required if the package indicates three or more declared quantity or net content values.",
                                                                     new AttributeValidator[]{new MeasurementAttributeValidator(), new OtherAttributeRequiredValidator(declaredQuantity2Desc)});
        basicAttributes.add(declaredQuantity3Desc);
        basicAttributes.add(m_productManager.newProductAttributeDesc("netContent4", "Declared Quantity / Net Content 4", 
                                                                     new String[]{"declaredquantity4", "declared quantity 4", "netcontent 4", "net content 4", "declared quantity / net content 4"},
                                                                     "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK, 
                                                                     "The fourth declared quantity or net content, as printed on the package. Required if the package indicates four or more declared quantity or net content values.",
                                                                     new AttributeValidator[]{new MeasurementAttributeValidator(), new OtherAttributeRequiredValidator(declaredQuantity3Desc)}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("color", "Color", new String[]{"color", "colour"}, "Product characteristics", AttributeType.STRING, false, 
                "The color of the product, or similar distinguishing characteristic, as printed on the package. Required if the package indicates a color or if needed to"
                + " distinguish multiple products that share the same functional product name. Leave blank if not applicable.",
                new AttributeValidator[]{new LengthAttributeValidator(80)}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("flavor", "Flavor", new String[]{"flavor", "flavour"}, "Product characteristics", AttributeType.STRING, false, 
                                                                     "The flavor of the product, or similar distinguishing characteristic, as printed on the package. Required if the package indicates a flavor or if needed to"
                                                                     + " distinguish multiple products that share the same functional product name. Leave blank if not applicable.",
                                                                     new AttributeValidator[]{new LengthAttributeValidator(70), new WhitelistedCharValidator()}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("scent", "Scent", new String[]{"scent"}, "Product characteristics", AttributeType.STRING, false, 
                                                                     "The scent of the product, or similar distinguishing characteristic, as printed on the package. Required if the package indicates a scent or if needed to"
                                                                     + " distinguish multiple products that share the same functional product name. Leave blank if not applicable.",
                                                                     new AttributeValidator[]{new LengthAttributeValidator(70), new WhitelistedCharValidator()}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("size", "Size", new String[]{"size"}, "Product characteristics", AttributeType.STRING, false, 
                                                         "The size of the product, or similar distinguishing characteristic, as printed on the package. This or \"flavor or color\" is required if needed to"
                                                         + " distinguish multiple products that share the same functional product name. Unlike \"declared quantity / net content\", this attribute "
                                                         + "is free text, often something like S, M, L, XL, etc. Leave blank if not applicable.",
                                                         new AttributeValidator[]{new LengthAttributeValidator(80), new WhitelistedCharValidator()}));
        basicAttributes.add(m_productManager.newProductAttributeDesc("mobileDeviceImage", "Mobile Device Image URL", new String[]{"mobiledeviceimage", "mobiledeviceimageurl", "mobile device image url"}, "Product image", AttributeType.WEBURL, false, 
                                                                     "A publicly-accessible Web URL of an image of the product, optimized for display on mobile devices. "
                                                                     + "This URL should be for an image (jpg, png, etc), not for a web page (html).",
                                                                     new AttributeValidator[]{new LengthAttributeValidator(2500), new UrlValidator(), new WhitelistedCharValidator()}));
        AttributeDesc basicStartDateAttribute = m_productManager.newProductAttributeDesc("basicStartDate", "Start Date (MM/DD/YYYY)", new String[]{"basicstartdate", "basic start date", "start date", "startdate"}, "Start date", AttributeType.DATE, false, 
                                                                                    "The date upon which the GTIN and product data is available for consumers to use. "
                                                                                            + "Set this in the future if you want to enter a product before it is ready to enter the market.",
                                                                                            new AttributeValidator[]{new DateValidator()});
        basicAttributes.add(basicStartDateAttribute);
        AttributeDesc basicEndDateAttribute = m_productManager.newProductAttributeDesc("basicEndDate", "End Date", null, null, AttributeType.DATE, false, 
                                                                                    "The date upon which the GTIN and product data is no longer available for consumers to use. "
                                                                                            + "Leave blank if the data should be available indefinitely.",
                                                                                            new AttributeValidator[]{new DateValidator()});
        AttributeDesc basicPaidThruAttribute = m_productManager.newProductAttributeDesc("basicPaidThru", "Paid Through", null, null, AttributeType.DATE, false, 
                                                                                            "The date through which basic product data is valid.",
                                                                                                    new AttributeValidator[]{new DateValidator()});
        AttributeDesc basicPendingPaidThruAttribute = m_productManager.newProductAttributeDesc("basicPendingPaidThru", null, null, "Paid Through (pending)", AttributeType.DATE, false, 
                                                                                                        "The pending date through which basic product data is valid.",
                                                                                                                new AttributeValidator[]{new DateValidator()});
        List<AttributeDesc> basicAccountAttributes = new ArrayList<>();
        basicAccountAttributes.add(m_productManager.newProductAttributeDesc("brandOwnerName", 
                                                                         "Brand owner name", 
                                                                         null, 
                                                                         null,
                                                                         AttributeType.STRING,
                                                                         null,
                                                                         false, 
                                                                         Action.ALL_ACTIONS_MASK,
                                                                         "Brand owner name to be included with all products for this member, if different from company name",
                                                                         new AttributeValidator[]{new WhitelistedCharValidator()}));

        
        return new AppDescImpl(GlobalBrokerService.BASIC_APP_NAME, 
                                         "Basic product info", 
                                         "gs1", 
                                         "Basic product attributes, published by GS1, free for use to all", 
                                         new ModuleDescImpl(null, null, null, null, null,
                                                            null,
                                                            basicAccountAttributes,
                                                            new FreePricer()),
                                         null,
                                         new ModuleDescImpl(null, 
                                                            basicStartDateAttribute, basicEndDateAttribute, 
                                                            basicPaidThruAttribute, basicPendingPaidThruAttribute,
                                                            basicPkAttributes,
                                                            basicAttributes,
                                                            new FreePricer()));
    }
    
    private AppDescImpl initDmApp()
    {
        List<AttributeDesc> dmAccountAttributes = new ArrayList<>();
        /*
        AttributeDesc dmStateSalesTaxJurisdiction = m_productManager.newProductAttributeDesc("dmStateSalesTaxJurisdiction", 
                                                                         "State Sales Tax Jurisdiction for DWCode", 
                                                                         null, 
                                                                         AttributeType.ENUM,
                                                                         m_stateTaxJurisdictions,
                                                                         false, 
                                                                         Action.ALL_ACTIONS_MASK,
                                                                         "If this member is subject to state sales tax for DWCodes, enter the jurisdiction here",
                                                                         new AttributeValidator[]{});
        dmAccountAttributes.add(m_productManager.newProductAttributeDesc("dmStateSalesTaxRate", 
                                                                         "State Sales Tax Percentage for DWCode", 
                                                                         null,
                                                                         AttributeType.STRING,
                                                                         false, 
                                                                         "If this member is subject to state sales tax for DWCodes, enter the percentage rate here (e.g., 5.3 if the tax rate is 5.3%)",
                                                                         new AttributeValidator[]{new DecimalValidator(), new OtherAttributeRequiredValidator(dmStateSalesTaxJurisdiction)}));
        dmAccountAttributes.add(dmStateSalesTaxJurisdiction);
        AttributeDesc dmLocalSalesTaxJurisdiction = m_productManager.newProductAttributeDesc("dmLocalSalesTaxJurisdiction", 
                                                                         "Local Sales Tax Jurisdiction for DWCode", 
                                                                         null,
                                                                         AttributeType.ENUM,
                                                                         m_localTaxJurisdictions,
                                                                         false, 
                                                                         Action.ALL_ACTIONS_MASK,
                                                                         "If this member is subject to local sales tax for DWCodes, enter the jurisdiction here",
                                                                         new AttributeValidator[]{});
        dmAccountAttributes.add(m_productManager.newProductAttributeDesc("dmLocalSalesTaxRate", 
                                                                         "Local Sales Tax Percentage for DWCode", 
                                                                         null,
                                                                         AttributeType.STRING,
                                                                         false, 
                                                                         "If this member is subject to local sales tax for DWCodes, enter the percentage rate here (e.g., 5.3 if the tax rate is 5.3%)",
                                                                         new AttributeValidator[]{new DecimalValidator(), new OtherAttributeRequiredValidator(dmLocalSalesTaxJurisdiction)}));
        dmAccountAttributes.add(dmLocalSalesTaxJurisdiction);
        */
        m_dmLicenseTypeAttribute = m_productManager.newProductAttributeDesc("dmLicenseType", 
                                                                         "DWCode License Type", 
                                                                         null,
                                                                         null,
                                                                         AttributeType.ENUM,
                                                                         m_dmLicenseTypes,
                                                                         true, 
                                                                         Action.ALL_ACTIONS_MASK,
                                                                         "The license type agreed with Digimarc",
                                                                         new AttributeValidator[]{new RequiredAttributeValidator()});
        dmAccountAttributes.add(m_dmLicenseTypeAttribute);
        
        List<AttributeDesc> dmAttributes = new ArrayList<>();
        dmAttributes.add(m_productManager.newProductAttributeDesc("dmExperienceUrl", "DWCode Experience URL", new String[]{"dmexperienceurl", "dwcode experience url"}, null, AttributeType.WEBURL, false, 
                                                         //"A web URL to which the user will be directed upon scanning the DWCode. If omitted, a page constructed from the product data will be shown instead.",
                                                                  "A mobile-optimized URL that users will view upon scanning the DWCode with an enabled mobile device. You may leave this field blank and update at any time later. If omitted, users will view a web page constructed from the product data you entered.",
                                                                  new AttributeValidator[]{new LengthAttributeValidator(512), new UrlValidator(), new WhitelistedCharValidator()}));
        dmAttributes.add(m_productManager.newProductAttributeDesc("dmUPCE", "Check this box if your product is marked with a UPC-E bar code instead of an ordinary UPC-A. ([[What's this? | /ui/upce]])", new String[]{"dmupce", "upce"}, null,
                                                                  AttributeType.BOOLEAN, false, 
                                                                  null, new AttributeValidator[]{new UPCEValidator()}));
        dmAttributes.add(m_productManager.newProductAttributeDesc("dmChargesAck", "By checking this box you agree to pay a license fee plus a one-time fee associated with enhancing your packaging by applying the DWCode. [[Read more | https://sites.gs1us.org/mobilescan/gettingstarted]] details about the packaging enhancement process and pricing.", null, null,
                                                                  AttributeType.AFFIRMATION, null, true, Action.mask(Action.CREATE, Action.RENEW),
                                                                  null, new AttributeValidator[]{new AffirmationValidator()}));
        AttributeDesc dmStartDateAttribute = m_productManager.newProductAttributeDesc("dmStartDate", "Start Date", null, null, AttributeType.DATE, false, 
                                                                                    "The date upon which the DWCode for this GTIN becomes operational. "
                                                                                            + "Set this in the future if you want to register a product before it is ready for DWCode use.",
                                                                                            new AttributeValidator[]{new DateValidator()});
        AttributeDesc dmEndDateAttribute = m_productManager.newProductAttributeDesc("dmEndDate", "End Date", null, null, AttributeType.DATE, false, 
                                                                                    "The date upon which the DWCode for this GTIN ceases to operate. "
                                                                                            + "Leave blank if the DWCode for this GTIN should be available indefinitely (subject to renewal).",
                                                                                            new AttributeValidator[]{new DateValidator()});
        m_dmPaidThruAttribute = m_productManager.newProductAttributeDesc("dmPaidThru", "Paid Through", null, null, AttributeType.DATE, false, 
                                                                                "The date through which the DWCode for this GTIN has been paid.",
                                                                                        new AttributeValidator[]{new DateValidator()});
        m_dmPendingPaidThruAttribute = m_productManager.newProductAttributeDesc("dmPendingPaidThru", "Paid Through (pending)", null, null, AttributeType.DATE, false, 
                                                                                            "The pending date through which the DWCode for this GTIN has been paid.",
                                                                                                    new AttributeValidator[]{new DateValidator()});
    
        AttributeDesc dmSelectionAttribute = m_productManager.newProductAttributeDesc(DWCODE_APP_NAME, "DWCode enabled", null, null, AttributeType.BOOLEAN, true, "Select to enable DWCode for this GTIN", null);
    
        OrderableItemDesc dmAnnualItemDesc = 
                new OrderableItemDesc(DWCODE_APP_NAME, "DWCode for GTIN {0}", new Amount(50, "USD"));
        /*
        OrderableItemDesc dmProRateItemDesc = 
                new OrderableItemDesc(DWCODE_APP_NAME + "-refund", "Pro-rata refund of DWCode for GTIN {0}", null);
        */
        
        m_commerceManager.addOrderableItemDesc(dmAnnualItemDesc);
        //m_commerceManager.addOrderableItemDesc(dmProRateItemDesc);
        m_quoter.addTaxer(dmAnnualItemDesc.getItemCode(), new Taxer("dmStateSalesTaxRate", "dmStateSalesTaxJurisdiction"));
        m_quoter.addTaxer(dmAnnualItemDesc.getItemCode(), new Taxer("dmLocalSalesTaxRate", "dmLocalSalesTaxJurisdiction"));
        /*
        m_quoter.addTaxer(dmProRateItemDesc.getItemCode(), new Taxer("dmStateSalesTaxRate", "dmStateSalesTaxJurisdiction"));
        m_quoter.addTaxer(dmProRateItemDesc.getItemCode(), new Taxer("dmLocalSalesTaxRate", "dmLocalSalesTaxJurisdiction"));
        */
        
        AppDescImpl dmAppDesc = new AppDescImpl(DWCODE_APP_NAME, 
                                       "DWCode", 
                                       DWCODE_APP_NAME.toLowerCase(), 
                                       "The GS1 US Mobile Scan solution leverages the DWCode, an imperceptible barcode. When scanned with a smartphone app it provides product information or your own custom content. Consumers receive an engaging consumer experience by interacting with the product this way. Trading partners realize business benefits such as faster retail checkout and improved supply chain efficiency.", 
                                       new ModuleDescImpl(null, null, null, null, null,
                                                          null,
                                                          dmAccountAttributes,
                                                          new FreePricer()),
                                       null,
                                       new ModuleDescImpl(dmSelectionAttribute, 
                                                          dmStartDateAttribute, dmEndDateAttribute, 
                                                          m_dmPaidThruAttribute, m_dmPendingPaidThruAttribute,
                                                          null,
                                                          dmAttributes,
                                                          new DMPricer(dmAnnualItemDesc, /* dmProRateItemDesc */ null)));
        return dmAppDesc;
    }

    private AppDescImpl initCloudPrototypeApp()
    {
        List<AttributeDesc> cpPkAttributes = new ArrayList<>();
        cpPkAttributes.add(m_productManager.newProductAttributeDesc("gtin", "GTIN", new String[]{"gtin"}, null, AttributeType.STRING, true, 
                                                                       "The GTIN of the product, as printed on the package.",
                                                                       new AttributeValidator[]{new RequiredAttributeValidator()}));

        List<AttributeDesc> cpAttributes = new ArrayList<>();
        cpAttributes.add(m_productManager.newProductAttributeDesc("brandOwnerName", "Brand Owner Name", new String[]{"brand owner name", "brandownername", "brandowner", "brand owner"}, "Basic product info", AttributeType.STRING, true, 
                                                                  "The name of the brand owner of the product, as printed on the package.",
                                                                  new AttributeValidator[]{new RequiredAttributeValidator(), new WhitelistedCharValidator()}));
        cpAttributes.add(m_productManager.newProductAttributeDesc("brandName", "Brand Name", new String[]{"brand name", "brandname", "brand"}, "Basic product info", AttributeType.STRING, true, 
                                                         "The brand of the product, as printed on the package.",
                                                         new AttributeValidator[]{new RequiredAttributeValidator(), new LengthAttributeValidator(70), new WhitelistedCharValidator()}));
        
        cpAttributes.add(m_productManager.newProductAttributeDesc("subBrand", "Sub-brand Name", new String[]{"subbrand", "sub-brand", "subbrand name", "sub-brand name"}, "Basic product info", AttributeType.STRING, false, 
                "The sub-brand of the product, as printed on the package. Required when the product package includes both a brand name and a"
                + " sub brand within the main brand. Leave blank if not applicable.",
                new AttributeValidator[]{new LengthAttributeValidator(70), new WhitelistedCharValidator()}));
                
        cpAttributes.add(m_productManager.newProductAttributeDesc("functionalName", "Functional Product Name", new String[]{"functionalproductname", "functional product name"}, "Basic product info", AttributeType.STRING, false, 
                "Describes use of the product or service by the consumer. Should help clarify the product classification associated with the GTIN.",
                new AttributeValidator[]{new LengthAttributeValidator(35), new WhitelistedCharValidator()}));
        cpAttributes.add(m_productManager.newProductAttributeDesc("tradeItemDescription", "Product Name", new String[]{"tradeitemdescription", "trade item description", "productname", "product name", "name"}, "Basic product info", AttributeType.STRING, true, 
                                                                  "An understandable and useable description of a trade item using brand and other descriptors.",
                                                                  new AttributeValidator[]{new RequiredAttributeValidator(), new LengthAttributeValidator(200), new WhitelistedCharValidator()}));
        cpAttributes.add(m_productManager.newProductAttributeDesc("additionalTradeItemDescription", "Description", new String[]{"additionaltradeitemdescription", "description"}, "Basic product info", AttributeType.STRING, false, 
                "Additional variants necessary to communicate to the industry to help define the product. Multiple variants can be established for each GTIN.",
                new AttributeValidator[]{new LengthAttributeValidator(500), new WhitelistedCharValidator()}));

        AttributeDesc declaredQuantity1Desc = m_productManager.newProductAttributeDesc("netContent1", "Declared Quantity / Net Content 1", 
                                                                                       new String[]{"declaredquantity", "declared quantity", "netcontent", "net content", "declared quantity / net content", "declaredquantity1", "declared quantity 1", "netcontent 1", "net content 1", "declared quantity / net content 1",},
                                                                                       "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK,
                "The first (or only) declared quantity or net content, as printed on the package.",
                new AttributeValidator[]{/*new RequiredAttributeValidator(), */new MeasurementAttributeValidator()});
        cpAttributes.add(declaredQuantity1Desc);
        AttributeDesc declaredQuantity2Desc = m_productManager.newProductAttributeDesc("netContent2", "Declared Quantity / Net Content 2",  
                                                                                       new String[]{"declaredquantity2", "declared quantity 2", "netcontent 2", "net content 2", "declared quantity / net content 2"},
                                                                                       "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK, 
                                                                     "The second declared quantity or net content, as printed on the package. Required if the package indicates two or more declared quantity or net content values.",
                                                                     new AttributeValidator[]{new MeasurementAttributeValidator(), new OtherAttributeRequiredValidator(declaredQuantity1Desc)});
        cpAttributes.add(declaredQuantity2Desc);
        AttributeDesc declaredQuantity3Desc = m_productManager.newProductAttributeDesc("netContent3", "Declared Quantity / Net Content 3",  
                                                                                       new String[]{"declaredquantity3", "declared quantity 3", "netcontent 3", "net content 3", "declared quantity / net content 3"},
                                                                                       "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK, 
                                                                     "The third declared quantity or net content, as printed on the package. Required if the package indicates three or more declared quantity or net content values.",
                                                                     new AttributeValidator[]{new MeasurementAttributeValidator(), new OtherAttributeRequiredValidator(declaredQuantity2Desc)});
        cpAttributes.add(declaredQuantity3Desc);
        cpAttributes.add(m_productManager.newProductAttributeDesc("netContent4", "Declared Quantity / Net Content 4",  
                                                                                       new String[]{"declaredquantity4", "declared quantity 4", "netcontent 4", "net content 4", "declared quantity / net content 4"},
                                                                                       "Declared quantity and/or net content", AttributeType.MEASUREMENT, m_uoms, false, Action.ALL_ACTIONS_MASK, 
                                                                     "The fourth declared quantity or net content, as printed on the package. Required if the package indicates four or more declared quantity or net content values.",
                                                                     new AttributeValidator[]{new MeasurementAttributeValidator(), new OtherAttributeRequiredValidator(declaredQuantity3Desc)}));
 
       cpAttributes.add(m_productManager.newProductAttributeDesc("descriptiveSize", "Size", new String[]{"descriptivesize", "descriptive size", "size"}, "Product characteristics", AttributeType.STRING, false, 
                                                         "The size of the product, or similar distinguishing characteristic, as printed on the package. This or \"flavor or color\" is required if needed to"
                                                         + " distinguish multiple products that share the same functional product name. Unlike \"declared quantity / net content\", this attribute "
                                                         + "is free text, often something like S, M, L, XL, etc. Leave blank if not applicable.",
                                                         new AttributeValidator[]{new LengthAttributeValidator(500), new WhitelistedCharValidator()}));
       cpAttributes.add(m_productManager.newProductAttributeDesc("gpcCategoryCode", "GPC Category", new String[]{"gpccategorycode", "gpc category code", "gpc category", "gpccategory", "gpc"}, "Product classification", AttributeType.STRING, true, 
                                                                 "The 8-digit GPC category code for the product",
                                                                 new AttributeValidator[]{new RequiredAttributeValidator(), new LengthAttributeValidator(8, 8), new DigitOnlyAttributeValidator()}));
       cpAttributes.add(m_productManager.newProductAttributeDesc("informationProviderGLN", "Information Provider", new String[]{"informationprovidergln", "information provider gln", "information provider", "informationprovider"}, "Product info metadata", AttributeType.STRING, true, 
                                                                 "The Global Location Number (GLN) of the party responsible for populating the database entry for this item",
                                                                 new AttributeValidator[]{new RequiredAttributeValidator(), new LengthAttributeValidator(13, 13), new DigitOnlyAttributeValidator(), new GLNAttributeValidator()}));
       cpAttributes.add(m_productManager.newProductAttributeDesc("itemDataLanguage", "Language Code", new String[]{"language code", "languagecode", "item data language", "itemdatalanguage"}, "Product info metadata", AttributeType.STRING, true, 
                                                                 "The language of the item data, as an ISO 2-letter language code. Example: English=en",
                                                                 new AttributeValidator[]{new RequiredAttributeValidator(), new LengthAttributeValidator(2,2)}));
       cpAttributes.add(m_productManager.newProductAttributeDesc("uriProductImage", "Product Image URL", new String[]{"uriproductimage", "uri product image", "product image url", "productimageurl"}, "Hyperlinks", AttributeType.WEBURL, false, 
                                                                 "A publicly-accessible Web URL of an image of the product. "
                                                                 + "This URL should be for an image (jpg, png, etc), not for a web page (html).",
                                                                 new AttributeValidator[]{new LengthAttributeValidator(2500), new UrlValidator(), new WhitelistedCharValidator()}));
       cpAttributes.add(m_productManager.newProductAttributeDesc("uriProductWebsite", "Product Website URL", new String[]{"uriproductwebsite", "uri product website", "productwebsiteurl", "product website url"}, "Hyperlinks", AttributeType.WEBURL, false, 
                                                                 "A publicly-accessible Web URL of a webpage appropriate for the product. "
                                                                 + "This URL should be for a web page (html), not for an image (jpg, png, etc).",
                                                                 new AttributeValidator[]{new LengthAttributeValidator(2500), new UrlValidator(), new WhitelistedCharValidator()}));
       AttributeDesc cpStartDateAttribute = m_productManager.newProductAttributeDesc("cpStartDate", "Start Date (MM/DD/YYYY)", new String[]{"start date", "startdate"}, "Start date", AttributeType.DATE, false, 
                                                                                    "The date upon which the GTIN and product data is available for consumers to use. "
                                                                                            + "Set this in the future if you want to enter a product before it is ready to enter the market.",
                                                                                            new AttributeValidator[]{new DateValidator()});
        cpAttributes.add(cpStartDateAttribute);
        AttributeDesc cpEndDateAttribute = m_productManager.newProductAttributeDesc("cpEndDate", "End Date", null, null, AttributeType.DATE, false, 
                                                                                    "The date upon which the GTIN and product data is no longer available for consumers to use. "
                                                                                            + "Leave blank if the data should be available indefinitely.",
                                                                                            new AttributeValidator[]{new DateValidator()});
        AttributeDesc cpPaidThruAttribute = m_productManager.newProductAttributeDesc("cpPaidThru", "Paid Through", null, null, AttributeType.DATE, false, 
                                                                                            "The date through which cp product data is valid.",
                                                                                                    new AttributeValidator[]{new DateValidator()});
        AttributeDesc cpPendingPaidThruAttribute = m_productManager.newProductAttributeDesc("cpPendingPaidThru", null, null, "Paid Through (pending)", AttributeType.DATE, false, 
                                                                                                        "The pending date through which cp product data is valid.",
                                                                                                                new AttributeValidator[]{new DateValidator()});
        List<AttributeDesc> cpAccountAttributes = new ArrayList<>();

        
        return new AppDescImpl("cp", 
                                         "cp product info", 
                                         "gs1", 
                                         "GS1 Cloud Prototype product attributes, published by GS1, free for use to all", 
                                         new ModuleDescImpl(null, null, null, null, null,
                                                            null,
                                                            cpAccountAttributes,
                                                            new FreePricer()),
                                         null,
                                         new ModuleDescImpl(null, 
                                                            cpStartDateAttribute, cpEndDateAttribute, 
                                                            cpPaidThruAttribute, cpPendingPaidThruAttribute,
                                                            cpPkAttributes,
                                                            cpAttributes,
                                                            new FreePricer()));
    }
    
    private static class FreePricer implements Pricer
    {
        @Override
        public OrderableItemDesc getAnnualItemDesc(GBAccount gbAccount)
        {
            return null;
        }

        @Override
        public OrderableItemDesc getProRateItemDesc(GBAccount gbAccount)
        {
            return null;
        }
        
    }
    
    private static class DMPricer implements Pricer
    {
        private OrderableItemDesc m_annualItemDesc;
        private OrderableItemDesc m_proRateItemDesc;
        
        
        public DMPricer(OrderableItemDesc annualItemDesc,
                OrderableItemDesc proRateItemDesc)
        {
            super();
            m_annualItemDesc = annualItemDesc;
            m_proRateItemDesc = proRateItemDesc;
        }


        @Override
        public OrderableItemDesc getAnnualItemDesc(GBAccount gbAccount)
        {
            if (isExemptFromLicenseFees(gbAccount))
                return null;
            else
                return m_annualItemDesc;
        }

        @Override
        public OrderableItemDesc getProRateItemDesc(GBAccount gbAccount)
        {
            if (isExemptFromLicenseFees(gbAccount))
                return null;
            else
                return m_proRateItemDesc;
        }
        
        private boolean isExemptFromLicenseFees(GBAccount gbAccount)
        {
            AttributeSet attributes = gbAccount.getAttributes();
            if (attributes == null)
                return false;
            else
            {
                String licenseType = attributes.getAttribute("dmLicenseType");
                if ("Enterprise".equals(licenseType) || "DMCustomer".equals(licenseType))
                    return true;
                else
                    return false;
            }
        }
        
    }
    
    private void initUoms()
    {
        m_uoms = new ArrayList<>();
        m_uoms.add(new AttributeEnumValueImpl("X_1N", "Count"));
        m_uoms.add(new AttributeEnumValueImpl("FTQ", "Cubic foot"));
        m_uoms.add(new AttributeEnumValueImpl("MMQ", "Cubic millimetre"));
        m_uoms.add(new AttributeEnumValueImpl("G21", "Cup (US)"));
        m_uoms.add(new AttributeEnumValueImpl("E27", "Dose"));
        m_uoms.add(new AttributeEnumValueImpl("DZN", "Dozen"));
        m_uoms.add(new AttributeEnumValueImpl("PTD", "Dry Pint (US)"));
        m_uoms.add(new AttributeEnumValueImpl("EA",  "Each"));
        m_uoms.add(new AttributeEnumValueImpl("OZA", "Fluid ounce (US)"));
        m_uoms.add(new AttributeEnumValueImpl("FOT", "Foot"));
        m_uoms.add(new AttributeEnumValueImpl("GLL", "Gallon (US)"));
        m_uoms.add(new AttributeEnumValueImpl("GRM", "Gram"));
        m_uoms.add(new AttributeEnumValueImpl("INH", "Inches"));
        m_uoms.add(new AttributeEnumValueImpl("KGM", "Kilogram"));
        m_uoms.add(new AttributeEnumValueImpl("PTL", "Liquid pint (US)"));
        m_uoms.add(new AttributeEnumValueImpl("QTL", "Liquid quart (US)"));
        m_uoms.add(new AttributeEnumValueImpl("LTR", "Litre"));
        m_uoms.add(new AttributeEnumValueImpl("MTR", "Metre"));
        m_uoms.add(new AttributeEnumValueImpl("MGM", "Milligram"));
        m_uoms.add(new AttributeEnumValueImpl("MLT", "Millilitre"));
        m_uoms.add(new AttributeEnumValueImpl("MMT", "Millimetre"));
        m_uoms.add(new AttributeEnumValueImpl("NIU", "Number of International Units"));
        m_uoms.add(new AttributeEnumValueImpl("ONZ", "Ounce"));
        m_uoms.add(new AttributeEnumValueImpl("H87", "Piece"));
        m_uoms.add(new AttributeEnumValueImpl("LBR", "Pound"));
        m_uoms.add(new AttributeEnumValueImpl("QTD", "Quart (US dry)"));
        m_uoms.add(new AttributeEnumValueImpl("FTK", "Square foot"));
        m_uoms.add(new AttributeEnumValueImpl("INK", "Square inch"));
        m_uoms.add(new AttributeEnumValueImpl("MTK", "Square metre"));
        m_uoms.add(new AttributeEnumValueImpl("MMK", "Square millimetre"));
        m_uoms.add(new AttributeEnumValueImpl("YDK", "Square Yard"));
        m_uoms.add(new AttributeEnumValueImpl("E55", "Use"));
        m_uoms.add(new AttributeEnumValueImpl("YRD", "Yard"));

        
    }
    
    private void initStateTaxJurisdictions()
    {
        m_stateTaxJurisdictions = new ArrayList<>();
        m_stateTaxJurisdictions.add(new AttributeEnumValueImpl("CT", "Connecticut"));
        m_stateTaxJurisdictions.add(new AttributeEnumValueImpl("NY", "New York"));
        m_stateTaxJurisdictions.add(new AttributeEnumValueImpl("OH", "Ohio"));
        m_stateTaxJurisdictions.add(new AttributeEnumValueImpl("WA", "Washington"));
    }
    
    private void initLocalTaxJurisdictions()
    {
        m_localTaxJurisdictions = new ArrayList<>();
        m_localTaxJurisdictions.add(new AttributeEnumValueImpl("Chicago", "Chicago, IL"));
     }

    private void initDmLicenseTypes()
    {
        m_dmLicenseTypes = new ArrayList<>();
        m_dmLicenseTypes.add(new AttributeEnumValueImpl("50PerGtinCC", "GS1 US Customer, $50 per GTIN, Payment by CC"));
        m_dmLicenseTypes.add(new AttributeEnumValueImpl("50PerGtinCheck", "GS1 US Customer, $50 per GTIN, Payment by check or PO"));
        m_dmLicenseTypes.add(new AttributeEnumValueImpl("Enterprise", "GS1 US Customer, Enterprise License"));
        m_dmLicenseTypes.add(new AttributeEnumValueImpl("DMCustomer", "Digimarc Customer (no GS1 portal access)"));
     }


    private void updateNextActionDates(ProductRecord productRecord)
    {
        boolean firstNextActionDate = true;
        Date nextActionDate = null;
        Date pendingNextActionDate = null;
        for (AppDesc appDesc : m_appDescs)
        {
            if (((AppDescImpl)appDesc).isSelected(productRecord))
            {
                AttributeDesc paidThruDesc = appDesc.getProductModuleDesc().getPaidThruDateAttribute();
                AttributeDesc pendingPaidThruDesc = appDesc.getProductModuleDesc().getPendingPaidThruDateAttribute();

                Date paidThru = productRecord.getAttributes().getDateAttribute(paidThruDesc);
                Date pendingPaidThru = productRecord.getAttributes().getDateAttribute(pendingPaidThruDesc);
                
                // TODO: make this more robust?
                // If both paidThru and pendingPaidThru are null, then this must not be a subscribed app
                if (!(paidThru == null && pendingPaidThru == null))
                {
                    if (firstNextActionDate)
                    {
                        nextActionDate = paidThru;
                        if (nextActionDate != null)
                            firstNextActionDate = false;
                    }
                    else
                        nextActionDate = DateUtil.dateMin(nextActionDate, paidThru, true);
                    
                    pendingNextActionDate = DateUtil.dateMin(pendingNextActionDate, pendingPaidThru, false);
                }
            }
        }
        productRecord.setNextActionDate(nextActionDate);
        productRecord.setPendingNextActionDate(pendingNextActionDate);
    }

    private class NextActionDateHandler implements ProductEventHandler
    {

        @Override
        public void handleCreate(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord)
        {
            updateNextActionDates(productRecord);
        }

        @Override
        public void handleUpdate(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord)
        {
            updateNextActionDates(productRecord);
        }

        @Override
        public void handleDelete(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord)
        {
            // Do nothing
        }
        
    }
    
    private class DWCodeEventHandler implements CommerceEventHandler
    {
        private Map<String,OrderStatus> m_paidThruTransitionStatusMap = new HashMap<>();

        
        public void add(String accountType, OrderStatus paidThruTransitionStatus)
        {
            m_paidThruTransitionStatusMap.put(accountType, paidThruTransitionStatus);
        }


        @Override
        public void handle(OrderStatus status, Date date, OrderLineItem lineItem, SalesOrderRecord salesOrder, InvoiceRecord invoice, PaymentRecord payment)
        {
            String gln = salesOrder.getGBAccountGln();
            GBAccount gbAccount = m_gbDao.getGBAccountByGln(gln);
            OrderStatus paidThruTransitionStatus = OrderStatus.ORDERED;
            AttributeSet accountAttributes = gbAccount.getAttributes();
            if (accountAttributes != null)
            {
                String licenseType = accountAttributes.getAttribute(m_dmLicenseTypeAttribute);
                OrderStatus nonDefaultPaidThruTransitionStatus = m_paidThruTransitionStatusMap.get(licenseType);
                if (nonDefaultPaidThruTransitionStatus != null)
                    paidThruTransitionStatus = nonDefaultPaidThruTransitionStatus;
            }
            
            if (status != OrderStatus.ORDERED && status != OrderStatus.PAID && status != paidThruTransitionStatus)
                return;
            
            try
            {
                String gtin = lineItem.getItemParameters()[0];
                ProductRecord productRecord = (ProductRecord) m_productManager.getProductByGtin(gbAccount, gtin);
                if (productRecord == null)
                {
                    // TODO: log this - shouldn't happen! (Unless somebody uses the non-production deleteProduct() operation)
                    return;
                }
                AttributeSet attributes = productRecord.getAttributes();

                switch (status)
                {
                case ORDERED:
                    productRecord.addPendingOrderId(salesOrder.getOrderId());
                    break;
                    
                case PAID:
                    productRecord.removePendingOrderId(salesOrder.getOrderId());
                    break;
                }
                if (status == paidThruTransitionStatus)
                {
                    Date paidThruDate = attributes.getDateAttribute(m_dmPaidThruAttribute);
                    Date pendingPaidThruDate = attributes.getDateAttribute(m_dmPendingPaidThruAttribute);
                    if (pendingPaidThruDate != null)
                    {
                        if (paidThruDate == null)
                        {
                            // In this case, this is the initial payment for the GTIN. Use one year from now, rather than
                            // the pending date which was calculated as one year from date of order.
                            pendingPaidThruDate = DateUtil.oneYearLater(DateUtil.roundToDay(m_clockService.now()));
                            
                            // Also bump up the paid thru date for the basic module
                            AttributeDesc basicPaidThruAttribute = m_appDescs.get(0).getProductModuleDesc().getPaidThruDateAttribute();
                            Date oldBasicPaidThru = attributes.getDateAttribute(basicPaidThruAttribute);
                            if (oldBasicPaidThru != null)
                            {
                                attributes.setDateAttribute(basicPaidThruAttribute, pendingPaidThruDate);
                            }
                        }
                        attributes.setDateAttribute(m_dmPaidThruAttribute, pendingPaidThruDate);
                        attributes.setAttribute(m_dmPendingPaidThruAttribute, null);
                        //updateNextActionDates(productRecord);
                    }
                }
                // If status == ORDERED, product is already being updated as part of an API createProduct or updateProduct request.
                if (status != OrderStatus.ORDERED)
                    m_productManager.internalUpdateProduct(null, null, gbAccount, productRecord, null);
                   
                //m_gbDao.updateProduct(productRecord);
            }
            catch (GlobalBrokerException e)
            {
                // TODO: what?
            }

        }
        
    }

    private class DWCodeEventHandlerPendingUntilPaid implements CommerceEventHandler
    {
        private OrderStatus m_paidThruTransitionStatus;
        
        
        public DWCodeEventHandlerPendingUntilPaid(OrderStatus paidThruTransitionStatus)
        {
            super();
            m_paidThruTransitionStatus = paidThruTransitionStatus;
        }


        @Override
        public void handle(OrderStatus status, Date date, OrderLineItem lineItem, SalesOrderRecord salesOrder, InvoiceRecord invoice, PaymentRecord payment)
        {
            if (status != OrderStatus.ORDERED && status != OrderStatus.PAID && status != m_paidThruTransitionStatus)
                return;
            
            try
            {
                String gln = salesOrder.getGBAccountGln();
                String gtin = lineItem.getItemParameters()[0];
                GBAccount gbAccount = m_gbDao.getGBAccountByGln(gln);
                ProductRecord productRecord = (ProductRecord) m_productManager.getProductByGtin(gbAccount, gtin);
                if (productRecord == null)
                {
                    // TODO: log this - shouldn't happen!
                    return;
                }
                AttributeSet attributes = productRecord.getAttributes();

                switch (status)
                {
                case ORDERED:
                    productRecord.addPendingOrderId(salesOrder.getOrderId());
                    break;
                    
                case PAID:
                    productRecord.removePendingOrderId(salesOrder.getOrderId());
                    break;
                }
                if (status == m_paidThruTransitionStatus)
                {
                    Date pendingPaidThruDate = attributes.getDateAttribute(m_dmPendingPaidThruAttribute);
                    attributes.setDateAttribute(m_dmPaidThruAttribute, pendingPaidThruDate);
                    attributes.setAttribute(m_dmPendingPaidThruAttribute, null);
                    /*
                    updateNextActionDates(productRecord);
                    */
                    m_productManager.internalUpdateProduct(null, null, gbAccount, productRecord, null);
                }
                m_gbDao.updateProduct(productRecord);
            }
            catch (GlobalBrokerException e)
            {
                // TODO: what?
            }

        }
        
    }

    public List<AppDesc> getAppDescs()
    {
        return m_appDescs;
    }
    
    public AppDesc getAppDesc(String appName)
    {
        for (AppDesc appDesc : m_appDescs)
        {
            if (appDesc.getName().equals(appName))
                return appDesc;
        }
        return null;
    }

    public Collection<? extends AppSubscription> getAppSubscriptions(GBAccount gbAccount, boolean includeAppDescs) throws GlobalBrokerException
    {
        Collection<? extends AppSubscriptionRecord> records = m_gbDao.getAppSubscriptionsByGln(gbAccount.getGln());
        List<AppSubscription> result = new ArrayList<>(records.size());
        for (AppSubscriptionRecord record : records)
        {
            String appId = record.getAppId();
            final AppDesc appDesc = getAppDesc(appId);
            if (appDesc != null)
            {
                AppSubscription sub = appDescToSubscription(record, appDesc, includeAppDescs);
                result.add(sub);
            }
        }
        // TODO: some better criterion for the ordering, and perhaps do this in the DAO call?
        Collections.sort(result, new Comparator<AppSubscription>(){
            @Override
            public int compare(AppSubscription o1, AppSubscription o2)
            {
                return o1.getAppName().compareTo(o2.getAppName());
            }
        });
        return result;
    }


    private AppSubscription appDescToSubscription(final AppSubscriptionRecord record, final AppDesc appDesc, final boolean includeAppDescs)
    {
        return new AppSubscription()
        {
            @Override
            public String getAppName()
            {
                return appDesc.getName();
            }
            

            @Override
            public AppDesc getAppDesc()
            {
                if (includeAppDescs)
                    return appDesc;
                else
                    return null;
            }


            @Override
            public Object getAppArgs()
            {
                return null;
            }


            @Override
            public String getSubscribedByAgentUsername()
            {
                return record.getSubscribedByAgentUsername();
            }


            @Override
            public String getSubscribedByUsername()
            {
                return record.getSubscribedByUsername();
            }


            @Override
            public Date getSubscriptionDate()
            {
                return record.getSubscriptionDate();
            }
        };
    }
    
    public void createAppSubscription(AgentUser agentUser, String username, GBAccount gbAccount, AppSubscription subscription) throws GlobalBrokerException
    {
        String gln = gbAccount.getGln();
        String appId = subscription.getAppName();
        AppSubscriptionRecord record = m_gbDao.getAppSubscription(gln, appId);
        if (record == null)
        {
            // A unique index on gln,appid protects us from a race that could occur under extremely rare circumstances
            record = m_gbDao.newAppSubscription();
            record.setGBAccountGln(gln);
            record.setAppId(appId);
            record.setSubscribedByAgentUsername(agentUser.getUsername());
            record.setSubscribedByUsername(username);
            Date date = m_clockService.now();
            record.setSubscriptionDate(date);

            // Do an update so that we'll get an exception if this is a duplicate request due to race
            m_gbDao.updateAppSubscription(record);
 

            AppEventHandler handler = m_appEventHandlerMap.get(appId);
            if (handler != null)
                handler.handleSubscribe(agentUser, username, gbAccount, record);

            // Update again in case the app handler updated the record
            m_gbDao.updateAppSubscription(record);

            AuditEventRecord event = m_gbDao.newAuditEvent();
            event.setType(AuditEventType.CREATE_APP_SUBSCRIPTION);
            event.setDate(date);
            event.setAgentUsername(agentUser.getUsername());
            event.setUsername(username);
            event.setGBAccountGln(gbAccount.getGln());
            m_gbDao.updateAuditEvent(event);
        }
        return;
    }

    public Quotation validateTerms(GBAppContext appContext, Product newProduct, Product oldProduct, boolean renew, List<ProductValidationError> validationErrors) throws GlobalBrokerException
    {
        Date today = m_clockService.now();
        
        return m_quoter.validateTerms(appContext, newProduct, oldProduct, renew, validationErrors, today);
    }


}
