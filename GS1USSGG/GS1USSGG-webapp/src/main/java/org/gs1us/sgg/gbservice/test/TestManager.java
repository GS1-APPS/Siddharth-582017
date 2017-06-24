package org.gs1us.sgg.gbservice.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.gs1us.sgg.app.dwcode.AccountInternalRequest;
import org.gs1us.sgg.app.dwcode.AccountReadResponse;
import org.gs1us.sgg.app.dwcode.DigimarcException;
import org.gs1us.sgg.app.dwcode.DigimarcService;
import org.gs1us.sgg.app.dwcode.ProductDetailsReadResponse;
import org.gs1us.sgg.app.dwcode.ProductDetailsRequest;
import org.gs1us.sgg.app.dwcode.ServiceReadResponse;
import org.gs1us.sgg.app.dwcode.UserAccountInfo;
import org.gs1us.sgg.app.dwcode.UserAccountsReadResponse;
import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.Product;

public class TestManager
{
    private static Logger s_logger = Logger.getLogger("org.gs1us.sgg.gbservice.test.TestManager");

    @Resource
    private GBDao m_gbDao;
    
    @Resource
    private DigimarcService m_digimarcService;
    
    public String test(String testName, String testParam)
    {
        switch (testName)
        {
        case "dmVerify":
            return testDmVerify(testParam);
        case "dmDeleteAllAccounts":
            return testDmDeleteAllAccounts(testParam);
        default:
            return "No such test " + testName;
        }
    }

    private String testDmDeleteAllAccounts(String testParam)
    {
        try
        {
            UserAccountsReadResponse response = m_digimarcService.getAccounts();
            List<UserAccountInfo> accountInfos = new ArrayList<>(response.getAccounts());
            Collections.sort(accountInfos, new Comparator<UserAccountInfo>()
            {

                @Override
                public int compare(UserAccountInfo o1, UserAccountInfo o2)
                {
                    return o1.getCreateDate().compareTo(o2.getCreateDate());
                }
                
            });
            StringBuffer buf = new StringBuffer();
            buf.append(response.getAccounts().size() + " accounts:<table class=\"table\">");
            boolean first = true;
            for (UserAccountInfo info : accountInfos)
            {
                Integer accountId = info.getAccountId();
                String status = first ? "Initial account" : testDmDeleteAccount(accountId);
                buf.append("<tr>");
                element(buf, "td", String.valueOf(accountId));
                element(buf, "td", status);
                buf.append("</tr>");
                first = false;
            }
            buf.append("</table>");
            return buf.toString();
        }
        catch (DigimarcException e)
        {
            return e.getMessage();
        }
    }

    private String testDmDeleteAccount(Integer accountId)
    {

        try
        {
            AccountInternalRequest air = new AccountInternalRequest();
            air.setId(accountId);
            air.setOperation("delete");
            m_digimarcService.accountInternal(air);
            return "Deleted";
        }
        catch (DigimarcException e)
        {
            return "Error: " + e.getMessage();
        }

    }

    private String testDmVerify(String testParam)
    {
        StringBuffer buf = new StringBuffer();
        Set<Integer> accountIds = new HashSet<>();
        Set<Integer> serviceIds = new HashSet<>();
        
        buf.append("<table class=\"table\">");
        buf.append("<thead><tr><th>GLN</th><th>GTIN</th><th>Name</th><th>Status</th><th>DM ID</th></tr></thead><tbody>");
        
        Collection<? extends GBAccountRecord> unsortedAccounts = m_gbDao.getAllGBAccounts();
        List<? extends GBAccountRecord> accounts = new ArrayList<>(unsortedAccounts);
        Collections.sort(accounts, new Comparator<GBAccountRecord>()
        {

            @Override
            public int compare(GBAccountRecord o1, GBAccountRecord o2)
            {
                return o1.getGln().compareTo(o2.getGln());
            }
        });
        for (GBAccountRecord account : accounts)
        {
            testDmVerifyAccount(buf, accountIds, serviceIds, account);
        }
        
        buf.append("</tbody>");
        buf.append("</table>");
        return buf.toString();
    }

    private void testDmVerifyAccount(StringBuffer buf, Set<Integer> accountIds, Set<Integer> serviceIds, GBAccountRecord account)
    {
        String dmStatus = dmAccountStatus(accountIds, account);
        buf.append("<tr>");
        element(buf, "td", account.getGln());
        element(buf, "td", null);
        element(buf, "td", account.getName());
        element(buf, "td", dmStatus);
        element(buf, "td", String.valueOf(account.getAttributes().getAttribute("dmAccountId")));
        buf.append("</tr>");
        if (dmStatus.startsWith("DM "))
            testDmVerifyProducts(buf, serviceIds, account);
    }

    private void testDmVerifyProducts(StringBuffer buf, Set<Integer> serviceIds, GBAccountRecord account)
    {
        Collection<? extends Product> unsortedProducts = m_gbDao.getProductsByGln(account.getGln());
        List<? extends Product> products = new ArrayList<>(unsortedProducts);
        Collections.sort(products, new Comparator<Product>()
        {

            @Override
            public int compare(Product o1, Product o2)
            {
                return o1.getGtin().compareTo(o2.getGtin());
            }
        });
        
        for (Product product : products)
            testDmVerifyProduct(buf, serviceIds, account, product);
       
    }

    private void testDmVerifyProduct(StringBuffer buf, Set<Integer> serviceIds, GBAccountRecord account, Product product)
    {
        String dmAccountId = account.getAttributes().getAttribute("dmAccountId");

        String dmStatus = dmProductStatus(serviceIds, product, Integer.parseInt(dmAccountId));
        buf.append("<tr>");
        element(buf, "td", null);
        element(buf, "td", product.getGtin());
        buf.append("<td>");
        element(buf, "div", product.getAttributes().getAttribute("brandName"));
        element(buf, "div", product.getAttributes().getAttribute("productName"));
        buf.append("</td>");
        element(buf, "td", dmStatus);
        element(buf, "td", String.valueOf(product.getAttributes().getAttribute("dmServiceId")));
        buf.append("</tr>");
        
    }

    private String dmAccountStatus(Set<Integer> accountIds,
            GBAccountRecord account)
    {
        String dmAccountId = account.getAttributes().getAttribute("dmAccountId");
        String dmStatus;
        if (dmAccountId == null)
            dmStatus = "No DM Id";
        else
        {
            int accountId = Integer.parseInt(dmAccountId);
            if (accountIds.contains(accountId))
                dmStatus = "Duplicate DM id";
            else
            {
                accountIds.add(accountId);
            
                try
                {
                    AccountReadResponse accountResult = m_digimarcService.getAccount(accountId);
                    if (account.getName().equals(accountResult.getCompanyName()))
                        dmStatus = "DM synced";
                    else
                        dmStatus = "DM out-of-sync";
                }
                catch (DigimarcException e)
                {
                    e.printStackTrace();
                    dmStatus = "Not at DM";
                }
            }
        }
        return dmStatus;
    }
    private String dmProductStatus(Set<Integer> serviceIds, Product product, int accountId)
    {
        String dmServiceId = product.getAttributes().getAttribute("dmServiceId");
        String dmStatus;
        if (dmServiceId == null)
            dmStatus = "No DM Id";
        else
        {
            int serviceId = Integer.parseInt(dmServiceId);
            if (serviceIds.contains(serviceId))
                dmStatus = "Duplicate DM Id";
            else
            {
                serviceIds.add(serviceId);
            
                try
                {
                    ServiceReadResponse productResult = m_digimarcService.getService(serviceId, accountId);
                    TestSummarizer ts = compareProductToService(product, productResult);
                    if (ts.passed())
                        dmStatus = "DM synced";
                    else
                        dmStatus = "DM out-of-sync";
                }
                catch (DigimarcException e)
                {
                    s_logger.log(Level.WARNING, "Can't find service " + serviceId, e);
                    dmStatus = "Not at DM";
                }
            }
        }
        return dmStatus;
    }
    
    private TestSummarizer compareProductToService(Product product,
            ServiceReadResponse productResult)
    {
        TestSummarizer ts = new TestSummarizer();
        
        AttributeSet a = product.getAttributes();
        
        ProductDetailsReadResponse d = productResult.getProductDetails();
        if (d == null)
            ts.fail();
        else
        {


            ts.equals("gtin", product.getGtin(), productResult.getHumanReadableId());
            ts.equals("dmExperienceUrl", a.getAttribute("dmExperienceUrl"), emptyToNull(productResult.getPayoffUrl()));
            ts.equals("brandName", a.getAttribute("brandName"), emptyToNull(d.getBrand()));
            ts.equals("subBrand", a.getAttribute("subBrand"), emptyToNull(d.getSubBrand()));
            ts.equals("productName", a.getAttribute("productName"), emptyToNull(d.getProductName()));
            ts.equals("description", a.getAttribute("description"), emptyToNull(d.getDescription()));
            ts.equals("color", a.getAttribute("color"), emptyToNull(d.getColor()));
            ts.equals("flavor", a.getAttribute("flavor"), emptyToNull(d.getFlavor()));
            ts.equals("scent", a.getAttribute("scent"), emptyToNull(d.getScent()));
            ts.equals("size", a.getAttribute("size"), emptyToNull(d.getSize()));
            ts.equals("mobileDeviceImage", a.getAttribute("mobileDeviceImage"), emptyToNull(d.getImageUrl()));
        }

        
        return ts;
    }
    
    private String emptyToNull(String s)
    {
        if (s == null || s.length() == 0)
            return null;
        else
            return s;
    }

    private static class TestSummarizer
    {
        private static Logger s_logger = Logger.getLogger("org.gs1us.sgg.gbservice.test.TestSummarizer");
        private boolean m_pass = true;
        
        public void equals(String label, String expected, String actual)
        {
            boolean isEqual = expected == null ? actual == null : expected.equals(actual);
            m_pass &= isEqual;
            if (!isEqual)
                s_logger.warning(String.format("%s: expected %s got %s", label, expected, actual));
        }
        
        public void fail()
        {
            m_pass = false;
        }
        
        public boolean passed()
        {
            return m_pass;
        }
    }

    private void element(StringBuffer buf, String eltName, String text)
    {
        buf.append("<");
        buf.append(eltName);
        buf.append(">");
        text(buf, text);
        buf.append("</");
        buf.append(eltName);
        buf.append(">");
        
    }
    
    private void text(StringBuffer buf, String text)
    {
        if (text == null)
            return;
        
        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            switch (c)
            {
            case '<':
                buf.append("&lt;");
                break;
            case '>':
                buf.append("&gt;");
                break;
            case '&':
                buf.append("&amp;");
                break;
            default:
                buf.append(c);
                break;
            }
        }
    }
}
