package org.gs1us.sgg.webapi;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.gs1us.sgg.account.AccountManager;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.ConflictException;
import org.gs1us.sgg.gbservice.api.DuplicateAccountException;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GBIllegalArgumentException;
import org.gs1us.sgg.gbservice.api.GBIllegalStateException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerServiceException;
import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceException;
import org.gs1us.sgg.gbservice.api.IsoCountryRef;
import org.gs1us.sgg.gbservice.api.NoSuchAccountException;
import org.gs1us.sgg.gbservice.api.NoSuchAppException;
import org.gs1us.sgg.gbservice.api.NoSuchInvoiceException;
import org.gs1us.sgg.gbservice.api.NoSuchPaymentException;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;
import org.gs1us.sgg.gbservice.api.PaymentException;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgg.gbservice.api.UploadValidationProduct;
import org.gs1us.sgg.gbservice.api.ValidationException;
import org.gs1us.sgg.gbservice.impl.GlobalBrokerServiceImpl;
import org.gs1us.sgg.gbservice.json.ExceptionInfo;
import org.gs1us.sgg.gbservice.json.InboundAppSubscription;
import org.gs1us.sgg.gbservice.json.InboundBillingInfo;
import org.gs1us.sgg.gbservice.json.InboundGBAccount;
import org.gs1us.sgg.gbservice.json.InboundImportPrevalidationSegmentSettings;
import org.gs1us.sgg.gbservice.json.InboundOrderIdsAndExtras;
import org.gs1us.sgg.gbservice.json.InboundPayInvoicesInfo;
import org.gs1us.sgg.gbservice.json.InboundProduct;
import org.gs1us.sgg.gbservice.json.InboundProductAndPo;
import org.gs1us.sgg.gbservice.json.InboundProductAttribute;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgg.dao.memberservice.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Transactional
@RequestMapping("/api")
public class ApiController
{
    private static final Logger s_logger = Logger.getLogger("org.gs1us.sgg.webapi.ApiController");
    @Resource
    private GlobalBrokerServiceImpl m_gbServiceImpl;
    
    @Resource
    private AccountManager m_accountManager;
    
    private String findUsername(String username) throws GBIllegalArgumentException
    {
        String trimmed = UserInputUtil.trimToNull(username);
        // TODO: reject if null
        return trimmed;
    }
    
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public String versionGet(Model model, Principal principal) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);

        return m_gbServiceImpl.getVersion(agentUser);
    }
    
    @RequestMapping(value = "/account/{gln}/apps", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends AppDesc> accountAppsGet(Model model, Principal principal, @PathVariable String gln) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        Collection<? extends AppDesc> appDescs = m_gbServiceImpl.getAppDescs(agentUser, gln);

        return appDescs;
    }
    
    
    private AgentUser findAgentUser(Principal principal)
    {
        if (principal == null)
            return null;
        else
            return (AgentUser)((Authentication)principal).getPrincipal();
    }

    private Member getMember(Principal principal)
    {
        if (principal == null)
            return null;
        else
        {
            AgentUser user = getUser(principal);
            Member member = user.getMember();
            if (member == null)
                return null;
            else
                return member;
        }
    }    
     
    
    private AgentUser getUser(Principal principal)
    {
        if (principal == null)
            return null;
        else
            return (AgentUser)((Authentication)principal).getPrincipal();
    }    
    
    /**
     * Returns the GLN of a user for user-level GB actions. Null if the brand owner agreement has not yet been signed.
     * @param principal
     * @return
     * @throws GlobalBrokerException
     */
    private String getGBAccountGln(Principal principal) throws GlobalBrokerException
    {
        Member member = getMember(principal);
        if (member == null || member.getBrandOwnerAgreementSignedByUser() == null)
            return null;
        else
            return member.getGln();
    }    

    
    @RequestMapping(value = "/account/{gln}", method = RequestMethod.GET)
    @ResponseBody
    public GBAccount accountGet(Model model, Principal principal, @PathVariable String gln) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        GBAccount gbAccount = m_gbServiceImpl.getAccount(agentUser, gln);
        if (gbAccount == null)
            throw new NoSuchAccountException();

        return gbAccount;
    }
    
    @RequestMapping(value = "/account/{gln}", method = RequestMethod.PUT)
    public ResponseEntity<String> accountPut(Model model, Principal principal, 
                                             @PathVariable String gln, 
                                             @RequestParam(required=false) String username,
                                             @RequestParam(required=false) Action action,
                                             @RequestBody InboundGBAccount gbAccount) throws GlobalBrokerException, IOException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        if (gbAccount.getGln() == null)
            gbAccount.setGln(gln);
        else if (!gbAccount.getGln().equals(gln))
            throw new GBIllegalArgumentException("GLN in account does not match GLN in URI");
        
        m_gbServiceImpl.putAccount(agentUser, validatedUsername, gbAccount, action);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/{gln}/subscription", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends AppSubscription> accountSubscriptionGet(Model model, Principal principal, 
                                                                        @PathVariable String gln, 
                                                                        @RequestParam(value="includeAppDescs", required=false) boolean includeAppDescs) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        Collection<? extends AppSubscription> subs = m_gbServiceImpl.getAppSubscriptions(agentUser, gln, includeAppDescs);

        return subs;
    }
    
    @RequestMapping(value = "/account/{gln}/subscription/{appName}", method = RequestMethod.PUT)
    public ResponseEntity<String> accountSubscriptionPut(Model model, Principal principal, 
                                                         @PathVariable String gln, 
                                                         @PathVariable String appName, 
                                                         @RequestParam(required=false) String username,
                                                         @RequestBody InboundAppSubscription subscription) 
                                                                 throws GlobalBrokerException, IOException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        if (subscription.getAppName() == null)
            subscription.setAppName(appName);
        else if (!subscription.getAppName().equals(appName))
            throw new GBIllegalArgumentException("App name in subscription does not match app name in URI");

        
        m_gbServiceImpl.createAppSubscription(agentUser, validatedUsername, gln, subscription);
        return new ResponseEntity<String>(HttpStatus.OK);
 
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends Product> accountProductGet(Model model, Principal principal, @RequestParam(value="gln", required=true) String gln) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        Collection<? extends Product> products = m_gbServiceImpl.getProducts(agentUser, gln);

        return products;
    }
        
    @RequestMapping(value = "/productList", method = RequestMethod.GET)
    @ResponseBody
    public Long productListGet(Model model) throws JsonProcessingException, GlobalBrokerException 
    {        
        Long products = m_gbServiceImpl.getProductsForReport();
        return products;
    }
    
    @RequestMapping(value = "/registeredProductsCount/{gln}", method = RequestMethod.GET)
    @ResponseBody
    public Long registeredProductsCountGet(Model model,
                                         @PathVariable String gln) throws JsonProcessingException, GlobalBrokerException 
    {
        Long products = m_gbServiceImpl.getRegisteredProductsCount(gln);
        return products;
    }    
        
    @RequestMapping(value = "/search/productGpcTmList/{gpc}", method = RequestMethod.GET)
    @ResponseBody
    public Long productGpcTmListGet(Model model,
                                         @PathVariable String gpc,
                                         @RequestParam(value="param", required=true) String param) throws JsonProcessingException, GlobalBrokerException 
    {
        Long products = m_gbServiceImpl.getProductsCountBasedOnGpcAndTargetMarket(gpc, param);
        return products;
    }    
    
    @RequestMapping(value = "/productListByDate", method = RequestMethod.GET)
    @ResponseBody
    public Long productListByDateGet(Model model) throws JsonProcessingException, GlobalBrokerException 
    {        
        Long products = m_gbServiceImpl.getProductsForReportByDate();
        return products;
    }
        
    @RequestMapping(value = "/search/isoCountryList", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends IsoCountryRef> isoCountryListGet(Model model) throws JsonProcessingException, GlobalBrokerException 
    {        
        Collection<? extends IsoCountryRef> countryList = m_gbServiceImpl.getAllIsoCountryRef();
        return countryList;
    }
    
    @RequestMapping(value = "/product/{gtin}", method = RequestMethod.GET)
    @ResponseBody
    public Product accountProductGtinGet(Model model, Principal principal,
                                         @PathVariable String gtin,
                                         @RequestParam(value="gln", required=true) String gln) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        Product product = m_gbServiceImpl.getProductByGtin(agentUser, gln, gtin);

        return product;
    }
    
    @RequestMapping(value = "/search/productBasedOnGpcAndTargetMarket/{gpc}", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends Product> productBasedOnGpcAndTargetMarketGet(Model model,
                                         @PathVariable String gpc,
                                         @RequestParam(value="param", required=true) String param,
                                         @RequestParam(value="startIndex", required=true) String startIndex,
                                         @RequestParam(value="size", required=true) String maxSize) throws JsonProcessingException, GlobalBrokerException 
    {
    	Collection<? extends Product> products = m_gbServiceImpl.getProductsBasedOnGpcAndTargetMarket(gpc, param, startIndex, maxSize);
        return products;
    }    

    @RequestMapping(value = "/search/productsForPagination/{gln}", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends Product> productsForPagination(Model model,
                                         @PathVariable String gln,
                                         @RequestParam(value="startIndex", required=true) String startIndex,
                                         @RequestParam(value="size", required=true) String maxSize) throws JsonProcessingException, GlobalBrokerException 
    {
    	Collection<? extends Product> products = m_gbServiceImpl.getProductsForPagination(gln, startIndex, maxSize);
        return products;
    }    
    
    @RequestMapping(value = "/search/productById/{gtin}", method = RequestMethod.GET)
    @ResponseBody
    public Product accountProductGtinOnlyGet(Model model, @PathVariable String gtin) throws JsonProcessingException, GlobalBrokerException 
    {        
        Product product = m_gbServiceImpl.getProductByGtinOnly(gtin);
        return product;
    }
    
    @RequestMapping(value = "/product/{gtin}/validate", method = RequestMethod.POST)
    @ResponseBody
    public ProductStatus accountProductGtinValidatePost(Model model, Principal principal,
                                             @PathVariable String gtin,
                                             @RequestParam(value="gln", required=true) String gln,
                                             @RequestParam(value="renew", required=false) boolean renew,
                                             @RequestBody InboundProduct product) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        logRequest(agentUser, null, String.format("POST /product/%s/validate", gtin), product);
        
        checkGtinAgreement(gtin, product);
        ProductStatus productStatus = m_gbServiceImpl.validateProduct(agentUser, gln, product, renew);

        return productStatus;
    }
    
    
    @RequestMapping(value = "/products/bulkUpload", method = RequestMethod.POST)
    @ResponseBody
    public List<? extends UploadValidationProduct> accountProductsCreatePost(Model model, Principal principal,
                                             //@PathVariable String gtin,
                                             //@RequestParam(required=false) String username,
                                             //@RequestParam(value="gln", required=true) String gln,
                                             @RequestBody(required = true) List<? extends InboundProductAttribute> productAttributeList) throws JsonProcessingException, GlobalBrokerException ,UsernameNotFoundException
    {    
        AgentUser agentUser = findAgentUser(principal);        
        String username = agentUser.getUsername();
        String validatedUsername = findUsername(username);        
        String gbAccountGln = getGBAccountGln(principal);

        logRequest(agentUser, validatedUsername, String.format("POST /product/bulkUpload"), productAttributeList);    
        
        List<? extends UploadValidationProduct> productResultList = m_gbServiceImpl.bulkUpload(agentUser, validatedUsername, gbAccountGln, productAttributeList);
        return productResultList;    
    }
    
    
    
    @RequestMapping(value = "/product/{gtin}/create", method = RequestMethod.POST)
    @ResponseBody
    public ProductStatus accountProductGtinCreatePost(Model model, Principal principal,
                                             @PathVariable String gtin,
                                             @RequestParam(required=false) String username,
                                             @RequestParam(value="gln", required=true) String gln,
                                             @RequestBody InboundProductAndPo productAndPo) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        logRequest(agentUser, validatedUsername, String.format("POST /product/%s/create", gtin), productAndPo);
        
        checkGtinAgreement(gtin, productAndPo.getProduct());
        ProductStatus productStatus = m_gbServiceImpl.createProduct(agentUser, validatedUsername, gln, productAndPo.getProduct(), productAndPo.getPo());

        return productStatus;
    }


    private void logRequest(AgentUser agentUser, String validatedUsername, String url, Object o)
    {
        String json;
        try
        {
            json = m_objectMapper.writeValueAsString(o);
        }
        catch (JsonProcessingException e)
        {
            json = "[cannot serialize json: " + e.getMessage() + "]";
        }
        s_logger.fine(String.format("%s : %s : %s [json]\n%s", 
                                    agentUser == null ? null : agentUser.getUsername(), 
                                                      validatedUsername, 
                                                      url, 
                                                      json));
    }
    
    @Resource
    private ObjectMapper m_objectMapper;
    
    @RequestMapping(value = "/product/{gtin}/update", method = RequestMethod.POST)
    @ResponseBody
    public ProductStatus accountProductGtinUpdatePost(Model model, Principal principal,
                                             @PathVariable String gtin,
                                             @RequestParam(required=false) String username,
                                             @RequestParam(value="gln", required=true) String gln,
                                             @RequestParam(value="renew", required=false) boolean renew,
                                             @RequestBody InboundProductAndPo productAndPo) throws GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        

        logRequest(agentUser, validatedUsername, String.format("POST /product/%s/update", gtin), productAndPo);
        
        checkGtinAgreement(gtin, productAndPo.getProduct());
        ProductStatus productStatus = m_gbServiceImpl.updateProduct(agentUser, validatedUsername, gln, productAndPo.getProduct(), renew, productAndPo.getPo());

        return productStatus;
    }
    
    
    @RequestMapping(value = "/product/{gtin}", method = RequestMethod.PUT)
    @ResponseBody
    public ProductStatus accountProductGtinPut(Model model, Principal principal,
                                             @PathVariable String gtin,
                                             @RequestParam(required=false) String username,
                                             @RequestParam(value="gln", required=true) String gln,
                                             @RequestParam(value="renew", required=false) boolean renew,
                                             /*InputStream is*/
                                             @RequestBody InboundProduct product) throws JsonProcessingException, GlobalBrokerException 
    {
        
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        logRequest(agentUser, validatedUsername, String.format("PUT /product/%s", gtin), product);
        
        checkGtinAgreement(gtin, product);
        ProductStatus productStatus = m_gbServiceImpl.putProduct(agentUser, validatedUsername, gln, product, renew);

        return productStatus;

        /*
        System.out.print("PUT content:");
        int c;
        try
        {
            while ((c = is.read()) > 0)
            {
                System.out.print((char)c);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println();
        InboundProductStatus productStatus = new InboundProductStatus();
        productStatus.setState(ProductState.COMPLETED);
        return productStatus;
        */
    }
    
    @RequestMapping(value = "/nonprod/product/{gtin}", method = RequestMethod.DELETE) 
    @ResponseBody
    public ProductStatus accountProductGtinDelete(Model model, Principal principal, @PathVariable String gtin,
                                         @RequestParam(required=false) String username,
                                         @RequestParam(value="gln", required=true) String gln) throws GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);        
        return m_gbServiceImpl.deleteProduct(agentUser, validatedUsername, gln, gtin, null);
    }

    
    
    private void checkGtinAgreement(String gtin, InboundProduct product) throws GBIllegalArgumentException
    {
        if (!gtin.equals(product.getGtin()))
            throw new GBIllegalArgumentException("GTIN in request path does not match GTIN in the specified Product");
    }

    //return m_jsonHttpClient.doRequest(InboundImport.class, HttpMethod.POST, content, "/api/import/load?gln=%s&username=%s&filename=%s", gln, username, filename);

    @RequestMapping(value = "/import/upload", method = RequestMethod.POST)
    @ResponseBody
    public Import importUpload(Model model, Principal principal,
                             @RequestParam(required=false) String username,
                             @RequestParam(value="gln", required=true) String gln,
                             @RequestParam(required=false) String filename,
                             @RequestHeader(required=false, value="Content-Type") String format,
                             @RequestBody byte[] content) throws IOException, GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        
        //byte[] content = Util.inputStreamContent(inputStream);
        return m_gbServiceImpl.importUpload(agentUser, username, gln, filename, format, content);
    }
    
    @RequestMapping(value = "/import/{importId}/settings", method = RequestMethod.POST)
    @ResponseBody
    public Import importChangeSettings(Model model, Principal principal,
                             @PathVariable String importId,
                             @RequestParam(required=false) String username,
                             @RequestParam(value="gln", required=true) String gln,
                             @RequestBody(required=false) List<? extends InboundImportPrevalidationSegmentSettings> settings) throws IOException, GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        
        //byte[] content = Util.inputStreamContent(inputStream);
        return m_gbServiceImpl.importChangeSettings(agentUser, username, gln, importId, settings);
    }

    @RequestMapping(value = "/import/{importId}/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Import importConfirm(Model model, Principal principal,
                             @PathVariable String importId,
                             @RequestParam(required=false) String username,
                             @RequestParam(value="gln", required=true) String gln) throws IOException, GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        
        //byte[] content = Util.inputStreamContent(inputStream);
        return m_gbServiceImpl.importConfirm(agentUser, username, gln, importId);
    }
    
     @RequestMapping(value = "/import/{importId}", method = RequestMethod.GET)
    @ResponseBody
    public Import importGet(Model model, Principal principal,
                            @PathVariable String importId,
                            @RequestParam(value="gln", required=true) String gln) throws GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        return m_gbServiceImpl.getImport(agentUser, gln, importId);
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends Import> importGetAll(Model model, Principal principal,
                            @RequestParam(value="gln", required=true) String gln) throws GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        return m_gbServiceImpl.getImports(agentUser, gln);
    }

    @RequestMapping(value = "/import/{importId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void importDelete(Model model, Principal principal,
                                                     @PathVariable String importId,
                            @RequestParam(value="gln", required=true) String gln) throws GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        m_gbServiceImpl.deleteImport(agentUser, gln, importId);
    }


    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends SalesOrder> orderGet(Model model, Principal principal, 
                                                     @RequestParam(value="gln", required=false) String gln,
                                                     @RequestParam(value="status", required=true) String status) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        if (!"uninvoiced".equals(status))
            throw new GBIllegalArgumentException("The value of status must be \"uninvoiced\"");
        
        Collection<? extends SalesOrder> salesOrders = 
                (gln == null ?
                 m_gbServiceImpl.getUninvoicedOrders(agentUser) :
                 m_gbServiceImpl.getUninvoicedOrders(agentUser, gln));
        
        // Make sure line items are fetched for Jackson
        for (SalesOrder salesOrder : salesOrders)
        {
            salesOrder.getLineItems().size();
        }
        
        return salesOrders;
    }
    
    @RequestMapping(value = "/invoice/{invoiceId}", method = RequestMethod.GET)
    @ResponseBody
    public Invoice invoiceGetById(Model model, Principal principal, 
                                                     @PathVariable String invoiceId) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        Invoice invoice = m_gbServiceImpl.getInvoice(agentUser, invoiceId);
        
        if (invoice == null)
            throw new NoSuchInvoiceException();
        
        return invoice;
    }
    
    @RequestMapping(value = "/invoice/{invoiceId}/orders", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends SalesOrder> invoiceOrdersGetById(Model model, Principal principal, 
                                                     @PathVariable String invoiceId) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        Collection<? extends SalesOrder> orders = m_gbServiceImpl.getInvoiceOrders(agentUser, invoiceId);
        
        return orders;
    }
    
    @RequestMapping(value = "/invoice", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends Invoice> invoiceGet(Model model, Principal principal,
            @RequestParam(value="gln", required=false) String gln,
            @RequestParam(value="status", required=true) String statusString) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        OrderStatus status;
        try
        {
             status = OrderStatus.valueOf(statusString.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            throw new GBIllegalArgumentException("Invalid value for parameter status");
        }

        if (gln == null)
            return m_gbServiceImpl.getAllInvoices(agentUser, status);
        else
            return m_gbServiceImpl.getAllInvoices(agentUser, gln, status);

    }
    
    @RequestMapping(value = "/invoice/invoiceOrders", method = RequestMethod.POST)
    @ResponseBody
    public Invoice invoiceOrdersPost(Model model, Principal principal,
                                     @RequestParam(required=false) String username,
                                     @RequestParam(value="gln", required=true) String gln,
                                     @RequestBody InboundOrderIdsAndExtras orderIdsAndExtras) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        return m_gbServiceImpl.invoiceOrders(agentUser, validatedUsername, gln, orderIdsAndExtras.getOrderIds(), orderIdsAndExtras.getExtras());
    }
 
    
    @RequestMapping(value = "/invoice/{invoiceId}/billinginfo", method = RequestMethod.PUT)
    public ResponseEntity<String> invoiceBillinginfoPut(Model model, Principal principal,
                                                        @RequestParam(required=false) String username,
                                                        @RequestParam(value="gln", required=true) String gln,
                                                        @PathVariable String invoiceId,
                                                        @RequestBody InboundBillingInfo billingInfo) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        Date date = billingInfo.getDate();
        String billingReference = billingInfo.getBillingReference();
        
        m_gbServiceImpl.setInvoiceBilled(agentUser, validatedUsername, gln, invoiceId, date, billingReference);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
 
    
    @RequestMapping(value = "/account/{gln}/transactions", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends BillingTransaction> accountTransactionsGet(Model model, Principal principal,
            @PathVariable String gln) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);

        return m_gbServiceImpl.getAllBillingTransactions(agentUser, gln);

    }
    
    @RequestMapping(value = "/payment/{paymentId}", method = RequestMethod.GET)
    @ResponseBody
    public Payment paymentGet(Model model, Principal principal,
            @PathVariable String paymentId) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);

        Payment payment = m_gbServiceImpl.getPayment(agentUser, paymentId);
        if (payment == null)
            throw new NoSuchPaymentException();

        return payment;
    }
    
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends Payment> paymentsGet(Model model, Principal principal,
            @RequestParam(value="status", required=true) String statusString) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        
        OrderStatus status;
        try
        {
             status = OrderStatus.valueOf(statusString.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            throw new GBIllegalArgumentException("Invalid value for parameter status");
        }

        return m_gbServiceImpl.getAllPayments(agentUser, status);

    }
    
    @RequestMapping(value = "/payment/payinvoices", method = RequestMethod.POST)
    @ResponseBody
    public Payment paymentPayinvoicesPost(Model model, Principal principal,
                                          @RequestParam(required=false) String username,
                                          @RequestParam(value="gln", required=true) String gln,
                                          @RequestBody InboundPayInvoicesInfo payInvoicesInfo) throws GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        PaymentReceipt receipt = payInvoicesInfo.getPaymentReceipt();
        List<String> invoiceIds = payInvoicesInfo.getInvoiceIds();
        
        return m_gbServiceImpl.payInvoices(agentUser, validatedUsername, gln, receipt, invoiceIds);
    }
    
    @RequestMapping(value = "/payment/{paymentId}/paid", method = RequestMethod.POST)
    public ResponseEntity<String> paymentPaidPost(Model model, Principal principal, 
                                                  @RequestParam(required=false) String username,
                                                  @RequestParam(value="gln", required=true) String gln,
                                                  @RequestParam(value="paidReference", required=false) String paidReference,
                                                  @PathVariable String paymentId,
                                                  @RequestBody Date date) throws GlobalBrokerException
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);
        
        m_gbServiceImpl.setPaymentPaid(agentUser, validatedUsername, gln, paymentId, date, paidReference);
        
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/time", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> timePut(Model model, Principal principal, 
                                          @RequestParam(required=false) String username,
                                          @RequestBody Date time) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);

        m_gbServiceImpl.setTime(agentUser, validatedUsername, time);
        return new ResponseEntity<String>(HttpStatus.OK);

    }

    @RequestMapping(value = "/test/{testName}", method = RequestMethod.GET)
    @ResponseBody
    public String testGet(Model model, Principal principal, 
                                          @PathVariable String testName,
                                          @RequestParam(required=false) String username,
                                          @RequestParam(required=false) String testParam) throws JsonProcessingException, GlobalBrokerException 
    {
        AgentUser agentUser = findAgentUser(principal);
        String validatedUsername = findUsername(username);

        return m_gbServiceImpl.test(agentUser, validatedUsername, testName, testParam);
    }

    
    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ExceptionInfo> handleDuplicateAccountException(DuplicateAccountException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e, "An account already exists for this GLN"), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(GlobalBrokerServiceException.class)
    public ResponseEntity<ExceptionInfo> handleGlobalBrokerServiceException(GlobalBrokerServiceException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchAccountException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchAccountException(NoSuchAccountException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e, "No such account exists for this GLN"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchAppException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchAppException(NoSuchAppException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e, "No such app exists"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchInvoiceException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchInvoiceException(NoSuchInvoiceException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e, "No such invoice exists"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchPaymentException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchPaymentException(NoSuchPaymentException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvoiceException.class)
    public ResponseEntity<ExceptionInfo> handleInvoiceException(InvoiceException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ExceptionInfo> handlePaymentException(PaymentException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(GBIllegalArgumentException.class)
    public ResponseEntity<ExceptionInfo> handleGBIllegalArgumentException(GBIllegalArgumentException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(GBIllegalStateException.class)
    public ResponseEntity<ExceptionInfo> handleGBIllegalStateException(GBIllegalStateException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionInfo> handleConflictException(ConflictException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionInfo> handleValidationException(ValidationException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo(e), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionInfo> handleMissingServletRequestParameterException(org.springframework.web.bind.MissingServletRequestParameterException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo("GBIllegalArgumentException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionInfo> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e)
    {
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo("GBIllegalArgumentException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionInfo> handleHttpMessageNotReadableException(HttpMessageNotReadableException e)
    {
        // The message created by Spring include nested exception info; truncate this by looking for a newline
        String message = e.getMessage();
        int newline = message.indexOf('\n');
        if (newline > 0)
            message = message.substring(0, newline);
        
        return new ResponseEntity<ExceptionInfo>(new ExceptionInfo("GBIllegalArgumentException", message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleMiscException(Exception e)
    {
        s_logger.log(Level.SEVERE, e.getMessage(), e);
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    

}
