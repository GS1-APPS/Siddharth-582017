package org.gs1us.sgl.webapp;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.NoSuchAccountException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@ControllerAdvice
@EnableWebMvc
public class GlobalControllerAdvice
{
    private static final Logger s_logger = Logger.getLogger("org.gs1us.sgl.webapp.GlobalControllerAdvice");
    
    @Resource
    private ClockService m_clockService;
    
    @ModelAttribute
    public void addClockService(Model model)
    {
        model.addAttribute("clockService", m_clockService);
    }
    
    @ExceptionHandler(NoSuchResourceException.class)
    public String handleNoSuchResourceException(NoSuchResourceException e)
    {
        return "/jsp/noSuchResource.jsp";
    }
    
    @ExceptionHandler(NoSuchAccountException.class)
    public String handleNoSuchAccountException(NoSuchAccountException e, HttpServletRequest request)
    {
        // This happens if we think the user is signed up (based on local information)
        // but there the global broker doesn't recognize the account.
        request.setAttribute("errorMessage", "There is a problem with your account. Please seek assistance from customer service.");
        return "/jsp/genericError.jsp";
    }
    
    @ExceptionHandler(NotSignedUpException.class)
    public String handleNotSignedUpException(NotSignedUpException e)
    {
        // Redirecting to home will cause the "you need to be signed up" message to be displayed.
        return "redirect:/ui/agreements";
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request)
    {
        Throwable cause = e.getCause();
        if (cause != null && cause instanceof FileUploadBase.SizeLimitExceededException)
        {
            FileUploadBase.SizeLimitExceededException ee = (FileUploadBase.SizeLimitExceededException)cause;
            request.setAttribute("sizeLimit", ee.getPermittedSize());
            request.setAttribute("uploadedSize", ee.getActualSize());
        }
        return "/jsp/fileTooLarge.jsp";
    }
    
    // doesn't seem to have an effect; instead see <error-page> in web.xml
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(Exception e)
    {
        return "/jsp/notFound.jsp";
    }
    
    // doesn't seem to have an effect; instead see <error-page> in web.xml
    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
    public String handleNoSuchRequestHandlingMethodException(Exception e)
    {
        return "/jsp/notFound.jsp";
    }
    
    @ExceptionHandler(GlobalBrokerException.class)
    public String handleGlobalBrokerException(HttpServletRequest request, GlobalBrokerException e)
    {
        s_logger.log(Level.SEVERE, "Global broker exception", e);
        request.setAttribute("errorMessage", "We're sorry, we cannot process your request at this time. Please try again later.");
        return "/jsp/genericError.jsp";
    }

    @ExceptionHandler(Exception.class)
    public String handleAnyException(Exception e)
    {
        s_logger.log(Level.SEVERE, "Internal error", e);
        return "/jsp/internalError.jsp";
    }
}
