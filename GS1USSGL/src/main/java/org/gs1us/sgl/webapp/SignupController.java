package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.User;
import org.gs1us.sgl.memberservice.standalone.StandaloneMember;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;
import org.gs1us.sgl.memberservice.standalone.StandaloneUser;
import org.gs1us.sgl.serviceterms.TermsOfService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@Transactional
@RequestMapping("/ui")
public class SignupController extends GBAwareController
{
    @Resource
    private ClockService m_clockService;
    
    @Resource
    private StandaloneMemberDao m_memberDao;
    
    @Resource
    private TermsOfService m_termsOfService;
    
    /*
     *  Date brandOwnerLicenseAgreementDate = (Date)request.getAttribute("brandOwnerLicenseAgreementDate");
    Date digimarcAgreementDate = (Date)request.getAttribute("digimarcAgreementDate");
    String brandOwnerAgreementActionUrl = (String)request.getAttribute("brandOwnerAgreementActionUrl");
    String digimarcActionUrl = (String)request.getAttribute("digimarcActionUrl");
    String timeZoneId = (String)request.getAttribute("timeZoneId");
     */
    
    @RequestMapping(value = "/agreements", method = RequestMethod.GET)
    public String agreementsGet(Model model, Principal principal) throws GlobalBrokerException
    {
        Member member = getMember(principal);
        if (member != null)
        {
            Date brandOwnerLicenseAgreementDate = member.getBrandOwnerAgreementSignedDate();
            if (brandOwnerLicenseAgreementDate != null)
            {
                model.addAttribute("brandOwnerLicenseAgreementDate", brandOwnerLicenseAgreementDate);

                Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(member.getGln(), false);
                for (AppSubscription sub : subs)
                {
                    if (sub.getAppName().equals("dwcode"))
                    {
                        Date digimarcAgreementDate = sub.getSubscriptionDate();
                        model.addAttribute("digimarcAgreementDate", digimarcAgreementDate);
                    }
                }
            }
        }


        model.addAttribute("brandOwnerAgreementActionUrl", MvcUriComponentsBuilder.fromMethodName(SignupController.class, "brandOwnerAgreementPost", null, null, null).replaceQuery(null).toUriString());
        model.addAttribute("digimarcActionUrl", MvcUriComponentsBuilder.fromMethodName(SignupController.class, "digimarcAgreementPost", null, null, null).replaceQuery(null).toUriString());
 /*
        model.addAttribute("brandOwnerAgreementActionUrl", "./agreements/brandOwner");
        model.addAttribute("digimarcActionUrl", "./agreements/digimarc");
 */       return "/WEB-INF/jsp/signup/agreements.jsp";
    }
    
    @RequestMapping(value = "/agreements/brandOwner", method = RequestMethod.POST)
    public String brandOwnerAgreementPost(Model model, Principal principal, @RequestParam(required=false) String agreed) throws GlobalBrokerException
    {
        User user = userToSignup(principal);

        if (user == null)
            return "redirect:/ui/agreements";

        /*
        if (!signupCommand.getAcceptTermsOfService())
            bindingResult.rejectValue("acceptTermsOfService", "MustAcceptTerms", "You must check the box to accept the " + GlobalSettings.shortProductName() + "Terms of Service");
        
        if (bindingResult.hasErrors())
        {
            signupModelAttributes(model);
            return "/WEB-INF/jsp/signup/signup.jsp";
        }
        */

        // Javascript disables the submit button if not agreed, so this is just to protect against HTTP-level hacking
        if (agreed != null)
        {
            Member member = user.getMember();

            StandaloneMember standaloneMember = m_memberDao.getMember(((StandaloneMember)member).getId());
            standaloneMember.setBrandOwnerAgreementSignedByUser(user.getUsername());
            standaloneMember.setBrandOwnerAgreementSignedDate(m_clockService.now());
            m_memberDao.updateMember(standaloneMember);

            // Need to do this so that the logged-in principal sees the change
            ((StandaloneUser)user).setMember(standaloneMember);
        }
        return "redirect:/ui/agreements";        
    }
    
    @RequestMapping(value = "/agreements/digimarc", method = RequestMethod.POST)
    public String digimarcAgreementPost(Model model, Principal principal, @RequestParam(required=false) String agreed) throws GlobalBrokerException, NoSuchResourceException
    {
        // Javascript disables the submit button if not agreed, so this is just to protect against HTTP-level hacking
        if (agreed != null)
        {
            String gln = getGBAccountGln(principal);

            Collection<? extends AppDesc> appDescs = getGbService().getAppDescs(gln);
            Collection<? extends AppSubscription> subscriptions = getGbService().getAppSubscriptions(gln, false);

            // Shouldn't happen if user uses UI correctly
            AppDesc appDesc = AppController.findApp(appDescs, "dwcode");
            if (appDesc == null)
                throw new NoSuchResourceException();

            // Shouldn't happen if user uses UI correctly
            if (!AppController.containsApp(subscriptions, appDesc))
            {

                AppSubscription sub = getGbService().newAppSubscription("dwcode", null);
                getGbService().createAppSubscription(getUser(principal).getUsername(), gln, sub);
            }
        }
        return "redirect:/ui/home";
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupGet(Model model, Principal principal) throws GlobalBrokerException
    {
        User user = userToSignup(principal);
        
        if (user == null)
            return "redirect:/ui/home";
        
        SignupCommand signupCommand = new SignupCommand();
        signupCommand.setAcceptTermsOfService(false);
        signupCommand.setTosVersion(m_termsOfService.getVersion());
        model.addAttribute("signupCommand", signupCommand);
        signupModelAttributes(model);
        return "/WEB-INF/jsp/signup/signup.jsp";
    } 
    
    /**
     * Returns the user to sign up, or null if the user is already signed up.
     */
    private User userToSignup(Principal principal) throws GlobalBrokerException
    {
        Member member = getMember(principal);
        if (member == null || member.getBrandOwnerAgreementSignedByUser() != null)
            return null;
        else
            return getUser(principal);
    }
    
    private void signupModelAttributes(Model model)
    {
        model.addAttribute("termsOfService", m_termsOfService);

        model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(HomeController.class, "home", (Object)null, (Object)null).toUriString());
        
        model.addAttribute("submitLabel", "Sign Up");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(SignupController.class, "signupPost", (Object)null, (Object)null, (Object)null, (Object)null).toUriString());
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost(Model model, @ModelAttribute SignupCommand signupCommand, BindingResult bindingResult, Principal principal)  throws GlobalBrokerException
    {
        User user = userToSignup(principal);

        if (user == null)
            return "redirect:/ui/home";

        if (!signupCommand.getAcceptTermsOfService())
            bindingResult.rejectValue("acceptTermsOfService", "MustAcceptTerms", "You must check the box to accept the " + WebappUtil.shortProductName() + "Terms of Service");
        
        if (bindingResult.hasErrors())
        {
            signupModelAttributes(model);
            return "/WEB-INF/jsp/signup/signup.jsp";
        }

        Member member = user.getMember();
        
        StandaloneMember standaloneMember = m_memberDao.getMember(((StandaloneMember)member).getId());
        standaloneMember.setBrandOwnerAgreementSignedByUser(user.getUsername());
        standaloneMember.setBrandOwnerAgreementSignedDate(m_clockService.now());
        m_memberDao.updateMember(standaloneMember);
        
        // Need to do this so that the logged-in principal sees the change
        ((StandaloneUser)user).setMember(standaloneMember);
        
        return "redirect:/ui/home";
    }
    
    public static class SignupCommand
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
