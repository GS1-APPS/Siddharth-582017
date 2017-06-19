package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgg.gbservice.api.NoSuchAccountException;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.util.VersionOracle;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController 
{
    @Resource
    private GlobalBrokerService m_gbService;
    
    @Resource
    private VersionOracle m_versionOracle;
    
    @RequestMapping(value = "/ui/home", method = RequestMethod.GET)
    public String home(Model model, Principal principal) throws GlobalBrokerException
    {
        if (principal != null)
        {
            User user = (User)((Authentication)principal).getPrincipal();
            model.addAttribute("user", user);
            Member member = user.getMember();
            if (member.getBrandOwnerAgreementSignedByUser() != null)
            {
                String gln = member.getGln();
                int noOfRecords = m_gbService.getRegisteredProductsCount(gln).intValue();
                model.addAttribute("gbAccountGln", gln);
                model.addAttribute("noOfRecords", noOfRecords);
            }
        }
        
        return "/WEB-INF/jsp/" + WebappUtil.homeJsp();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect1()
    {
        return "redirect:/ui/home";
    }
    
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String redirect2()
    {
        return "redirect:/ui/home";
    }
    
    @RequestMapping(value = "/ui/resources", method = RequestMethod.GET)
    public String resources()
    {
        return "/WEB-INF/jsp/resources.jsp";
    }

    @RequestMapping(value = "/ui/accessDenied", method = RequestMethod.GET)
    public String accessDeniedGet()
    {
        return "/WEB-INF/jsp/accessDenied.jsp";
    }
    @RequestMapping(value = "/ui/notFound", method = RequestMethod.GET)
    public String notFoundGet()
    {
        return "/WEB-INF/jsp/notFound.jsp";
    }
    @RequestMapping(value = "/ui/accessDenied", method = RequestMethod.POST)
    public String accessDeniedPost()
    {
        return "/WEB-INF/jsp/accessDenied.jsp";
    }
    
    @RequestMapping(value = "/ui/about", method = RequestMethod.GET)
    public String aboutGet(Model model)
    {
        Map<String, String> properties = new TreeMap<>();
        properties.put("Portal version", m_versionOracle.getVersion());
        try
        {
            properties.put("Global broker version", m_gbService.getVersion());
        }
        catch (GlobalBrokerException e)
        {
            properties.put("Global broker version", "Cannot connect: " + e.getMessage());
        }
        properties.put("Profile", System.getProperty("spring.profiles.active", "[none]"));
        
        model.addAttribute("properties", properties);
        return "/WEB-INF/jsp/about.jsp";
    }

    @RequestMapping(value = "/ui/upce", method = RequestMethod.GET)
    public String upceGet(Model model)
    {
        return "/WEB-INF/jsp/upce.jsp";
    }
}
