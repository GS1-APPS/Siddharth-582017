package org.gs1us.sgl.webapp;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GBAccountData;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@Transactional
@RequestMapping("/ui")
public class BillingController extends GBAwareController
{
    @Resource
    private StandaloneMemberDao m_memberDao;
    
    @Resource
    private ClockService m_clockService;
    
    @RequestMapping(value = "/billing/summary", method = RequestMethod.GET)
    public String billingSummary(Model model, Principal principal) throws GlobalBrokerException 
    {
        int uninvoicedCount = getGbService().getUninvoicedOrders().size();
        int invoicedCount = getGbService().getAllInvoices(OrderStatus.INVOICED).size();
        int billedCount = getGbService().getAllInvoices(OrderStatus.BILLED).size();
        int unpaidCount = getGbService().getAllPayments(OrderStatus.PAYMENT_COMMITTED).size();
        model.addAttribute("uninvoicedCount", uninvoicedCount);
        model.addAttribute("invoicedCount", invoicedCount);
        model.addAttribute("billedCount", billedCount);
        model.addAttribute("unpaidCount", unpaidCount);
        
        return "/jsp/billing/billingSummary.jsp";
    }
    
    @RequestMapping(value = "/billing/invoiceAndBill", method = RequestMethod.GET)
    public String invoiceAndBillGet(Model model, Principal principal) throws GlobalBrokerException 
    {
        ByMemberGrouper<SalesOrder> uninvoicedOrderGroups = new ByMemberGrouper<>(m_memberDao);
        Collection<? extends SalesOrder> uninvoiced = getGbService().getUninvoicedOrders();
        uninvoicedOrderGroups.addAll(uninvoiced);
        model.addAttribute("uninvoicedOrderGroups", uninvoicedOrderGroups);
        
        return "/jsp/billing/invoiceAndBill.jsp";
    }
    
    @RequestMapping(value = "/billing/invoiceAndBill/{gln}", method = RequestMethod.GET)
    public String invoiceAndBillMemberGet(Model model, Principal principal, @PathVariable String gln) throws GlobalBrokerException, NoSuchResourceException 
    {
        Member member = getMember(gln);
        Collection<? extends SalesOrder> uninvoiced = getGbService().getUninvoicedOrders(gln);
        GBAccount gbAccount = getGbService().getAccount(gln);
        String paymentMethod = paymentMethod(gbAccount);

        model.addAttribute("member", member);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("uninvoiced", uninvoiced);
        
        return "/jsp/billing/invoiceAndBillMember.jsp";
    }

    private String paymentMethod(GBAccount gbAccount)
    {
        String licenseType = gbAccount.getAttributes().getAttribute("dmLicenseType");
        switch (licenseType)
        {
        case "50PerGtinCC": 
            return "Payment by credit card";
            
        case "50PerGtinCheck":
            return "Payment by check or PO";
            
        case "Enterprise":
            return "Enterprise license";
            
        case "DMCustomer":
            return "Digimarc customer";

        default: 
            return null;
        }
    }

    @RequestMapping(value = "/billing/invoiceAndBill/{gln}", method = RequestMethod.POST)
    public String invoiceAndBillMemberPost(HttpServletRequest request, Principal principal, @PathVariable String gln) throws GlobalBrokerException, NoSuchResourceException 
    {
        Member member = getMember(gln);
        Collection<? extends SalesOrder> uninvoiced = getGbService().getUninvoicedOrders(gln);
        List<String> ordersToInvoice = determineOrdersToInvoice(request, uninvoiced);
        if (ordersToInvoice.size() > 0)  // TODO: give an error or something if not?
        {
            String currency = uninvoiced.iterator().next().getTotal().getCurrency();
            
            String stateSalesTaxJurisdiction = UserInputUtil.trimToNull(request.getParameter("stateSalesTaxJurisdiction"));
            Amount stateSalesTaxAmount = parseSalesTax(request.getParameter("stateSalesTaxAmount"), currency);
            String localSalesTaxJurisdiction = UserInputUtil.trimToNull(request.getParameter("localSalesTaxJurisdiction"));
            Amount localSalesTaxAmount = parseSalesTax(request.getParameter("localSalesTaxAmount"), currency);
            
            List<InvoiceExtra> extras = new ArrayList<>(2);
            if (stateSalesTaxJurisdiction != null && stateSalesTaxAmount != null)
                extras.add(salesTaxExtra(stateSalesTaxJurisdiction, stateSalesTaxAmount));
            if (localSalesTaxJurisdiction != null && localSalesTaxAmount != null)
                extras.add(salesTaxExtra(localSalesTaxJurisdiction, localSalesTaxAmount));
            
            String gbAccountGln = member.getGln();
            Invoice invoice = getGbService().invoiceOrders(getUser(principal).getUsername(), gbAccountGln, ordersToInvoice, extras);

            String billingReference = UserInputUtil.trimToNull(request.getParameter("billingReference"));
            
            getGbService().setInvoiceBilled(getUser(principal).getUsername(), gbAccountGln, invoice.getInvoiceId(), m_clockService.now(), billingReference);
        }
        return "redirect:/ui/billing/invoiceAndBill";
    }
    
    private Amount parseSalesTax(String amountString, String currency)
    {
        amountString = UserInputUtil.trimToNull(amountString);
        if (amountString == null)
            return null;
        
        int pos = amountString.indexOf(' ');
        if (pos > 0)
            amountString = amountString.substring(0, pos);  // To trim off any currency if supplied
        
        BigDecimal amountValue = new BigDecimal(amountString);
        
        return new Amount(amountValue, currency);
    }




    private InvoiceExtra salesTaxExtra(String jurisdiction, Amount amount)
    {
        return getGbService().newInvoiceExtra("$tax_" + jurisdiction, 
                                              "Sales tax for " + jurisdiction, 
                                              new String[]{jurisdiction},
                                              amount);
    }
        
    private List<String> determineOrdersToInvoice(HttpServletRequest request, Collection<? extends SalesOrder> uninvoiced) throws GlobalBrokerException
    {
        List<String> result = new ArrayList<>(uninvoiced.size());
        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
        {
            String parameterName = e.nextElement();
            if (parameterName.startsWith("order-"))
            {
                String orderId = parameterName.substring("order-".length());
            
                if (!salesOrderListContainsOrderId(uninvoiced, orderId))
                    throw new GlobalBrokerException("Out of sync");  // TODO: not the best way to handle this, but should be exceedingly rare
                
                result.add(orderId);
            }
        }
        return result;
    }

    private boolean salesOrderListContainsOrderId(Collection<? extends SalesOrder> uninvoiced, String orderId)
    {
        for (SalesOrder order : uninvoiced)
        {
            if (orderId.equals(order.getOrderId()))
                return true;
        }
        return false;
    }

    @RequestMapping(value = "/billing/invoiced", method = RequestMethod.GET)
    public String billingInvoiced(Model model, Principal principal) throws GlobalBrokerException 
    {
        Collection<? extends Invoice> invoiced = getGbService().getAllInvoices(OrderStatus.INVOICED);
        Map<String, Member> memberMap = toMemberMap(invoiced);
        
        model.addAttribute("invoiced", invoiced);
        model.addAttribute("memberMap", memberMap);
        
        return "/jsp/billing/invoiced.jsp";
    }




    private Map<String, Member> toMemberMap(
            Collection<? extends GBAccountData> accountData)
    {
        Map<String, Member> memberMap = new HashMap<>();
        for (GBAccountData accountDatum : accountData)
        {
            String gln = accountDatum.getGBAccountGln();
            Member member = m_memberDao.getMemberByGln(gln);
            if (member != null)
                memberMap.put(gln, member);
        }
        return memberMap;
    }

    
    @RequestMapping(value = "/billing/invoice/{invoiceId}/bill", method = RequestMethod.GET)
    public String billInvoiceGet(Model model, Principal principal, @PathVariable String invoiceId) throws GlobalBrokerException, NoSuchResourceException 
    {
        Invoice invoice = lookupInvoice(invoiceId);

        model.addAttribute("invoice", invoice);
        model.addAttribute("orders", getGbService().getInvoiceOrders(invoiceId));
        
        return "/jsp/billing/billInvoice.jsp";
    }
    
    @RequestMapping(value = "/billing/invoice/{invoiceId}/bill", method = RequestMethod.POST)
    public String billInvoicePost(HttpServletRequest request, Principal principal, @PathVariable String invoiceId) throws GlobalBrokerException, NoSuchResourceException 
    {
        Invoice invoice = lookupInvoice(invoiceId);
        String gbAccountGln = invoice.getGBAccountGln();
        
        String billingReference = request.getParameter("billingReference");
        
        getGbService().setInvoiceBilled(getUser(principal).getUsername(), gbAccountGln, invoiceId, m_clockService.now(), UserInputUtil.trimToNull(billingReference));
        
        return "redirect:/ui/billing/invoiced";
    }
    
    @RequestMapping(value = "/billing/billed", method = RequestMethod.GET)
    public String billingBilled(Model model, Principal principal) throws GlobalBrokerException 
    {
        Collection<? extends Invoice> billed = getGbService().getAllInvoices(OrderStatus.BILLED);
        Map<String, Member> memberMap = toMemberMap(billed);
        model.addAttribute("billed", billed);
        model.addAttribute("memberMap", memberMap);
        
        return "/jsp/billing/billed.jsp";
    }

    
    @RequestMapping(value = "/billing/invoice/{invoiceId}/pay", method = RequestMethod.GET)
    public String payInvoiceGet(Model model, Principal principal, @PathVariable String invoiceId) throws GlobalBrokerException, NoSuchResourceException 
    {
        Invoice invoice = lookupInvoice(invoiceId);
        
        String gln = invoice.getGBAccountGln();
        Member member = getMember(gln);
        GBAccount gbAccount = getGbService().getAccount(gln);
        
        Collection<? extends Invoice> invoices = Collections.singleton(invoice);
        
        Amount total = invoice.getTotal();
        
        PaymentCommand paymentCommand = new PaymentCommand();
        paymentCommand.setPaymentAmount(String.format("%.2f", total.getValue()));
        paymentCommand.setCurrency(total.getCurrency());
        
        model.addAttribute("member", member);
        model.addAttribute("invoices", invoices);
        model.addAttribute("total", total);
        model.addAttribute("paymentCommand", paymentCommand);
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(BillingController.class, "payInvoicePost", null, null, invoiceId, null).toUriString());
        model.addAttribute("cancelUrl", MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingBilled", null, null).toUriString());
        
        return "/jsp/billing/payAll.jsp";
    }
    
    @RequestMapping(value = "/billing/invoice/{invoiceId}/pay", method = RequestMethod.POST)
    public String payInvoicePost(Model model, Principal principal, @PathVariable String invoiceId, @ModelAttribute PaymentCommand paymentCommand) throws GlobalBrokerException, NoSuchResourceException 
    {
        Invoice invoice = lookupInvoice(invoiceId);
        
        String gln = invoice.getGBAccountGln();
        Member member = getMember(gln);
        
        // TODO: validation!
        BigDecimal paymentAmount = new BigDecimal(paymentCommand.getPaymentAmount());
        Amount payment = new Amount(paymentAmount, paymentCommand.getCurrency());
        
        String paymentId = paymentCommand.getPaymentId();
        PaymentReceipt paymentReceipt = getGbService().newPaymentReceipt(paymentId, m_clockService.now(), payment);
        
        Collection<? extends Invoice> invoices = getGbService().getAllInvoices(gln, OrderStatus.INVOICED);
        List<String> invoiceIds = Collections.singletonList(invoice.getInvoiceId());
        
        getGbService().payInvoices(getUser(principal).getUsername(), gln, paymentReceipt, invoiceIds);
        
        return "redirect:/ui/billing/billed";
    }
    
    
    
    private Invoice lookupInvoice(String invoiceId) throws NoSuchResourceException, GlobalBrokerException
    {
        Invoice invoice = getGbService().getInvoice(invoiceId);
        if (invoice == null)
            throw new NoSuchResourceException();
        return invoice;
    }
    
    @RequestMapping(value = "/billing/unpaid", method = RequestMethod.GET)
    public String billingUnpaid(Model model, Principal principal) throws GlobalBrokerException 
    {
        Collection<? extends Payment> unpaid = getGbService().getAllPayments(OrderStatus.PAYMENT_COMMITTED);
        Map<String, Member> memberMap = toMemberMap(unpaid);
        model.addAttribute("unpaid", unpaid);
        model.addAttribute("memberMap", memberMap);
        
        return "/jsp/billing/unpaid.jsp";
    }

    
    @RequestMapping(value = "/billing/payment/{paymentId}/paid", method = RequestMethod.GET)
    public String paidPaymentGet(Model model, Principal principal, @PathVariable String paymentId) throws GlobalBrokerException, NoSuchResourceException 
    {
        Payment payment = lookupPayment(paymentId);
        
        model.addAttribute("payment", payment);
        
        return "/jsp/billing/payPayment.jsp";
    }
    
    @RequestMapping(value = "/billing/invoice/{paymentId}/paid", method = RequestMethod.POST)
    public String paidPaymentPost(Model model, Principal principal, @PathVariable String paymentId, HttpServletRequest request) throws GlobalBrokerException, NoSuchResourceException 
    {
        Payment payment = lookupPayment(paymentId);
        String gbAccountGln = payment.getGBAccountGln();
        String paidReference = UserInputUtil.trimToNull(request.getParameter("paidReference"));
        
        getGbService().setPaymentPaid(getUser(principal).getUsername(), gbAccountGln, paymentId, m_clockService.now(), paidReference);
        
        return "redirect:/ui/billing/unpaid";
    }
    
    private Payment lookupPayment(String paymentId) throws NoSuchResourceException, GlobalBrokerException
    {
        Payment payment = getGbService().getPayment(paymentId);
        if (payment == null)
            throw new NoSuchResourceException();
        return payment;
    }

    private static class PaymentCommand
    {
        private String m_paymentId;
        private String m_currency;
        private String m_paymentAmount;
        public String getPaymentId()
        {
            return m_paymentId;
        }
        public void setPaymentId(String paymentId)
        {
            m_paymentId = paymentId;
        }
        public String getCurrency()
        {
            return m_currency;
        }
        public void setCurrency(String currency)
        {
            m_currency = currency;
        }
        public String getPaymentAmount()
        {
            return m_paymentAmount;
        }
        public void setPaymentAmount(String paymentAmount)
        {
            m_paymentAmount = paymentAmount;
        }
        
        
    }
    
    private Member getMember(String gln) throws NoSuchResourceException
    {
        Member member = m_memberDao.getMemberByGln(gln);
        if (member == null)
            throw new NoSuchResourceException();
        return member;
    }


    public static class InvoiceGroup
    {
        Member m_member;
        Date m_mostRecentInvoiceDate;
        Amount m_total;
        List<Invoice> m_invoices;
        public InvoiceGroup(Member member)
        {
            super();
            m_member = member;
            m_invoices = new ArrayList<>();
            m_total = Amount.ZERO;
        }
        public Member getMember()
        {
            return m_member;
        }
        
        public Date getMostRecentInvoiceDate()
        {
            return m_mostRecentInvoiceDate;
        }
        
        public Amount getTotal()
        {
            return m_total;
        }
        public List<? extends Invoice> getInvoices()
        {
            return m_invoices;
        }
        public void addOrder(Invoice invoice)
        {
            m_invoices.add(invoice);
            m_total = m_total.add(invoice.getTotal());
            if (m_mostRecentInvoiceDate == null || invoice.getDate().compareTo(m_mostRecentInvoiceDate) < 0)
                m_mostRecentInvoiceDate = invoice.getDate();

        }
        
    }

}
    