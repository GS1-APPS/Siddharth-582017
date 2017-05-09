package org.gs1us.sgl.webapp;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgg.util.UserInputUtil;
import org.hibernate.id.IdentityGenerator.GetGeneratedKeysDelegate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
@RequestMapping("/ui")
public class TestController extends GBAwareController
{
    private static final DateFormat NOW_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    
    @Resource
    private ClockService m_clockService;
    
    @RequestMapping(value = "/test/settime", method = RequestMethod.GET)
    public String setTimeGet(Model model)
    {
        SetTimeCommand setTimeCommand = new SetTimeCommand();
        setTimeCommand.setNowString(NOW_FORMAT.format(m_clockService.now()));
        
        model.addAttribute("setTimeCommand", setTimeCommand);
        
        return "/WEB-INF/jsp/test/setTime.jsp";
    }
    
    @RequestMapping(value = "/test/settime", method = RequestMethod.POST)
    public String setTimePost(Model model, Principal principal, @ModelAttribute SetTimeCommand setTimeCommand) throws GlobalBrokerException
    {
        String nowString = UserInputUtil.trimToNull(setTimeCommand.getNowString());
        
        if (nowString == null)
        {
            m_clockService.setNow(null);
            getGbService().setTime(getUser(principal).getUsername(), null);
            return "redirect:/ui/home";
        }
        else
        {
            try
            {
                Date newNow = NOW_FORMAT.parse(nowString);
                m_clockService.setNow(newNow);
                getGbService().setTime(getUser(principal).getUsername(), newNow);
                return "redirect:/ui/home";
            }
            catch (ParseException e)
            {
                return "/WEB-INF/jsp/test/setTime.jsp";
            }
        }
    }
    
    public static class SetTimeCommand
    {
        private String m_nowString;

        public String getNowString()
        {
            return m_nowString;
        }

        public void setNowString(String nowString)
        {
            m_nowString = nowString;
        }
        
        
    }
    
    @RequestMapping(value = "/test/tick", method = RequestMethod.GET)
    public String tick(Model model, Principal principal, HttpServletRequest request) throws GlobalBrokerException
    {
        String referrer = request.getHeader("Referer");
        if (referrer == null)
            referrer = "/ui/home";
        
        Date newNow;
        if (m_clockService.isNowFixed())
            newNow = m_clockService.fromNow(60000);
        else
            newNow = (new GregorianCalendar(1962, 2, 15, 12, 01, 00)).getTime();
        m_clockService.setNow(newNow);
        getGbService().setTime(getUser(principal).getUsername(), newNow);

        return "redirect:" + referrer;
    }
    
    @RequestMapping(value = "/test/tomorrow", method = RequestMethod.GET)
    public String tomorrow(Model model, Principal principal, HttpServletRequest request) throws GlobalBrokerException
    {
        String referrer = request.getHeader("Referer");
        if (referrer == null)
            referrer = "/ui/home";
        
        Date newNow;
        if (m_clockService.isNowFixed())
            newNow = m_clockService.fromNow(86400000L);
        else
            newNow = (new GregorianCalendar(1962, 2, 15, 12, 01, 00)).getTime();
        m_clockService.setNow(newNow);
        getGbService().setTime(getUser(principal).getUsername(), newNow);

        return "redirect:" + referrer;
    }
    
    @RequestMapping(value = "/test/{testName}", method = RequestMethod.GET)
    public String testGet(Model model, Principal principal, @PathVariable String testName, @RequestParam(required=false) String testParam) throws GlobalBrokerException
    {
        String result = getGbService().test(null, testName, testParam);
        model.addAttribute("title", testName + " results");
        model.addAttribute("result", result);
        
        return "/WEB-INF/jsp/test/genericTest.jsp";
    }


}
