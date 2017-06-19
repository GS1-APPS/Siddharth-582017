package org.gs1us.sgl.webapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GBAccountData;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.NoSuchInvoiceException;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgl.export.DelimRowColSink;
import org.gs1us.sgl.export.ExcelRowColSink;
import org.gs1us.sgl.export.RowColSink;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.MemberService;
import org.gs1us.sgl.memberservice.User;
import org.gs1us.sgl.memberservice.standalone.StandaloneMember;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@Transactional
@RequestMapping("/ui")
public class BillingReportController extends GBAwareController
{
    @Resource
    private StandaloneMemberDao m_memberDao;
    
    @Resource
    private ClockService m_clockService;
    
    @RequestMapping(value = "/billing/report", method = RequestMethod.GET)
    public String billingReportGet(Model model, Principal principal, @ModelAttribute ReportCommand reportCommand, BindingResult bindingResult) throws GlobalBrokerException
    {
        User user = (User)((Authentication)principal).getPrincipal();
        String timeZoneId = user.getTimezone();

        ReportDates reportDates = validateReportCommand(reportCommand, bindingResult, timeZoneId);
        
        if (reportDates != null)
        {
            model.addAttribute("fromDate", reportDates.getFromDate());
            model.addAttribute("toDate", reportDates.getToDate());
            
            PaidReport paidReport = createPaidReport(reportDates);
            model.addAttribute("paidReport", paidReport);
            
            PendingReport pendingReport = createPendingReport(reportDates);
            model.addAttribute("pendingReport", pendingReport);
        }
        
        return "/jsp/billing/report.jsp";
    }
    
    @RequestMapping(value = "/billing/report/paid", method = RequestMethod.GET)
    public void exportPaidReport(Model model, Principal principal, HttpServletResponse response, @RequestParam String fromDate, @RequestParam String toDate) throws IOException, NoSuchResourceException, GlobalBrokerException
    {
        User user = (User)((Authentication)principal).getPrincipal();
        String timeZoneId = user.getTimezone();
        
        ReportDates reportDates = validateReportDateParameters(fromDate, toDate, timeZoneId);

        String filename = String.format("report-paid-%tF-thru-%tF", reportDates.getFromDate(), reportDates.getToDate());
        
        PaidReport report = createPaidReport(reportDates);
        
        exportReport(report, filename, timeZoneId, response);

    }

    @RequestMapping(value = "/billing/report/pending", method = RequestMethod.GET)
    public void exportPendingReport(Model model, Principal principal, HttpServletResponse response, @RequestParam String fromDate, @RequestParam String toDate) throws IOException, NoSuchResourceException, GlobalBrokerException
    {
        User user = (User)((Authentication)principal).getPrincipal();
        String timeZoneId = user.getTimezone();
        
        ReportDates reportDates = validateReportDateParameters(fromDate, toDate, timeZoneId);

        String filename = String.format("report-pending-as-of-%tF", reportDates.getToDate());
        
        PendingReport report = createPendingReport(reportDates);
        
        exportReport(report, filename, timeZoneId, response);

    }

    private ReportDates validateReportDateParameters(String fromDate,
            String toDate, String timeZoneId) throws NoSuchResourceException
    {
        ReportCommand reportCommand = new ReportCommand();
        reportCommand.setDateRangeSelection("CUSTOM");
        reportCommand.setFromDate(fromDate);
        reportCommand.setToDate(toDate);
        
        ReportDates reportDates = validateReportCommand(reportCommand, null, timeZoneId);
        if (reportDates == null)
            throw new NoSuchResourceException();
        return reportDates;
    }

    private void exportReport(PaidReport report, String filename,
            String timeZoneId, HttpServletResponse response) throws IOException
    {
        if (false)
        {
            response.setContentType("application/text");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"");
            OutputStream outputStream = response.getOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);
            DelimRowColSink sink = new DelimRowColSink(writer);
            report.export(timeZoneId, sink);
            writer.flush();
            outputStream.close();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xlsx\"");
            OutputStream outputStream = response.getOutputStream();
            ExcelRowColSink sink = new ExcelRowColSink(outputStream);
            report.export(timeZoneId, sink);
            outputStream.close();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    
    private PaidReport createPaidReport(ReportDates reportDates) throws GlobalBrokerException
    {
        // this report includes all invoices where payment was received within the specified dates
        Collection<? extends Invoice> invoices = getInvoicesPaidBetween(reportDates.getFromDate(), reportDates.getToDate());

        PaidReport report = new PaidReport(invoices.size());
        report.addInvoices(invoices);
        
        return report;
    }

    private Collection<? extends Invoice> getInvoicesPaidBetween(Date fromDate, Date toDate) throws GlobalBrokerException
    {
        // TODO: do this more efficiently by passing dates to GB!
        Collection<? extends Invoice> paidInvoices = getGbService().getAllInvoices(OrderStatus.PAID);
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : paidInvoices)
        {
            Date paidDate = invoice.getPaidDate();
            if (paidDate.after(fromDate) && paidDate.before(toDate))
                result.add(invoice);
        }
        return result;
    }

    private PendingReport createPendingReport(ReportDates reportDates) throws GlobalBrokerException
    {
        // This report shows all orders entered before the report ending date that were not fully paid as of that date
        // So this includes:
        //   Invoices in the PAID state whose paid date is prior to report end date
        //   Invoices not yet in the PAID state (paid date is null) whose order date is prior to report end date
        //   Uninvoiced orders whose order date is prior to report end date

        Date asOfDate = reportDates.getToDate();
        Collection<? extends Invoice> invoices = getInvoicesWithOrdersUnpaidAsOf(asOfDate);
        PendingReport report = new PendingReport(invoices.size());
        for (Invoice invoice : invoices)
        {
            Collection<? extends SalesOrder> invoiceOrders = getGbService().getInvoiceOrders(invoice.getInvoiceId());
            if (allOrdersPrecede(invoiceOrders, asOfDate))
                report.addInvoice(invoice, invoiceOrders);
            else
            {
                for (SalesOrder order : invoiceOrders)
                {
                    if (order.getDate().before(asOfDate))
                        report.addUninvoicedOrder(order);
                }
            }
        }
        
        Collection<? extends SalesOrder> uninvoicedOrders = getGbService().getUninvoicedOrders();
        for (SalesOrder uninvoiced : uninvoicedOrders)
        {
            if (uninvoiced.getDate().before(asOfDate))
                report.addUninvoicedOrder(uninvoiced);
        }
        
        return report;

    }

    private boolean allOrdersPrecede(Collection<? extends SalesOrder> invoiceOrders, Date asOfDate)
    {
        for (SalesOrder order : invoiceOrders)
        {
            if (order.getDate().after(asOfDate))
                return false;
        }
        return true;
    }

    private Collection<? extends Invoice> getInvoicesWithOrdersUnpaidAsOf(Date asOfDate) throws GlobalBrokerException
    {
        // TODO: do this more efficiently by passing dates to GB!
        List<Invoice> result = new ArrayList<>();

        Collection<? extends Invoice> paidInvoices = getGbService().getAllInvoices(OrderStatus.PAID);
        for (Invoice invoice : paidInvoices)
        {
            Date paidDate = invoice.getPaidDate();
            if (paidDate.after(asOfDate))
                result.add(invoice);
        }

        Collection<? extends Invoice> paymentCommittedInvoices = getGbService().getAllInvoices(OrderStatus.PAYMENT_COMMITTED);
        result.addAll(paymentCommittedInvoices);
        
        Collection<? extends Invoice> billedInvoices = getGbService().getAllInvoices(OrderStatus.BILLED);
        result.addAll(billedInvoices);
        
        Collection<? extends Invoice> invoicedInvoices = getGbService().getAllInvoices(OrderStatus.INVOICED);
        result.addAll(invoicedInvoices);       

        return result;
    }

    private ReportDates validateReportCommand(ReportCommand reportCommand, BindingResult bindingResult, String timeZoneId)
    {
        if (reportCommand.getDateRangeSelection() == null)  // selection could be null if initial entry to page
            return null;
        
        switch (reportCommand.getDateRangeSelection())
        {
        case "CUSTOM":
        {
            Date fromDate = null;
            Date toDate = null;
            try
            {
                fromDate = snapDate(parseDateInput(reportCommand.getFromDate(), timeZoneId), null, null, null, 0, 0, 0);
            }
            catch (ParseException e)
            {
                if (bindingResult != null)
                    bindingResult.rejectValue("fromDate", "FromDateSyntax", "Enter a 'from' date in MM/DD/YYYY format");
            }
            try
            {
                toDate = snapDate(parseDateInput(reportCommand.getToDate(), timeZoneId), null, null, null, 23, 59, 59);
            }
            catch (ParseException e)
            {
                if (bindingResult != null)
                    bindingResult.rejectValue("toDate", "FromDateSyntax", "Enter a 'to' date in MM/DD/YYYY format");
            }
            if (fromDate != null && toDate != null)
            {
                if (fromDate.after(toDate))
                {
                    if (bindingResult != null)
                        bindingResult.rejectValue("toDate", "ToDateValue", "The 'to' date must be later than the 'from' date");
                }
                else
                    return new ReportDates(fromDate, toDate);
            }
            return null;
        }
        case "MONTH_TO_DATE":
        {
            Date now = m_clockService.now();
            Date fromDate = snapDate(now, null, null, 1, 0, 0, 0);
            Date toDate = snapDate(now, null, null, null, 23, 59, 59);
            return new ReportDates(fromDate, toDate);
        }
        case "YEAR_TO_DATE":
        {
            Date now = m_clockService.now();
            Date fromDate = snapDate(now, null, 0, 1, 0, 0, 0);
            Date toDate = snapDate(now, null, null, null, 23, 59, 59);
            return new ReportDates(fromDate, toDate);
        }
        case "LAST_MONTH":
        {
            Date now = m_clockService.now();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(now);
            cal.add(Calendar.MONTH, -1);
            Date lastMonth = cal.getTime();
            Date fromDate = snapDate(lastMonth, null, null, 1, 0, 0, 0);
            Date toDate = snapDate(lastMonth, null, null, cal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
            return new ReportDates(fromDate, toDate);
        }
        case "LAST_YEAR":
        {
            Date now = m_clockService.now();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(now);
            int thisYear = cal.get(Calendar.YEAR);
            Date fromDate = snapDate(now, thisYear - 1, 0, 1, 0, 0, 0);
            Date toDate = snapDate(now, thisYear - 1, 11, 31, 23, 59, 59);
            return new ReportDates(fromDate, toDate);
        }

        
        
        default:
            return null;
        }

    }
    
    
    
    private Date parseDateInput(String input, String timeZoneId) throws ParseException
    {
        String trimmed = UserInputUtil.trimToNull(input);
        if (trimmed == null)
            throw new ParseException(input, 0);
        Date result = UserInputUtil.stringToDateOnly(trimmed, timeZoneId);
        return result;
    }
    
    private static Date snapDate(Date d, Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        
        if (year != null)
            cal.set(Calendar.YEAR, year);
        
        if (month != null)
            cal.set(Calendar.MONTH, month);
        
        if (day != null)
            cal.set(Calendar.DAY_OF_MONTH, day);
        
        if (hour != null)
            cal.set(Calendar.HOUR_OF_DAY, hour);
        
        if (minute != null)
            cal.set(Calendar.MINUTE, minute);
        
        if (second != null)
            cal.set(Calendar.SECOND, second);
        
        return cal.getTime();
        
    }
    
    private static class ReportDates
    {
        private Date m_fromDate;
        private Date m_toDate;
        
        
        public ReportDates(Date fromDate, Date toDate)
        {
            super();
            m_fromDate = fromDate;
            m_toDate = toDate;
        }
        public Date getFromDate()
        {
            return m_fromDate;
        }
        public void setFromDate(Date fromDate)
        {
            m_fromDate = fromDate;
        }
        public Date getToDate()
        {
            return m_toDate;
        }
        public void setToDate(Date toDate)
        {
            m_toDate = toDate;
        }
        
        
    }

    public static class ReportCommand
    {
        private String m_dateRangeSelection;
        private String m_fromDate;
        private String m_toDate;
        public String getDateRangeSelection()
        {
            return m_dateRangeSelection;
        }
        public void setDateRangeSelection(String dateRangeSelection)
        {
            m_dateRangeSelection = dateRangeSelection;
        }
        public String getFromDate()
        {
            return m_fromDate;
        }
        public void setFromDate(String fromDate)
        {
            m_fromDate = fromDate;
        }
        public String getToDate()
        {
            return m_toDate;
        }
        public void setToDate(String toDate)
        {
            m_toDate = toDate;
        }
        
        
    }

    public class PaidReport
    {
        private List<Invoice> m_invoices;
        private Map<String,Member> m_memberMap;
        private Map<String,GBAccount> m_gbAccountMap;
        private Map<String,Collection<? extends SalesOrder>> m_orderMap;
        private Map<String,Collection<? extends SalesOrder>> m_uninvoicedOrderMap;
        
        public PaidReport(int size)
        {
            m_invoices = new ArrayList<>(size);
            m_memberMap = new HashMap<>();
            m_gbAccountMap = new HashMap<>();
            m_orderMap = new HashMap<>();
            m_uninvoicedOrderMap = new HashMap<>();
        }
        
        public List<Invoice> getInvoices()
        {
            return m_invoices;
        }
        
        public void addInvoices(Collection<? extends Invoice> invoices) throws GlobalBrokerException
        {
            for (Invoice invoice : invoices)
            {
                addInvoice(invoice, getGbService().getInvoiceOrders(invoice.getInvoiceId()));
            }
        }
        
        public void addInvoice(Invoice invoice, Collection<? extends SalesOrder> invoiceOrders)
        {
            m_invoices.add(invoice);
            String gln = invoice.getGBAccountGln();
            if (!containsGln(gln))
            {
                addGln(gln, m_memberDao.getMemberByGln(gln));
                try
                {
                    m_gbAccountMap.put(gln,  getGbService().getAccount(gln));
                }
                catch (GlobalBrokerException e)
                {
                }
            }
            m_orderMap.put(invoice.getInvoiceId(), invoiceOrders);
           
        }
        
        public void addUninvoicedOrder(SalesOrder order)
        {
            String gln = order.getGBAccountGln();
            Collection<SalesOrder> uninvoiced = (Collection<SalesOrder>)m_uninvoicedOrderMap.get(gln);
            if (uninvoiced == null)
            {
                uninvoiced = new ArrayList<SalesOrder>();
                m_uninvoicedOrderMap.put(gln, uninvoiced);
            }
             uninvoiced.add(order);
        }
        
        public boolean containsGln(String gln)
        {
            return m_memberMap.containsKey(gln);
        }

        public void addGln(String gln, StandaloneMember member)
        {
            m_memberMap.put(gln, member);
        }
        
        public Member getMember(String gln)
        {
            return m_memberMap.get(gln);
        }
        
        public Collection<? extends SalesOrder> getOrders(Invoice invoice)
        {
            return m_orderMap.get(invoice.getInvoiceId());
        }

        public Map<String,Collection<? extends SalesOrder>> getUninvoicedOrders()
        {
            return m_uninvoicedOrderMap;
        }
        
        public void export(String timeZoneId, RowColSink sink)
        {
            sink.heading("GLN");
            sink.heading("Member ID");
            sink.heading("Company Name");
            sink.heading("Address 1");
            sink.heading("Address 2");
            sink.heading("City");
            sink.heading("State");
            sink.heading("Postal Code");
            sink.heading("License Type");
            sink.heading("GTIN");
            sink.heading("Renewal Date");
            sink.heading("Order ID");
            sink.heading("Order Date");
            sink.heading("DWCode amount");
            sink.heading("Tax Jurisdiction");
            sink.heading("Tax Amount");
            sink.heading("Amount");
            sink.heading("Invoice ID");
            sink.heading("Invoice Date");
            sink.heading("Billed Date");
            sink.heading("Payment Committed Date");
            sink.heading("Paid Date");
            sink.endRow();
            
            for (Invoice invoice : m_invoices)
            {
                String gln = invoice.getGBAccountGln();
                Member member = getMember(gln);
                
                for (SalesOrder order : getOrders(invoice))
                {
                    for (OrderLineItem lineItem : order.getLineItems())
                    {
                        exportCompany(member, sink);
                        String gtin = lineItem.getItemParameters()[0];
                        exportGs1Key(gtin, sink);
                        sink.cell(formatRenewalDate(gln, gtin, timeZoneId));
                        sink.cell(order.getOrderId());
                        exportDate(order.getDate(), timeZoneId, sink);
                        exportAmount(lineItem.getTotal(), sink);
                        sink.cell();
                        sink.cell();
                        exportAmount(lineItem.getTotal(), sink);
                        sink.cell(invoice.getInvoiceId());
                        exportDate(invoice.getDate(), timeZoneId, sink);
                        exportDate(invoice.getBilledDate(), timeZoneId, sink);
                        exportDate(invoice.getPaymentCommittedDate(), timeZoneId, sink);
                        exportDate(invoice.getPaidDate(), timeZoneId, sink);
                        sink.endRow();
                    }
                }
                
                for (InvoiceExtra extra : invoice.getExtras())
                {
                    exportCompany(member, sink);
                    sink.cell();
                    sink.cell();
                    sink.cell();
                    sink.cell();
                    sink.cell();
                    sink.cell(extra.getItemParameters()[0]);
                    exportAmount(extra.getTotal(), sink);
                    exportAmount(extra.getTotal(), sink);
                    sink.cell(invoice.getInvoiceId());
                    exportDate(invoice.getDate(), timeZoneId, sink);
                    exportDate(invoice.getBilledDate(), timeZoneId, sink);
                    exportDate(invoice.getPaymentCommittedDate(), timeZoneId, sink);
                    exportDate(invoice.getPaidDate(), timeZoneId, sink);
                    sink.endRow();
                }
            }
            for (Map.Entry<String,Collection<? extends SalesOrder>> entry : getUninvoicedOrders().entrySet())
            {
                String gln = entry.getKey();
                Member member = getMember(gln);
                
                for (SalesOrder order : entry.getValue())
                {
                    for (OrderLineItem lineItem : order.getLineItems())
                    {
                        exportCompany(member, sink);
                        String gtin = lineItem.getItemParameters()[0];
                        sink.cell(gtin);
                        sink.cell(formatRenewalDate(gln, gtin, timeZoneId));
                        sink.cell(order.getOrderId());
                        exportDate(order.getDate(), timeZoneId, sink);
                        exportAmount(lineItem.getTotal(), sink);
                        sink.cell();
                        sink.cell();
                        exportAmount(lineItem.getTotal(), sink);
                        sink.cell();
                        sink.cell();
                        sink.cell();
                        sink.cell();
                        sink.cell();
                        sink.endRow();
                    }
                }
                
            }
            sink.finish();
        }

        private String formatRenewalDate(String gln, String gtin, String timeZoneId)
        {
            if (gtin == null)
                return null;
            Product product;
            try
            {
                product = getGbService().getProductByGtin(gln, gtin);
            }
            catch (GlobalBrokerException e)
            {
                return null;
            }
            if (product == null)
                return null;
            else if (product.getNextActionDate() != null)
            {
                StringBuffer buf = new StringBuffer();
                buf.append(UserInputUtil.dateOnlyToString(product.getNextActionDate(), timeZoneId));
                if (product.getPendingNextActionDate() != null)
                {
                    buf.append("\n(Pending: ");
                    buf.append(UserInputUtil.dateOnlyToString(product.getNextActionDate(), timeZoneId));
                    buf.append(")");
                }
                return buf.toString();
            }
            else if (product.getPendingNextActionDate() != null)
                return "(Pending payment)";
            else
                return null;

        }

        private void exportCompany(Member member, RowColSink sink)
        {
            String gln = member.getGln();
            exportGs1Key(gln, sink);
            sink.cell(member.getMemberId());
            sink.cell(member.getCompanyName());
            sink.cell(member.getAddress1());
            sink.cell(member.getAddress2());
            sink.cell(member.getCity());
            sink.cell(member.getState());
            sink.cell(member.getPostalCode());
            
            GBAccount gbAccount = m_gbAccountMap.get(gln);
            if (gbAccount == null)
                sink.cell();
            else
            {
                String licenseType = gbAccount.getAttributes().getAttribute("dmLicenseType");
                sink.cell(licenseType);
            }
        }
        
        private void exportGs1Key(String key, RowColSink sink)
        {
            sink.cell(key);
        }
        
        private void exportAmount(Amount amount, RowColSink sink)
        {
            sink.cell(amount.getValue());
        }
        
        private void exportDate(Date date, String timeZoneId, RowColSink sink)
        {
            if (date == null)
                sink.cell();
            else
                sink.cell(UserInputUtil.dateOnlyToString(date, timeZoneId));
        }

    }
    
    public class PendingReport extends PaidReport
    {

        public PendingReport(int size)
        {
            super(size);
        }
        
    }
}
    