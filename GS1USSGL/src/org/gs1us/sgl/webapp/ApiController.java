package org.gs1us.sgl.webapp;

import java.security.Principal;

import javax.transaction.Transactional;

import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Transactional
@RequestMapping("/ui")
public class ApiController
{
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String apiTop(Model model, Principal principal) throws GlobalBrokerException
    {
        model.addAttribute("text", "top level");
        return "/WEB-INF/jsp/api/test.jsp";
    }

    @RequestMapping(value = "/api/method1", method = RequestMethod.GET)
    public String apiM1(Model model, Principal principal) throws GlobalBrokerException
    {
        model.addAttribute("text", "method 1");
        return "/WEB-INF/jsp/api/test.jsp";
    }

    @RequestMapping(value = "/api/method2", method = RequestMethod.GET)
    public String apiM2(Model model, Principal principal) throws GlobalBrokerException
    {
        model.addAttribute("text", "method 2");
        return "/WEB-INF/jsp/api/test.jsp";
    }
}