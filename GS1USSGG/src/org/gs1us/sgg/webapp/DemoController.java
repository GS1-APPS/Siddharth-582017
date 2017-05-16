package org.gs1us.sgg.webapp;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.gs1us.sgg.account.AccountManager;
import org.gs1us.sgg.app.AppManager;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GBIllegalArgumentException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ValidationException;
import org.gs1us.sgg.gbservice.impl.ProductOpsManager;
import org.gs1us.sgg.gbservice.json.ExceptionInfo;
import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.util.UserInputUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
public class DemoController
{
    private static boolean s_demoEnabled = false;
    
    @Resource
    private ProductManager m_productManager;
    
    @Resource
    private AccountManager m_accountManager;
    
    public boolean getEnabled()
    {
        return s_demoEnabled;
    }
    
    public void setEnabled(boolean enabled)
    {
        s_demoEnabled = enabled;
    }
        
    @RequestMapping(value = "/demo/deleteimports", method = RequestMethod.GET)
    public String productInfoGet(Model model, Principal principal) throws NotFoundException
    {    	    
        if (s_demoEnabled)
        {
            model.addAttribute("result", "Enabled is " + s_demoEnabled);
            return "/WEB-INF/jsp/demo/deleteImports.jsp";
        }
        else
            throw new NotFoundException();
    }
    
    @RequestMapping(value = "/demo/deleteimports", method = RequestMethod.POST)
    public String productInfoPost(Model model, @RequestParam(value="minutes") String minutesString) throws NotFoundException, GlobalBrokerException
    {
        if (s_demoEnabled)
        {
            GBAccount gbAccount = m_accountManager.getAccount("0000000000000");
            Collection<? extends Product> products = m_productManager.getProducts(gbAccount);
            int minutes = Integer.parseInt(minutesString);
            Date threshold = new Date(System.currentTimeMillis() - (minutes * 60000L));
            for (Product product : products)
            {
                if (product.getModifiedDate().after(threshold))
                    m_productManager.deleteProduct(null, "demo", gbAccount, product, null);
            }
 
            return "redirect:http://52.55.25.90/GS1USSGL/ui/product";
        }
        else
            throw new NotFoundException();
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e)
    {
        return new ResponseEntity<String>((String)null, HttpStatus.NOT_FOUND);
    }

    private class NotFoundException extends Exception
    {
        public NotFoundException()
        {
            super();
        }
    }

}