package org.gs1us.sgl.webapp;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

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
    public String searchByProduct(Model model) throws GlobalBrokerException
    {
    	Collection<? extends IsoCountryRef> countryList = getGbService().getAllIsoCountryRef();
    	model.addAttribute("countryList", countryList);    	    	
        return "/WEB-INF/jsp/productSearch/searchFunction.jsp";
    }
    
    @RequestMapping(value = "/ui/searchByProduct", method = RequestMethod.POST)
    public String searchByValidationPost(Model model, @RequestParam String gpcNumber, @RequestParam String targetMarket) throws GlobalBrokerException, NoSuchResourceException
    {
    	try
    	{
    		if (gpcNumber != null && !gpcNumber.equals("") && targetMarket != null && !targetMarket.equals(""))
    		{
    			Collection<? extends Product> products = getGbService().getProductsBasedOnGpcAndTargetMarket(gpcNumber, targetMarket);
    			if (products != null)
    			{
    				if (products.size() > 0)
    				{
    					model.addAttribute("Product", products);	
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
    	
    	model.addAttribute("gpcNumber", gpcNumber);
    	model.addAttribute("targetMarket", targetMarket);
    	
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
