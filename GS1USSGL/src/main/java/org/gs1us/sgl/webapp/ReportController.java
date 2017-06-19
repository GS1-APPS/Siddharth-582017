package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
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
public class ReportController  extends GBAwareController
{
    @Resource
    private GlobalBrokerService m_gbService;
    
    @Resource
    private VersionOracle m_versionOracle;
            
    @RequestMapping(value = "/ui/report", method = RequestMethod.GET)
    public String report(Model model) throws GlobalBrokerException
    {        
    	Long products = getGbService().getProductsForReport();    	
    	Long productsByDate = getGbService().getProductsForReportByDate();
    	model.addAttribute("TotalCount", products.toString());
    	model.addAttribute("OldDataCount", productsByDate.toString());    	
        return "/jsp/report/index.jsp";
    }
}
