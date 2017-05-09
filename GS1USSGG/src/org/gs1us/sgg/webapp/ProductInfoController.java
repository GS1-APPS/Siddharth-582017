package org.gs1us.sgg.webapp;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.util.UserInputUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
public class ProductInfoController
{
    @Resource
    private Options m_options;
    
    @Resource
    private ProductManager m_productManager;
    
    @Resource
    private GBDao m_gbDao;
    
    @RequestMapping(value = "/product/{gtin}/info", method = RequestMethod.GET)
    public String productInfoGet(Model model, @PathVariable String gtin)
    {
        try
        {
            Product product = m_productManager.getProductByGtin(null, gtin);
            if (product == null)
                return m_options.jspPath("nosuchproduct.jsp");
            else
            {
                model.addAttribute("product", product);
                return m_options.jspPath("productinfo.jsp");
            }
        }
        catch (GlobalBrokerException e)
        {
            return m_options.jspPath("noinfoavailable.jsp");
        }
    }
    
    @RequestMapping(value = "/product/query", method = RequestMethod.GET)
    public String productQueryGet(Model model, @RequestParam(required=false) String gtin)
    {
        String trimmed = UserInputUtil.trimToNull(gtin);
        if (trimmed == null || !UserInputUtil.isValidGtin(gtin))
        {
            model.addAttribute("gtin", trimmed);
            return m_options.jspPath("query.jsp");
        }
        else
        {
            String padded = "000000".substring(0, 14 - trimmed.length()) + trimmed;
            return "redirect:/product/" + padded + "/info";
        }
    }
    
    public static class Options
    {
        private String m_jspBase;

        public Options(String jspBase)
        {
            super();
            m_jspBase = jspBase;
        }
        
        public String getJspBase()
        {
            return m_jspBase;
        }
        
        public String jspPath(String file)
        {
            return m_jspBase + "/" + file;
        }
    }


}
