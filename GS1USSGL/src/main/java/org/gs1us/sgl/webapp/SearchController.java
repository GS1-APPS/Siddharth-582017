package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgg.gbservice.api.IsoCountryRef;
import org.gs1us.sgg.gbservice.api.NoSuchAccountException;
import org.gs1us.sgg.gbservice.api.Product;
//import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.util.VersionOracle;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.User;
import org.gs1us.sgl.memberservice.standalone.StandaloneUser;
import org.gs1us.sgl.security.SecurityUtil;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
//@RequestMapping("/ui")
public class SearchController  extends GBAwareController
{
    @Resource
    private GlobalBrokerService m_gbService;
    
    @Resource
    private VersionOracle m_versionOracle;
            
    @RequestMapping(value = "/ui/search", method = RequestMethod.GET)
    public String search(Model model) throws GlobalBrokerException
    {        
        return "/WEB-INF/jsp/productSearch/index.jsp";
    }

    @RequestMapping(value = "/ui/searchByAuthentication", method = RequestMethod.GET)
    public String searchByAuthentication(Model model)
    {
        return "/WEB-INF/jsp/productSearch/keyAuthentication.jsp";
    }
    
    @RequestMapping(value = "/ui/searchByValidation", method = RequestMethod.GET)
    public String searchByValidation(Model model)
    {
        return "/WEB-INF/jsp/productSearch/productValidation.jsp";
    }

    @RequestMapping(value = "/ui/searchByProduct", method = RequestMethod.GET)
    public String searchByProduct(Model model) throws GlobalBrokerException
    {
    	Collection<? extends IsoCountryRef> countryList = getGbService().getAllIsoCountryRef();
    	model.addAttribute("countryList", countryList);    	    	
        return "/WEB-INF/jsp/productSearch/searchFunction.jsp";
    }
    
    @RequestMapping(value = "/ui/searchByProduct", method = RequestMethod.POST)
    public String searchByValidationPost(Model model, @RequestParam String gpcNumber, @RequestParam String targetMarket, HttpServletRequest req) throws GlobalBrokerException, NoSuchResourceException
    {
    	try
    	{
    		if (gpcNumber != null && !gpcNumber.equals("") && targetMarket != null && !targetMarket.equals(""))
    		{
    	    	int recordsPerPage = 100;              
    	        int noOfRecords = getGbService().getProductsCountBasedOnGpcAndTargetMarket(gpcNumber, targetMarket).intValue();        
    	        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);    			
    			
    			Collection<? extends Product> products = getGbService().getProductsBasedOnGpcAndTargetMarket(gpcNumber, targetMarket, "0", String.valueOf(recordsPerPage));
    			
    			if (products != null)
    			{
    				if (products.size() > 0)
    				{
    			        List<Product> productsList = new ArrayList<Product>(products);        
    			        PagedListHolder<Product> productPagedList = new PagedListHolder<Product>();
    			        productPagedList.setSource(productsList);
    			        productPagedList.setPageSize(recordsPerPage);
    			        model.addAttribute("productPagedList", productPagedList);    					
    			        model.addAttribute("productCount", noOfRecords);
    			        model.addAttribute("noOfPages", noOfPages);
    			        model.addAttribute("currentPage", 1);
    				}
    				else
    				{
    					model.addAttribute("errorMessage", "No product found based on the provided GPC and Target Market.");
    				}
    			}
    			else
    			{
    				model.addAttribute("errorMessage", "No product found based on the provided GPC and Target Market.");
    			}
    		}
    		else
    		{
    			model.addAttribute("errorMessage", "Global Product Classification (GPC) and Target Market are both required fields.");
    		}    		    	
    	}
    	catch (Exception ex)
    	{
    		model.addAttribute("errorMessage", "Global Trade Item Number is required.");
    	} 	
    	
    	Collection<? extends IsoCountryRef> countryList = getGbService().getAllIsoCountryRef();
    	model.addAttribute("countryList", countryList);
    	//model.addAttribute("gpcNumber", gpcNumber);
    	//model.addAttribute("targetMarket", targetMarket);
    	req.getSession().setAttribute("gpcNumber",  gpcNumber);
    	req.getSession().setAttribute("targetMarket",  targetMarket);
    	return "/WEB-INF/jsp/productSearch/searchFunctionResponse.jsp";
    }
    
    @RequestMapping(value = "/ui/searchByProductPagination/{type}", method = RequestMethod.GET) 
    public String searchByProductPaginationGet(Model model, HttpServletRequest req, @PathVariable String type) throws GlobalBrokerException, NoSuchResourceException 
    {
    	System.out.println("am i here");
    	
    	int recordsPerPage = 100;
    	int noOfRecords = getGbService().getProductsCountBasedOnGpcAndTargetMarket(req.getSession().getAttribute("gpcNumber").toString(), req.getSession().getAttribute("targetMarket").toString()).intValue(); 
    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
    	int pageNumPassed = Integer.parseInt(type);
    	int offset = (pageNumPassed * 100) - 99;
    	
    	if (pageNumPassed == 1)
    	{
    		offset = 0;
    	}
    	
    	Collection<? extends Product> products = getGbService().getProductsBasedOnGpcAndTargetMarket(req.getSession().getAttribute("gpcNumber").toString(), req.getSession().getAttribute("targetMarket").toString(), String.valueOf(offset), String.valueOf(recordsPerPage));
    	List<Product> productsList = new ArrayList<Product>(products);    	
        PagedListHolder<Product> productPagedList = new PagedListHolder<Product>();        
        productPagedList.setSource(productsList);        
        productPagedList.setPageSize(recordsPerPage);
        
    	Collection<? extends IsoCountryRef> countryList = getGbService().getAllIsoCountryRef();
    	model.addAttribute("countryList", countryList);
        
        model.addAttribute("productPagedList", productPagedList);    					
        model.addAttribute("productCount", noOfRecords);
        model.addAttribute("noOfPages", noOfPages);
        model.addAttribute("currentPage", pageNumPassed);
        
        return "/WEB-INF/jsp/productSearch/searchFunctionResponse.jsp";        
    }
    
    @RequestMapping(value = "/ui/searchByAuthentication", method = RequestMethod.POST)
    public String searchByAuthenticationPost(Model model, @RequestParam String itemNumber) throws GlobalBrokerException, NoSuchResourceException
    {
    	try
    	{
    		if (itemNumber != null && !itemNumber.equals(""))
    		{
            	Product product = lookupProduct(itemNumber);
            	model.addAttribute("itemNumber", itemNumber);
            	
            	if (product != null)
            	{
            		model.addAttribute("ProductName", itemNumber + " is valid");
            	}    		
            	else
            	{
            		model.addAttribute("ProductName", itemNumber + " no information is available");
            	}    			
    		}
    		else
    		{
    			model.addAttribute("itemNumber", "");
    			model.addAttribute("errorMessage", "Global Trade Item Number is required.");
    		}
    	}
    	catch (Exception ex)
    	{
    		model.addAttribute("itemNumber", "");
    		model.addAttribute("errorMessage", "Global Trade Item Number is required.");
    	} 	
   	
    	return "/WEB-INF/jsp/productSearch/keyAuthenticationResponse.jsp";
    }

    @RequestMapping(value = "/ui/searchByValidation", method = RequestMethod.POST)
    public String searchByValidationPost(Model model, @RequestParam String itemNumber) throws GlobalBrokerException, NoSuchResourceException
    {
    	try
    	{
    		if (itemNumber != null && !itemNumber.equals(""))
    		{
            	Product product = lookupProduct(itemNumber);
            	model.addAttribute("itemNumber", itemNumber);
            	
            	if (product != null)
            	{
            		model.addAttribute("Product", product);
            	}
            	else
            	{
            		model.addAttribute("errorMessage", "No product found based on the provided GTIN number.");
            	}    			
    		}
    		else
    		{
    			model.addAttribute("itemNumber", "");
    			model.addAttribute("errorMessage", "Global Trade Item Number is required.");
    		}
    	}
    	catch (Exception ex)
    	{
    		model.addAttribute("itemNumber", "");
    		model.addAttribute("errorMessage", "Global Trade Item Number is required.");
    	} 	
   	
    	return "/WEB-INF/jsp/productSearch/productValidationResponse.jsp";
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
    
}
