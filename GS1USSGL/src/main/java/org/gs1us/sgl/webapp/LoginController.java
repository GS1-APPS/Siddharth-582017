package org.gs1us.sgl.webapp;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.gs1us.sgl.memberservice.standalone.StandaloneUser;
import org.gs1us.sgl.memberservice.standalone.StandaloneUserDao;
import org.gs1us.sgl.serviceterms.TermsOfService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional   // Only needed for standalone, to update the last login and login count
@RequestMapping("/ui/login")
public class LoginController 
{
    @Resource
    private StandaloneUserDao m_userDao;
    
    @Resource
    private TermsOfService m_termsOfService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String login(Model model)
    {
        model.addAttribute("termsOfService", m_termsOfService);
        return "/jsp/login.jsp";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String login(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request) 
    {
        try
        {
            request.login(username, password);
            
            StandaloneUser user = m_userDao.getUserByUsername(username);
            user.setLastLogin(new Date());
            user.setLoginCount(user.getLoginCount() + 1);
            m_userDao.updateUser(user);
            
            if (false) // m_termsOfService != null && !m_termsOfService.getVersion().equals(user.getTosVersion()))
                return "redirect:/ui/reagreeTos";
            else
                return "redirect:/ui/home";
            
        }
        catch (ServletException e)
        {
            request.setAttribute("errorMessage", e.getMessage());
            return "/jsp/login.jsp";
        }

    }
}
