package org.gs1us.sgl.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ui/logout")
public class LogoutController 
{
    @RequestMapping(method = RequestMethod.GET)
    public String logout(HttpServletRequest request)
    {
        try
        {
            request.logout();
        }
        catch (ServletException e)
        {
        }
        return "redirect:/ui/home";
    }
 
}
