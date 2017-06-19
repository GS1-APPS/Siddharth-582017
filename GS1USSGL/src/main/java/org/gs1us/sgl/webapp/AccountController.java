package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.NoSuchInvoiceException;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.User;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;
import org.gs1us.sgl.memberservice.standalone.StandaloneUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
@RequestMapping("/ui")
public class AccountController extends GBAwareController
{
    @Resource
    private StandaloneMemberDao m_memberDao;
    
    @RequestMapping(value = "/myaccount", method = RequestMethod.GET)
    public String myAccountGet(Model model, Principal principal) throws NoSuchResourceException
    {
        Member member = getMember(principal);
        if (member == null)
            throw new NoSuchResourceException();
        
        model.addAttribute("member", member);
        
        return "/jsp/account/myAccount.jsp";
    }
    
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String showAccount(Model model, Principal principal) throws GlobalBrokerException
    {
        String gln = getGBAccountGln(principal);
       
        Collection<? extends SalesOrder> uninvoiced = getGbService().getUninvoicedOrders(gln);
        model.addAttribute("uninvoiced", uninvoiced);
        Collection<? extends BillingTransaction> xactions = getGbService().getAllBillingTransactions(gln);
        model.addAttribute("xactions", xactions);
        
        return "/jsp/account/showAccount.jsp";
    }
    
    @RequestMapping(value = "/account/transaction", method = RequestMethod.GET)
    public String showTransactions(Model model, Principal principal) throws GlobalBrokerException
    {
        String gln = getGBAccountGln(principal);
       
        Collection<? extends BillingTransaction> xactions = getGbService().getAllBillingTransactions(gln);
        model.addAttribute("xactions", xactions);
        
        return "/jsp/account/showTransactions.jsp";
    }
    
    @RequestMapping(value = "/account/order", method = RequestMethod.GET)
    public String showOrders(Model model, Principal principal) throws GlobalBrokerException
    {
        String gln = getGBAccountGln(principal);

       
        Collection<? extends SalesOrder> uninvoiced = getGbService().getUninvoicedOrders(gln);
        Collection<? extends Invoice> invoices = getGbService().getAllInvoices(gln, null);
        model.addAttribute("uninvoiced", uninvoiced);
        model.addAttribute("invoices", invoices);
        
        return "/jsp/account/showOrders.jsp";
    }
    
    @RequestMapping(value = "/account/invoice/{invoiceId}", method = RequestMethod.GET)
    public String showInvoice(Model model, Principal principal, HttpServletRequest request, @PathVariable String invoiceId) throws GlobalBrokerException, NoSuchResourceException
    {
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        
        Invoice invoice;
        try
        {
            invoice = getGbService().getInvoice(invoiceId);
            if (invoice == null)
                throw new NoSuchResourceException();
            if (!isAdmin)
            {
                // We only want to call getGBAccountGln for non-admin, to avoid the intercept for the admin
                String gln = getGBAccountGln(principal);
                if (!gln.equals(invoice.getGBAccountGln()))
                    throw new NoSuchResourceException();
            }
        }
        catch (NoSuchInvoiceException e)
        {
            throw new NoSuchResourceException();
        }
        
        Collection<? extends SalesOrder> orders = getGbService().getInvoiceOrders(invoice.getInvoiceId());
       
        Member member = m_memberDao.getMemberByGln(invoice.getGBAccountGln());
        model.addAttribute("forMember", member);
        model.addAttribute("invoice", invoice);
        model.addAttribute("orders", orders);
        
        return "/jsp/account/showInvoice.jsp";
    }

}
