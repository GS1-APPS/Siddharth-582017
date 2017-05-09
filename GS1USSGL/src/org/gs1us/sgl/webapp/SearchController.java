package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.annotation.Resource;

import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgg.gbservice.api.NoSuchAccountException;
import org.gs1us.sgg.gbservice.api.Product;
//import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.util.VersionOracle;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.User;
import org.gs1us.sgl.memberservice.standalone.StandaloneUser;
import org.gs1us.sgl.security.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
    public String searchByProduct(Model model)
    {
        return "/WEB-INF/jsp/productSearch/searchFunction.jsp";
    }
    
    @RequestMapping(value = "/ui/searchByAuthentication", method = RequestMethod.POST)
    public String searchByAuthenticationPost(Model model, @RequestParam String itemNumber) throws GlobalBrokerException, NoSuchResourceException
    {
    	try
    	{
        	Product product = lookupProduct(itemNumber);
        	
        	if (product != null)
        	{
        		model.addAttribute("ProductName", itemNumber + " is valid");
        	}    		
        	else
        	{
        		model.addAttribute("ProductName", itemNumber + " no information is available");
        	}
    	}
    	catch (Exception ex)
    	{
    		model.addAttribute("errorMessage", "Global Trade Item Number is required.");
    	} 	
   	
    	return "/WEB-INF/jsp/productSearch/keyAuthenticationResponse.jsp";
    }

    @RequestMapping(value = "/ui/searchByValidation", method = RequestMethod.POST)
    public String searchByValidationPost(Model model, @RequestParam String itemNumber) throws GlobalBrokerException, NoSuchResourceException
    {
    	try
    	{
        	Product product = lookupProduct(itemNumber);
        	
        	if (product != null)
        	{
        		model.addAttribute("Product", product);
        	}
        	else
        	{
        		model.addAttribute("errorMessage", "No product found based on the provided GTIN number.");
        	}
    	}
    	catch (Exception ex)
    	{
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
    		
    		/*
    		if (productTest != null)
    		{
    			System.out.println(productTest.getGtin());	
    		}
    		else
    		{
    			System.out.println("product test is null");
    		}
    		*/
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	    	
    	//Product product = getGbService().getProductByGtin("0000000000000", itemNumber);
    	
    	return product;
    }
    
}
