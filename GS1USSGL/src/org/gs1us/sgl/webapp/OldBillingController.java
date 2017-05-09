package org.gs1us.sgl.webapp;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgl.billingservice.BillingService;
import org.gs1us.sgl.billingservice.Order;
import org.gs1us.sgl.billingservice.BillingStatus;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.MemberService;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Transactional
@RequestMapping("/ui")
public class OldBillingController extends GBAwareController
{
    @Resource
    private BillingService m_billingService;
    
    @Resource
    private StandaloneMemberDao m_memberDao;
    
    @Resource
    private ClockService m_clockService;
    /*
    //@RequestMapping(value = "/billing/orders", method = RequestMethod.GET)
    public String showOrders(Model model, Principal principal) 
    {
        Collection<? extends Order> orders = m_billingService.getAllOrders();
        model.addAttribute("orders", orders);
        
        return "/WEB-INF/jsp/billing/showOrders.jsp";
    }
    
    //@RequestMapping(value = "/billing/unpaid", method = RequestMethod.GET)
    public String showUnpaidOrders(Model model, Principal principal) 
    {
        Collection<? extends Order> orders = m_billingService.getOrders(BillingStatus.INVOICED);
        
        Map<String,OrderGroup> orderMap = new HashMap<String, OldBillingController.OrderGroup>();
        
        for (Order order : orders)
        {
            String gln = order.getMemberGln();
            Member member = m_memberDao.getMemberByGln(gln);
            if (member != null)
            {
                OrderGroup group = orderMap.get(gln);
                if (group == null)
                {
                    group = new OrderGroup(member);
                    orderMap.put(gln, group);
                }
                group.addOrder(order);
            }
        }
        
        List<OrderGroup> sortedOrderGroups = new ArrayList<>(orderMap.values());
        Collections.sort(sortedOrderGroups, new Comparator<OrderGroup>(){
            @Override
            public int compare(OrderGroup o1, OrderGroup o2)
            {
                return -o1.getMostRecentInvoiceDate().compareTo(o2.getMostRecentInvoiceDate());
            }
        });
        
        for (OrderGroup orderGroup : sortedOrderGroups)
        {
            Collections.sort(orderGroup.getOrders(), new Comparator<Order>(){

                @Override
                public int compare(Order o1, Order o2)
                {
                    return -o1.getInvoiceDate().compareTo(o2.getInvoiceDate());
                }
                
            });
        }
        
        model.addAttribute("orderGroups", sortedOrderGroups);
        
        return "/WEB-INF/jsp/billing/showUnpaidOrders.jsp";
    }
    
    //@RequestMapping(value = "/billing/payall/{gln}", method = RequestMethod.GET)
    public String payAllGet(Model model, Principal principal, @PathVariable String gln) throws NoSuchResourceException
    {
        Member member = getMember(gln);
        
        Collection<? extends Order> orders = m_billingService.getOrders(gln, BillingStatus.INVOICED);
        
        Amount total = Amount.ZERO;
        for (Order order : orders)
        {
            total = total.add(order.getTotal());
        }
        
        PaymentCommand paymentCommand = new PaymentCommand();
        paymentCommand.setPaymentAmount(String.format("%.2f", total.getValue()));
        paymentCommand.setCurrency(total.getCurrency());
        
        model.addAttribute("member", member);
        model.addAttribute("orders", orders);
        model.addAttribute("total", total);
        model.addAttribute("paymentCommand", paymentCommand);
        
        return "/WEB-INF/jsp/billing/payAll.jsp";        
    }

    //@RequestMapping(value = "/billing/payall/{gln}", method = RequestMethod.POST)
    public String payAllPost(Model model, Principal principal, @PathVariable String gln, @ModelAttribute PaymentCommand paymentCommand) throws NoSuchResourceException, GlobalBrokerException
    {
        Member member = getMember(gln);
        
        // TODO: validation!
        BigDecimal paymentAmount = new BigDecimal(paymentCommand.getPaymentAmount());
        Amount payment = new Amount(paymentAmount, paymentCommand.getCurrency());
        
        GBAccount gbAccount = getGbService().getAccount(gln);
        
        String paymentId = paymentCommand.getPaymentId();
        PaymentReceipt paymentReceipt = getGbService().newPaymentReceipt(paymentId, m_clockService.now(), payment);
        
        Collection<? extends Order> orders = m_billingService.getOrders(gln, BillingStatus.INVOICED);
        List<String> invoiceIds = new ArrayList<>(orders.size());
        for (Order order : orders)
        {
            invoiceIds.add(order.getInvoiceId());
            m_billingService.enterPayment(order.getId(), paymentId);
        }
        
        getGbService().pay(gbAccount, paymentReceipt, invoiceIds);
        
        return "redirect:/ui/billing/unpaid";
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


    public static class OrderGroup
    {
        Member m_member;
        Date m_mostRecentInvoiceDate;
        Amount m_total;
        List<Order> m_orders;
        public OrderGroup(Member member)
        {
            super();
            m_member = member;
            m_orders = new ArrayList<>();
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
        public List<? extends Order> getOrders()
        {
            return m_orders;
        }
        public void addOrder(Order order)
        {
            m_orders.add(order);
            m_total = m_total.add(order.getTotal());
            if (m_mostRecentInvoiceDate == null || order.getInvoiceDate().compareTo(m_mostRecentInvoiceDate) < 0)
                m_mostRecentInvoiceDate = order.getInvoiceDate();

        }
        
    }
*/
}
    