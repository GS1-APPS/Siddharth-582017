package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.ProductState;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;
import org.gs1us.sgg.gbservice.api.Quotation;
import org.gs1us.sgg.gbservice.api.AppDesc.Scope;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgl.billingservice.BillingService;
import org.gs1us.sgl.billingservice.Order;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.User;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@Transactional
@RequestMapping("/ui")
public class ProductController extends GBAwareController
{
    @Resource
    private BillingService m_billingService;
    
    
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String showProducts(Model model, Principal principal) throws GlobalBrokerException 
    {
        String gbAccountGln = getGBAccountGln(principal);                
    	int recordsPerPage = 100;        
    	int noOfRecords = getGbService().getRegisteredProductsCount(gbAccountGln).intValue();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gbAccountGln, true);
        Collection<? extends Product> products = getGbService().getProductsForPagination(gbAccountGln, "0", String.valueOf(recordsPerPage));
        List<Product> productsList = new ArrayList<Product>(products);        
        PagedListHolder<Product> productPagedList = new PagedListHolder<Product>();
        productPagedList.setSource(productsList);
        productPagedList.setPageSize(recordsPerPage);
        model.addAttribute("productPagedList", productPagedList);               
        model.addAttribute("subs", subs);        
        model.addAttribute("productCount", noOfRecords);
        model.addAttribute("noOfPages", noOfPages);
        model.addAttribute("currentPage", 1);
        return "/WEB-INF/jsp/product/showProducts.jsp";
        
    }
    
    @RequestMapping(value = "/product/{type}", method = RequestMethod.GET)
    public String productGet(Model model, Principal principal, @PathVariable String type) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);       
        Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gbAccountGln, true);    	
    	int recordsPerPage = 100;
    	int noOfRecords = getGbService().getRegisteredProductsCount(gbAccountGln).intValue();
    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
    	int pageNumPassed = Integer.parseInt(type);    	
    	int offset = (pageNumPassed * recordsPerPage) - 99;
    	
    	if (pageNumPassed == 1)
    	{
    		offset = 0;
    	}
    	
    	Collection<? extends Product> products = getGbService().getProductsForPagination(gbAccountGln, String.valueOf(offset), String.valueOf(recordsPerPage));
    	List<Product> productsList = new ArrayList<Product>(products);    	
        PagedListHolder<Product> productPagedList = new PagedListHolder<Product>();        
        productPagedList.setSource(productsList);
        productPagedList.setPageSize(recordsPerPage);
        model.addAttribute("productPagedList", productPagedList);        
        model.addAttribute("subs", subs);
        model.addAttribute("productCount", noOfRecords);
        model.addAttribute("noOfPages", noOfPages);
        model.addAttribute("currentPage", pageNumPassed);
                        
        return "/WEB-INF/jsp/product/showProducts.jsp";
    }
 
    @RequestMapping(value = "/product/new", method = RequestMethod.GET)
    public String newProductGet(Model model, Principal principal) throws GlobalBrokerException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        
        modelForNewProduct(model, gbAccountGln, null, null);
        
        return "/WEB-INF/jsp/product/editProduct.jsp";
    }

    @RequestMapping(value = "/product/new", method = RequestMethod.POST)
    public String newProductPost(Model model, Principal principal, HttpServletRequest request) throws GlobalBrokerException 
    {
        String gbAccountGln = getGBAccountGln(principal);
       
        Member member = getMember(principal);
        
        Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gbAccountGln, true);
        
        String gtin = UserInputUtil.trimToNull((String)request.getParameter("gtin"));
        // Hack to avoid error with null GTIN -- validation will flag this
        if (gtin == null)
            gtin = "0";
        Product product = getGbService().newProduct(gtin);
        boolean dataAccuracyAckChecked = request.getParameter("dataAccuracyAck") != null;
        product.setDataAccuracyAckUser(dataAccuracyAckChecked ? principal.getName() : null);
        parseAttributeSet(request, subs, product);
        ProductStatus status = getGbService().validateProduct(gbAccountGln, product, false);
        
        // In the following clauses, reuse variable status so that the fall through picks up the right errors
        if (status.getState() == ProductState.AWAITING_PAYMENT)
        {
            PurchaseOrder po = requisitionPayment(member, status.getQuotation());
            status = getGbService().createProduct(getUser(principal).getUsername(), gbAccountGln, product, po);
            
            if (status.getState() == ProductState.COMPLETED)
                return "redirect:/ui/product";
        }
        else if (status.getState() == ProductState.AWAITING_FULFILLMENT)
        {
            status = getGbService().createProduct(getUser(principal).getUsername(), gbAccountGln, product, null);
            
            if (status.getState() == ProductState.COMPLETED)
                return "redirect:/ui/product";
        }

        // Fall through here if validate or create fails
        {
            modelForNewProduct(model, gbAccountGln, gtin, status);
            model.addAttribute("parameters", request.getParameterMap());
            return "/WEB-INF/jsp/product/editProduct.jsp";
        }
    }

    private void modelForNewProduct(Model model, String gbAccountGln, String gtin, ProductStatus status)
        throws GlobalBrokerException
    {
        modelForEditOrNewProduct(model, gbAccountGln, status);
        model.addAttribute("gtin", gtin);
        model.addAttribute("editGtin", true);
        model.addAttribute("action", Action.CREATE);
        model.addAttribute("heading", "Register a new product");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(ProductController.class, "newProductPost", (Object)null, (Object)null, (Object)null).toUriString());
        model.addAttribute("submitLabel", "Register");
    }

    @RequestMapping(value = "/product/{gtin}/edit", method = RequestMethod.GET)
    public String editProductGet(Model model, Principal principal, @PathVariable String gtin) throws GlobalBrokerException, NoSuchResourceException 
    {
        return editOrRenewProductGet(model, principal, gtin, false);
    }
    
    @RequestMapping(value = "/product/{gtin}/detail", method = RequestMethod.GET)
    public String detailProductGet(Model model, Principal principal, @PathVariable String gtin) throws GlobalBrokerException, NoSuchResourceException 
    {   	    	
    	Product product = lookupProduct(gtin);
    	if (product != null)
    	{
    		model.addAttribute("Product", product);
    	}
    	return "/WEB-INF/jsp/product/productDetail.jsp";
    }

    @RequestMapping(value = "/product/{gtin}/renew", method = RequestMethod.GET)
    public String renewProductGet(Model model, Principal principal, @PathVariable String gtin) throws GlobalBrokerException, NoSuchResourceException 
    {
        return editOrRenewProductGet(model, principal, gtin, true);
    }

    private String editOrRenewProductGet(Model model, Principal principal, @PathVariable String gtin, boolean renew) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        User user = (User)((Authentication)principal).getPrincipal();
        String timeZoneId = user.getTimezone();

        
        Product product = lookupProduct(gbAccountGln, gtin);
        
        modelForEditProduct(model, gbAccountGln, product, null, renew, timeZoneId);
        
        return "/WEB-INF/jsp/product/editProduct.jsp";
    
    }

    @RequestMapping(value = "/product/{gtin}/edit", method = RequestMethod.POST)
    public String editProductPost(Model model, Principal principal, HttpServletRequest request, @PathVariable String gtin) throws GlobalBrokerException, NoSuchResourceException 
    {
        return editOrRenewProductPost(model, principal, request, gtin, false);
    }

    @RequestMapping(value = "/product/{gtin}/renew", method = RequestMethod.POST)
    public String renewProductPost(Model model, Principal principal, HttpServletRequest request, @PathVariable String gtin) throws GlobalBrokerException, NoSuchResourceException 
    {
        return editOrRenewProductPost(model, principal, request, gtin, true);
    }

    public String editOrRenewProductPost(Model model, Principal principal, HttpServletRequest request, @PathVariable String gtin, boolean renew) throws GlobalBrokerException, NoSuchResourceException 
    {
        String gbAccountGln = getGBAccountGln(principal);
        User user = (User)((Authentication)principal).getPrincipal();
        String timeZoneId = user.getTimezone();
       
        Member member = getMember(principal);
        
        Product product = lookupProduct(gbAccountGln, gtin);
        
        Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gbAccountGln, true);
        
        parseAttributeSet(request, subs, product);
        boolean dataAccuracyAckChecked = request.getParameter("dataAccuracyAck") != null;
        product.setDataAccuracyAckUser(dataAccuracyAckChecked ? principal.getName() : null);
        ProductStatus status = getGbService().validateProduct(gbAccountGln, product, renew);
        
        // In the following clauses, reuse variable status so that the fall through picks up the right errors
        if (status.getState() == ProductState.AWAITING_PAYMENT)
        {
            PurchaseOrder po = requisitionPayment(member, status.getQuotation());
            status = getGbService().updateProduct(getUser(principal).getUsername(), gbAccountGln, product, renew, po);
            
            if (status.getState() == ProductState.COMPLETED)
                return "redirect:/ui/product";
        }
        else if (status.getState() == ProductState.AWAITING_FULFILLMENT)
        {
            status = getGbService().updateProduct(getUser(principal).getUsername(), gbAccountGln, product, renew, null);
            
            if (status.getState() == ProductState.COMPLETED)
                return "redirect:/ui/product";
        }

        // Fall through to here if validate or update fails
        modelForEditProduct(model, gbAccountGln, product, status, renew, timeZoneId);
        model.addAttribute("parameters", request.getParameterMap());
        return "/WEB-INF/jsp/product/editProduct.jsp";
    }

    private List<String> previouslySelectedApps(Product product, Collection<? extends AppDesc> appDescs)
    {
        List<String> result = new ArrayList<>();
        for (AppDesc appDesc : appDescs)
        {
            AttributeDesc selectionAttributeDesc = appDesc.getProductModuleDesc().getSelectionAttribute();
            boolean isSelectable = selectionAttributeDesc != null;
            boolean isSelected = !isSelectable || product.getAttributes().getBooleanAttribute(selectionAttributeDesc);
            if (isSelected)
                result.add(appDesc.getName());
        }
        return result;
    }

    private PurchaseOrder requisitionPayment(Member member, Quotation quotation)
    {
        Order purchase = m_billingService.requisition(member, quotation);
        PurchaseOrder po = getGbService().newPurchaseOrder(purchase.getId(), purchase.getRequisitionDate(), purchase.getLineItems());
        return po;
        
    }
    /*
    private void invoiceIfNecessary(GBAccount gbAccount, PurchaseOrder po, ProductStatus status)
    {
        String invoiceId = status.getInvoiceId();
        if (invoiceId != null)
        {
            m_billingService.invoice(gbAccount, po.getPOId(), invoiceId);
        }
    }
*/
    private void modelForEditProduct(Model model, String gbAccountGln, Product product, ProductStatus status, boolean renew, String timeZoneId) throws GlobalBrokerException
    {
        String gtin = product.getGtin();
        String actionUrl = 
                (renew ? 
                 MvcUriComponentsBuilder.fromMethodName(ProductController.class, "renewProductPost", (Object)null, (Object)null, (Object)null, gtin).toUriString() :
                 MvcUriComponentsBuilder.fromMethodName(ProductController.class, "editProductPost", (Object)null, (Object)null, (Object)null, gtin).toUriString());
        String heading = 
                (renew ?
                 "Renew product data and applications for GTIN " + gtin :
                 "Edit product data for GTIN " + gtin);
        List<String> previouslySelectedApps = previouslySelectedApps(product, getGbService().getAppDescs(gbAccountGln));
        
        Collection<? extends AppSubscription> subs = modelForEditOrNewProduct(model, gbAccountGln, status);
        model.addAttribute("heading", heading);
        model.addAttribute("editGtin", false);
        model.addAttribute("action", renew ? Action.RENEW : Action.UPDATE);
        model.addAttribute("previouslySelectedApps", previouslySelectedApps);
        model.addAttribute("parameters", WebappUtil.gatherAttributeSetFormParameters(subs, Scope.PRODUCT, product.getAttributes(), timeZoneId));
        model.addAttribute("actionUrl", actionUrl);
        model.addAttribute("submitLabel", "Save");
    }
    
    private Collection<? extends AppSubscription> modelForEditOrNewProduct(Model model, String gbAccountGln, ProductStatus status) throws GlobalBrokerException
    {
        Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gbAccountGln, true);
        
        model.addAttribute("subs", subs);
        model.addAttribute("cancelUrl", MvcUriComponentsBuilder.fromMethodName(ProductController.class, "showProducts", (Object)null, (Object)null).toUriString());

        Map<String, List<String>> validationErrors = WebappUtil.errorListToMap(status == null ? null : status.getValidationErrors());
        model.addAttribute("validationErrors", validationErrors);
        return subs;
    }

    private void parseAttributeSet(HttpServletRequest request, Collection<? extends AppSubscription> subs, Product product)
    {
        AttributeSet attributes = product.getAttributes();
        AppDesc.Scope scope = Scope.PRODUCT;
        
        WebappUtil.parseAttributeSetFormParameters(request, subs, scope, "", attributes);
        
        // Hack: we only have a form control for basic start date, but we need this to control both basic start date and dm start date
        // So we transfer the value for basic start date to dm start date
        attributes.setAttribute("dmStartDate", attributes.getAttribute("basicStartDate"));
    }

    
    private Product lookupProduct(String itemNumber) throws GlobalBrokerException, NoSuchResourceException
    {    	
    	Product product = null;
    	
    	try 
    	{    		
    		product = getGbService().getProductByGtinOnly(itemNumber);
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	    	    	
    	return product;
    }
    
    
    private Product lookupProduct(String gbAccountGln, String gtin) throws GlobalBrokerException, NoSuchResourceException
    {
        Product product = getGbService().getProductByGtin(gbAccountGln, gtin);
        if (product == null)
            throw new NoSuchResourceException();
        return product;
    }

    @RequestMapping(value = "/product/{gtin}/delete", method = RequestMethod.GET)
    public String deleteProductGet(Model model, Principal principal, @PathVariable String gtin) throws GlobalBrokerException 
    {
    	String gbAccountGln = getGBAccountGln(principal);
        User user = (User)((Authentication)principal).getPrincipal();
        getGbService().deleteProduct(user.getUsername(), gbAccountGln, gtin, null);
        return showProducts(model, principal);
    }

}