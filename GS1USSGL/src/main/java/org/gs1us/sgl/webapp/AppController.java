package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@Transactional
@RequestMapping("/ui")
public class AppController extends GBAwareController
{
    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public String showApps(Model model, Principal principal) throws GlobalBrokerException
    {
        String gln = getGBAccountGln(principal);
        
        Collection<? extends AppDesc> appDescs = getGbService().getAppDescs(gln);
        Collection<? extends AppSubscription> subscriptions = getGbService().getAppSubscriptions(gln, true);
        List<AppDesc> unsubscribedApps = new ArrayList<>(appDescs.size());
        for (AppDesc appDesc : appDescs)
        {
            if (!containsApp(subscriptions, appDesc))
                unsubscribedApps.add(appDesc);
        }
        
        model.addAttribute("unsubscribedApps", unsubscribedApps);
        model.addAttribute("subscriptions", subscriptions);
        
        return "/jsp/app/showApps.jsp";
    }

    static boolean containsApp(Collection<? extends AppSubscription> subscriptions, AppDesc appDesc)
    {
        for (AppSubscription subscription : subscriptions)
        {
            if (subscription.getAppName().equals(appDesc.getName()))
                return true;
        }
        return false;
    }
    
    @RequestMapping(value = "/app/{appName}/subscribe", method = RequestMethod.GET)
    public String subscribeGet(Model model, Principal principal, @PathVariable String appName) throws GlobalBrokerException, NoSuchResourceException
    {
        String gln = getGBAccountGln(principal);
        
        Collection<? extends AppDesc> appDescs = getGbService().getAppDescs(gln);
        Collection<? extends AppSubscription> subscriptions = getGbService().getAppSubscriptions(gln, false);
        
        // Shouldn't happen if user uses UI correctly
        AppDesc appDesc = findApp(appDescs, appName);
        if (appDesc == null)
            throw new NoSuchResourceException();
        
        // Shouldn't happen if user uses UI correctly
        if (containsApp(subscriptions, appDesc))
            return "redirect:/ui/app";
        
        model.addAttribute("appDesc", appDesc);
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(AppController.class, "subscribePost", (Object)null, (Object)null, appDesc.getName()).toUriString());
        model.addAttribute("submitLabel", "Subscribe");
        model.addAttribute("cancelUrl", MvcUriComponentsBuilder.fromMethodName(AppController.class, "showApps", (Object)null, (Object)null).toUriString());
        model.addAttribute("subscribeCommand", new SubscribeCommand());
        return "/jsp/app/subscribe.jsp";
    }

    static AppDesc findApp(Collection<? extends AppDesc> appDescs, String appName)
    {
        for (AppDesc appDesc : appDescs)
        {
             if (appDesc.getName().equals(appName))
                 return appDesc;
        }
        return null;
    }
    
    @RequestMapping(value = "/app/{appName}/subscribe", method = RequestMethod.POST)
    public String subscribePost(Model model, Principal principal, @PathVariable String appName) throws GlobalBrokerException, NoSuchResourceException
    {
        String gln = getGBAccountGln(principal);
        
        Collection<? extends AppDesc> appDescs = getGbService().getAppDescs(gln);
        Collection<? extends AppSubscription> subscriptions = getGbService().getAppSubscriptions(gln, false);
        
        // Shouldn't happen if user uses UI correctly
        AppDesc appDesc = findApp(appDescs, appName);
        if (appDesc == null)
            throw new NoSuchResourceException();
        
        // Shouldn't happen if user uses UI correctly
        if (containsApp(subscriptions, appDesc))
            return "redirect:/ui/app";
        
        AppSubscription sub = getGbService().newAppSubscription(appName, null);
        getGbService().createAppSubscription(getUser(principal).getUsername(), gln, sub);
        
        return "redirect:/ui/app";
        
    }

    public static class SubscribeCommand
    {
        private boolean m_acceptTermsOfService;
        private String m_tosVersion;
        

        public boolean getAcceptTermsOfService()
        {
            return m_acceptTermsOfService;
        }
        public void setAcceptTermsOfService(boolean acceptTermsOfService)
        {
            m_acceptTermsOfService = acceptTermsOfService;
        }
        
        public String getTosVersion()
        {
            return m_tosVersion;
        }
        public void setTosVersion(String tosVersion)
        {
            m_tosVersion = tosVersion;
        }
    }

}
