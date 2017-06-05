package org.gs1us.sgl.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppDesc.Scope;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AttributeType;
import org.gs1us.sgg.gbservice.api.ModuleDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgl.memberservice.User;
import org.springframework.security.core.Authentication;

public class WebappUtil
{
    private static String s_productLogoFilename = "logo-us.png";
    private static String s_productLogoAlt = "GS1 US logo";
    private static String s_productOperator = "GS1 US";
    private static String s_productBrand = s_productOperator;
    private static String s_productCopyrightHolder = s_productOperator;
    private static String s_homeLinkText = s_productOperator + " Home";
    private static String s_homeLinkUrl = "http://www.gs1us.org";
    private static String s_shortProductName = "DWCode Portal";
    private static String s_longProductName = s_productBrand + " " + s_shortProductName;
    //private static String s_shortProductHtml = "<strong>DW</strong>Code Portal";    
    private static String s_shortProductHtml = "Cloud Phase1";    
    private static String s_longProductHtml = s_productBrand + " " + s_shortProductHtml;
    private static String s_valueAddedPhrase =  " and sign up for value added services such as DWCodes";
    private static String s_homeJsp = "gtinRegistryHome.jsp"; //"home.jsp";    
    private static String s_showProductsLine1AttributeName = "brandName";
    private static String s_showProductsLine2AttributeName = "productName";
    private static boolean s_showExperimentalFeatures = false;
    
    public static void setBranding(String operator, String homeLink, String productName, String productHtml, String logoFilename, 
            String valueAddedPhrase, String homeJsp, String showProductsLine1AttributeName, String showProductsLine2AttributeName,
            boolean showExperimentalFeatures)
    {
        s_productLogoFilename = logoFilename;
        s_productLogoAlt = operator + " logo";
        s_productOperator = operator;
        s_productBrand = s_productOperator;
        s_productCopyrightHolder = s_productOperator;
        s_homeLinkText = s_productOperator + " Home";
        s_homeLinkUrl = homeLink;
        s_shortProductName = productName;
        s_longProductName = s_productBrand + " " + s_shortProductName;
        s_shortProductHtml = productHtml;
        s_longProductHtml = s_productBrand + " " + s_shortProductHtml;
        s_valueAddedPhrase = valueAddedPhrase;
        s_homeJsp = homeJsp;
        s_showProductsLine1AttributeName = showProductsLine1AttributeName;
        s_showProductsLine2AttributeName = showProductsLine2AttributeName;
        s_showExperimentalFeatures = showExperimentalFeatures;
   }
    
    public static String productLogoFilename()
    {
        return s_productLogoFilename;
    }

    public static String productLogoAlt()
    {
        return s_productLogoAlt;
    }
    
    public static String productOperator()
    {
        return s_productOperator;
    }

    public static String productCopyrightHolder()
    {
        return s_productCopyrightHolder;
    }

    public static String productBrand()
    {
        return s_productBrand;
    }

    
    public static String homeLinkText()
    {
        return s_homeLinkText;
    }

    public static String homeLinkUrl()
    {
        return s_homeLinkUrl;
    }

    public static String shortProductName()
    {
        return s_shortProductName;
    }
    
    public static String longProductName()
    { 
        return s_longProductName;
    }
    
    public static String shortProductHtml()
    {
        return s_shortProductHtml;
    }
    
    public static String longProductHtml()
    {
        return s_longProductHtml;
    }
    
    public static String valueAddedPhrase()
    {
        if (s_valueAddedPhrase == null)
            return "";
        else
            return s_valueAddedPhrase;
    }
    
    public static String homeJsp()
    {
        return s_homeJsp;
    }
    
    public static String showProductsLine1AttributeName()
    {
        return s_showProductsLine1AttributeName;
    }

    public static String showProductsLine2AttributeName()
    {
        return s_showProductsLine2AttributeName;
    }
    
    

    public static boolean showExperimentalFeatures()
    {
        return s_showExperimentalFeatures;
    }

    public static void parseAttributeSetFormParametersForApps(HttpServletRequest request,
            Collection<? extends AppDesc> appDescs, AppDesc.Scope scope, String nullValue,
            AttributeSet attributes)
    {
        for (AppDesc appDesc : appDescs)
        {
           parseAttributeSetFormParametersForApp(request, appDesc, scope, nullValue,
                                              attributes);
        }
    }

    public static void parseAttributeSetFormParameters(HttpServletRequest request,
            Collection<? extends AppSubscription> subs, AppDesc.Scope scope, String nullValue,
            AttributeSet attributes)
    {
        for (AppSubscription sub : subs)
        {
           AppDesc appDesc = sub.getAppDesc();
           parseAttributeSetFormParametersForApp(request, appDesc, scope, nullValue,
                                              attributes);
        }
    }

    private static void parseAttributeSetFormParametersForApp(HttpServletRequest request, AppDesc appDesc, AppDesc.Scope scope, String nullValue, AttributeSet attributes)
    {
        String timeZoneId = "GMT";
        if (request.getUserPrincipal() != null)
        {
            User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
            timeZoneId = user.getTimezone();
        }

        ModuleDesc moduleDesc = appDesc.getModuleDesc(scope);
        if (moduleDesc != null)
        {
            AttributeDesc selectionAttrDesc = moduleDesc.getSelectionAttribute();

            // If not selectable, it is selected by default
            boolean isAppEnabled = true;
            if (selectionAttrDesc != null)
            {
                String value = (String)request.getParameter(selectionAttrDesc.getName());
                isAppEnabled = value != null;
                attributes.setBooleanAttribute(selectionAttrDesc, isAppEnabled);
            }

            for (AttributeDesc attrDesc : moduleDesc.getUserAttributeDescs())
            {
                String attrName = attrDesc.getName();
                String value = parseProductAttribute(request, attributes, isAppEnabled, attrDesc, attrName, nullValue, timeZoneId);

                if (attrDesc.getType() == AttributeType.MEASUREMENT)
                {
                    String uomAttrName = attrName + "_uom";
                    if (value == nullValue)
                        attributes.setAttribute(uomAttrName, nullValue);
                    else
                        parseProductAttribute(request, attributes, isAppEnabled, null, uomAttrName, nullValue, timeZoneId);
                }
            }
        }
    }
    
    private static String parseProductAttribute(HttpServletRequest request, 
                                                AttributeSet attributes, boolean isAppEnabled, 
                                                AttributeDesc attrDesc, String attrName, 
                                                String nullValue, String timeZoneId)
    {
        String value = UserInputUtil.trimToNull((String)request.getParameter(attrName));
        if (isAppEnabled && value != null)
        {
            if (attrDesc != null && attrDesc.getType() == AttributeType.DATE)
                convertDateAttribute(attributes, attrDesc, value, timeZoneId);
            else
                attributes.setAttribute(attrName, value);
            return value;
        }
        else
        {
            attributes.setAttribute(attrName, nullValue);
            return nullValue;
        }
    }

    private static void convertDateAttribute(AttributeSet attributes, AttributeDesc attrDesc, String value, String timeZoneId)
    {
        try
        {
            Date date = UserInputUtil.stringToDateOnly(value, timeZoneId);
            attributes.setDateAttribute(attrDesc, date);
        }
        catch (ParseException e)
        {
            // hack to cause a validation error message
            attributes.setAttribute(attrDesc, "baddate");
        }
    }
    
    public static Map<String,String[]> gatherAttributeSetFormParametersForApps(Collection<? extends AppDesc> appDescs, AppDesc.Scope scope, AttributeSet attributes, String timeZoneId)
    {
        Map<String,String[]> result = new HashMap<>();
        for (AppDesc appDesc : appDescs)
        {
           gatherAttributeSetFormParametersForApp(result, appDesc, scope, attributes, timeZoneId);
        }
        return result;
    }

    public static Map<String,String[]> gatherAttributeSetFormParameters(Collection<? extends AppSubscription> subs, AppDesc.Scope scope, AttributeSet attributes, String timeZoneId)
    {
        Map<String,String[]> result = new HashMap<>();
        for (AppSubscription sub : subs)
        {
           AppDesc appDesc = sub.getAppDesc();
           gatherAttributeSetFormParametersForApp(result, appDesc, scope, attributes, timeZoneId);
        }
        return result;
    }

    private static void gatherAttributeSetFormParametersForApp(Map<String,String[]> result, AppDesc appDesc, AppDesc.Scope scope, AttributeSet attributes, String timeZoneId)
    {
        if (attributes == null)
            return;
        
        ModuleDesc moduleDesc = appDesc.getModuleDesc(scope);
        if (moduleDesc != null)
        {
            AttributeDesc selectionAttrDesc = moduleDesc.getSelectionAttribute();

            // If not selectable, it is selected by default
            boolean isAppEnabled = true;
            if (selectionAttrDesc != null)
            {
                isAppEnabled = attributes.getBooleanAttribute(selectionAttrDesc);
                if (isAppEnabled)
                    result.put(selectionAttrDesc.getName(), new String[]{"true"});
            }

            for (AttributeDesc attrDesc : moduleDesc.getUserAttributeDescs())
            {
                String attrName = attrDesc.getName();
                String value = gatherProductAttribute(result, attributes, isAppEnabled, attrDesc, attrName, timeZoneId);

                if (attrDesc.getType() == AttributeType.MEASUREMENT)
                {
                    String uomAttrName = attrName + "_uom";
                    if (value != null)
                        gatherProductAttribute(result, attributes, isAppEnabled, null, uomAttrName, timeZoneId);
                }
            }
        }
    }
    
    private static String gatherProductAttribute(Map<String,String[]> result, 
                                                AttributeSet attributes, boolean isAppEnabled, 
                                                AttributeDesc attrDesc, String attrName, 
                                                String timeZoneId)
    {
        String value = attributes.getAttribute(attrName);
        if (isAppEnabled && value != null)
        {
            if (attrDesc != null && attrDesc.getType() == AttributeType.DATE)
                gatherDateAttribute(result, attributes, attrDesc, timeZoneId);
            else
                result.put(attrName, new String[]{value});
            return value;
        }
        else
            return null;
    }

    private static void gatherDateAttribute(Map<String,String[]> result, AttributeSet attributes, AttributeDesc attrDesc, String timeZoneId)
    {
        Date date = attributes.getDateAttribute(attrDesc);
        if (date != null)
            result.put(attrDesc.getName(), new String[]{UserInputUtil.dateOnlyToString(date, timeZoneId)});
    }

    public static Map<String, List<String>> errorListToMap(Collection<? extends ProductValidationError> l)
    {
        Map<String,List<String>> validationErrors = new HashMap<String, List<String>>();
        if (l != null)
        {
            for (ProductValidationError error : l)
            {
                String path = error.getPath();
                // TODO: uom hack
                if (path.endsWith("_uom"))
                    path = path.substring(0, path.length()-4);
                List<String> errorList = validationErrors.get(path);
                if (errorList == null)
                {
                    errorList = new ArrayList<>();
                    validationErrors.put(path, errorList);
                }
                errorList.add(error.getErrorMessage());
            }
        }
        return validationErrors;
    }

    public static String markdown(String s)
    {
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while (i < s.length())
        {
            char c = s.charAt(i);
            switch (c)
            {
            case '<':
                buf.append("&lt");
                i++;
                break;
                
            case '>':
                buf.append("&gt");
                i++;
                break;
                
            case '&':
                buf.append("&amp;");
                i++;
                break;
                
            case '[':
            {
                int next = processHyperlink(s, buf, i);
                if (next < 0)
                {
                    buf.append('[');
                    i++;
                }
                else
                    i = next;
            }
            break;
                
            default:
                buf.append(c);
                i++;
                break;
            }
        }
        return buf.toString();
    }

    private static int processHyperlink(String s, StringBuffer buf, int i)
    {
        if (i + 1 >= s.length() || s.charAt(i+1) != '[')
            return -1;
        
        int bar = s.indexOf('|', i+2);
        if (bar< 0)
            return -1;
        
        int end = s.indexOf("]]", bar+1);
        if (end < 0)
            return -1;
        
        String label = s.substring(i+2, bar).trim();
        String url = s.substring(bar+1, end).trim();
        
        buf.append("<a target=\"_blank\" href=\"");
        buf.append(url);
        buf.append("\">");
        buf.append(label);  // TODO: escape for URL
        buf.append("</a>");
        
        return end+2;
    }
}
