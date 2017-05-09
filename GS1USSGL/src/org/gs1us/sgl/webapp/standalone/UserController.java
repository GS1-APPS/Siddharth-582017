package org.gs1us.sgl.webapp.standalone;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.persistence.Temporal;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppDesc.Scope;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgg.gbservice.api.ValidationException;
import org.gs1us.sgg.gbservice.json.InboundAttributeSet;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgl.memberservice.User;
import org.gs1us.sgl.memberservice.standalone.StandaloneMember;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;
import org.gs1us.sgl.memberservice.standalone.StandaloneUser;
import org.gs1us.sgl.memberservice.standalone.StandaloneUserDao;
import org.gs1us.sgl.memberservice.standalone.UserState;
import org.gs1us.sgl.security.SecurityUtil;
import org.gs1us.sgl.serviceterms.TermsOfService;
import org.gs1us.sgl.webapp.AppController;
import org.gs1us.sgl.webapp.GBAwareController;
import org.gs1us.sgl.webapp.HomeController;
import org.gs1us.sgl.webapp.NoSuchResourceException;
import org.gs1us.sgl.webapp.SortOrder;
import org.gs1us.sgl.webapp.SortPageManager;
import org.gs1us.sgl.webapp.WebappUtil;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@Transactional
@RequestMapping("/ui")
public class UserController extends GBAwareController
{
    private static Logger s_logger = Logger.getLogger("org.gs1us.sgl.webapp.standalone.UserController");
    
    private static final String INITIAL_USER_SUBJECT = "Welcome to the " + WebappUtil.shortProductName();
    private static final String INITIAL_USER_EMAIL =
            "Congratulations, ${firstName}!\n\n"
            + "The ${longProductName} account for ${companyName} is set up. To access it, you will first need to create a password. "
            + "Select the link below to do this. Once completed, you will be brought to the login page for the ${shortProductName}. "
            + "This link is active for 24 hours, please set up your password within this timeframe. "
            + "Otherwise, select the Forgot Password link to have a new password link sent to you.\n\n"
            + "${passwordLink}\n\n"
            + "From here you can enter your product information and enable them for DWCodes. "
            + "Should you have any questions, contact us at ${supportEmail}.\n\n"
            + "Thank you for signing up ... and welcome!\n\n"
            + "The DWCode Team\n";
    
    private static final String ADDITIONAL_USER_SUBJECT = "Welcome to the " + WebappUtil.shortProductName();
    private static final String ADDITIONAL_USER_EMAIL =
            "Welcome, ${firstName}!\n\n"
            + "You have been added to the ${longProductName} account used by ${companyName}. To access it, you will first need to create a password. "
            + "Select the link below to do this. Once completed, you will be brought to the login page for the ${shortProductName}. "
            + "This link is active for 24 hours, please set up your password within this timeframe. "
            + "Otherwise, select the Forgot Password link to have a new password link sent to you.\n\n"
            + "${passwordLink}\n\n"
            + "From here you can enter your product information and enable them for DWCodes. "
            + "Should you have any questions, contact us at ${supportEmail}.\n\n"
            + "Again, welcome to the ${shortProductName}.\n\n"
            + "The DWCode Team\n";
    
    private static final String RESET_PASSWORD_SUBJECT = WebappUtil.shortProductName() + " password reset";
    private static final String RESET_PASSWORD_EMAIL = 
            "Dear ${firstName},\n\n"
            + "This email has been sent because this user account requested a password reset.\n\n"
            + "Select the link below to reset your password.  Once completed, you will be brought to the login page for the ${longProductName}. "
            + "This link is active for 24 hours, please set up your password within this timeframe. "
            + "Otherwise, select the Forgot Password link again to have a new password link sent to you.\n\n"
            + "${passwordLink}\n\n"
            + "The DWCode Team\n\n"
            + "(Please do not reply to this email.)\n";
    
    @Resource
    private StandaloneMemberDao m_memberDao;
    
    @Resource
    private StandaloneUserDao m_userDao;
    
    @Resource
    private MailSender m_mailSender;
    
    @Resource
    private SimpleMailMessage m_templateEmail;
    
    @Resource
    private PasswordEncoder m_passwordEncoder;
    
    private static final String DEFAULT_TIMEZONE = "US/Eastern";

    private static final long PASSWORD_RESET_EXPIRATION_INTERVAL_MILLIS = 86400000L;  // 24 hours
    
    /*
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGet(Model model)
    {
        UserCommand userCommand = new UserCommand();
        userCommand.setTimezone(DEFAULT_TIMEZONE);
        model.addAttribute("userCommand", userCommand);
        
        registerModelAttributes(model);
        model.addAttribute("cancelLink", "home");
        
        return "/WEB-INF/jsp/user/editUser.jsp";
    }

    private void registerModelAttributes(Model model)
    {
        model.addAttribute("heading", "Register to use the " + WebappUtil.longProductName());
        model.addAttribute("submitLabel", "Register");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(UserController.class, "registerPost", (Object)null, (Object)null, (Object)null).toUriString());
        model.addAttribute("editUsername", true);
        model.addAttribute("timezones", UserInputUtil.getTimeZoneIds());
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(Model model, @ModelAttribute UserCommand userCommand, BindingResult bindingResult)
    {
        String username = UserInputUtil.trimToNull(userCommand.getEmail());
        
        if (username == null || !UserInputUtil.isValidEmailAddress(username))
            bindingResult.rejectValue("email", "EmailRequired", "A valid e-mail address is required");
        
        StandaloneUser existingUser = m_userDao.getUserByUsername(username);
        // If state is CREATED, we need to allow re-registration in case they lost the password key
        if (existingUser != null && existingUser.getState() != UserState.CREATED)
        {
            bindingResult.rejectValue("email", "DuplicateUsername", "A " + WebappUtil.shortProductName() + " account already exists for " + username + ". If this is you, please log in. Otherwise, choose a different e-mail address to register.");
        }
        if (bindingResult.hasErrors())
        {
            registerModelAttributes(model);
            model.addAttribute("cancelLink", "home");
            return "/WEB-INF/jsp/user/editUser.jsp";
        }

        // We'll update the existing user if we got this far with one
        StandaloneUser user = existingUser != null ? existingUser : m_userDao.createUser();
        Date created = new Date();
        user.setUsername(username);
        userCommand.updateUserEditableFields(user);
        user.setCreated(created);
        user.setRoles(new String[]{"ROLE_USER"});
        user.setState(UserState.CREATED);
        user.setPasswordReset(SecurityUtil.generatePasswordResetKey());
        user.setPasswordResetExpiration(new Date(created.getTime() + PASSWORD_RESET_EXPIRATION_INTERVAL_MILLIS));
        m_userDao.updateUser(user);
        
        sendResetPasswordEmail(user);
       
        return "redirect:/ui/registered/" + user.getId();
    }
    */
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public String forgotPasswordGet(Model model)
    {
        model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(HomeController.class, "home", null, null).toUriString());
        return "/WEB-INF/jsp/user/forgotPassword.jsp";
    }

    
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public String forgotPasswordPost(Model model, @RequestParam String email)
    {
        StandaloneUser user = m_userDao.getUserByUsername(email);
        if (user != null)
        {
            user.setPasswordReset(SecurityUtil.generatePasswordResetKey());
            user.setPasswordResetExpiration(new Date(System.currentTimeMillis() + PASSWORD_RESET_EXPIRATION_INTERVAL_MILLIS));
            m_userDao.updateUser(user);
            sendResetPasswordEmail(user, RESET_PASSWORD_SUBJECT, RESET_PASSWORD_EMAIL);
        }
        else
        {
            // Otherwise, we still display the confirmation message, but we don't do anything. This way, we don't reveal if the
            // user has typed a wrong email address, for security reasons. However, we log it in case we need to figure out
            // why a legitimate customer is having difficulty
            s_logger.log(Level.INFO, String.format("Attempt to reset password for non-existent username %s", email));
        }
       
        return "redirect:/ui/forgotPasswordSent";
    }
    
    @RequestMapping(value = "/forgotPasswordSent", method = RequestMethod.GET)
    public String forgotPasswordSentGet(Model model)
    {
       
        return "/WEB-INF/jsp/user/forgotPasswordSent.jsp";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePasswordGet(Model model, Principal principal)
    {        
        StandaloneUser user = (StandaloneUser)((Authentication)principal).getPrincipal();

        model.addAttribute("user", user);
       
        return "/WEB-INF/jsp/user/changePassword.jsp";
    }

    
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePasswordPost(Model model, Principal principal)
    {
        StandaloneUser loggedInUser = (StandaloneUser)((Authentication)principal).getPrincipal();
        if (loggedInUser != null) // shouldn't happen because we have to be logged in
        {
            // Fetch user again to get it within a transaction, so that we can update it
            StandaloneUser user = m_userDao.getUser(loggedInUser.getId());
            user.setPasswordReset(SecurityUtil.generatePasswordResetKey());
            user.setPasswordResetExpiration(new Date(System.currentTimeMillis() + PASSWORD_RESET_EXPIRATION_INTERVAL_MILLIS));
            m_userDao.updateUser(user);
            
            sendResetPasswordEmail(user, RESET_PASSWORD_SUBJECT, RESET_PASSWORD_EMAIL);
        }

       
        return "redirect:/ui/changePasswordSent";
    }
    
    @RequestMapping(value = "/changePasswordSent", method = RequestMethod.GET)
    public String changePasswordSentGet(Model model, Principal principal)
    {
        StandaloneUser user = (StandaloneUser)((Authentication)principal).getPrincipal();

        model.addAttribute("user", user);
       
        return "/WEB-INF/jsp/user/changePasswordSent.jsp";
    }

    @RequestMapping(value = "/user/{id}/changePassword", method = RequestMethod.GET)
    public String changePasswordForUserGet(Model model, @PathVariable String id) throws NoSuchResourceException
    {        
        StandaloneUser user = populateUser(model, id);

        model.addAttribute("user", user);
       
        return "/WEB-INF/jsp/user/changePassword.jsp";
    }

    
    @RequestMapping(value = "/user/{id}/changePassword", method = RequestMethod.POST)
    public String changePasswordForUserPost(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneUser user = populateUser(model, id);
        if (user != null) // shouldn't happen because we have to be logged in
        {
            user.setPasswordReset(SecurityUtil.generatePasswordResetKey());
            user.setPasswordResetExpiration(new Date(System.currentTimeMillis() + PASSWORD_RESET_EXPIRATION_INTERVAL_MILLIS));
            m_userDao.updateUser(user);
            
            sendResetPasswordEmail(user, RESET_PASSWORD_SUBJECT, RESET_PASSWORD_EMAIL);
        }

       
        return "redirect:/ui/user/" + user.getId() + "/changePasswordSent";
    }
    
    @RequestMapping(value = "/user/{id}/changePasswordSent", method = RequestMethod.GET)
    public String changePasswordForUserSentGet(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneUser user = populateUser(model, id);

        model.addAttribute("user", user);
       
        return "/WEB-INF/jsp/user/changePasswordSent.jsp";
    }

    private void sendResetPasswordEmail(StandaloneUser user, String subject, String messageBodyTemplate)
    {
        try
        {
            String pwresetUrl = 
                    MvcUriComponentsBuilder.fromMethodName(UserController.class, "passwordResetGet", (Object)null, user.getPasswordReset()).toUriString();

            SimpleMailMessage message = new SimpleMailMessage(m_templateEmail);
            message.setTo(user.getUsername());
            message.setSubject(subject);

            String salutation = user.getFirstName() == null ? user.getUsername() : user.getFirstName();

            String text = 
                    messageBodyTemplate
                    .replace("${firstName}", salutation)
                    .replace("${longProductName}", WebappUtil.longProductName())
                    .replace("${shortProductName}", WebappUtil.shortProductName())
                    .replace("${companyName}", user.getMember().getCompanyName())
                    .replace("${passwordLink}", pwresetUrl)
                    .replace("${supportEmail}", message.getFrom());

            message.setText(text);
            m_mailSender.send(message);
        }
        catch (Exception e)
        {
            s_logger.log(Level.WARNING, "Unable to send e-mail", e);
        }
    }
    
    @RequestMapping(value = "/myAccount", method = RequestMethod.GET)
    public String myAccountGet(Model model, Principal principal)
    {
        StandaloneUser user = (StandaloneUser)((Authentication)principal).getPrincipal();
        UserCommand userCommand = new UserCommand();
        userCommand.setUserEditableFields(user);
        model.addAttribute("userCommand", userCommand);
        
        myAccountModelAttributes(model, user.getUsername());
        
        return "/WEB-INF/jsp/user/editUser.jsp";
    }

    private void myAccountModelAttributes(Model model, String username)
    {
        model.addAttribute("heading", "Edit information for user " + username);
        model.addAttribute("submitLabel", "Save");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(UserController.class, "myAccountPost", (Object)null, (Object)null, (Object)null, (Object)null).toUriString());
        model.addAttribute("resetPasswordLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "changePasswordGet", (Object)null, (Object)null).toUriString());
        model.addAttribute("editUsername", false);
        model.addAttribute("timezones", UserInputUtil.getTimeZoneIds());
        model.addAttribute("cancelLink", "home");
        model.addAttribute("termsOfService", null);

    }
    
    @RequestMapping(value = "/myAccount", method = RequestMethod.POST)
    public String myAccountPost(Model model, @ModelAttribute UserCommand userCommand, BindingResult bindingResult, Principal principal)
    {
        StandaloneUser user = (StandaloneUser)((Authentication)principal).getPrincipal();
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "company", "CompanyRequired", "Company is required");
        
        if (bindingResult.hasErrors())
        {
            myAccountModelAttributes(model, user.getUsername());
            return "/WEB-INF/jsp/user/editUser.jsp";
        }

        // Need to fetch the user within the transaction in order to get it to update;
        // the authentication principal is fetched outside the transaction
        StandaloneUser userToUpdate = m_userDao.getUser(user.getId());
        userCommand.updateUserEditableFields(userToUpdate);
        m_userDao.updateUser(userToUpdate);
        
        // But also update the user in the Authentication object, so that the welcome message is updated
        // and if the user edits again before logging out he will see the latest values
        userCommand.updateUserEditableFields(user);
        
        return "redirect:/ui/home";
    }
    
    @RequestMapping(value = "/user/{id}/edit", method = RequestMethod.GET)
    public String editUserGet(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneUser user = populateUser(model, id);
        
        UserCommand userCommand = new UserCommand();
        userCommand.setAdminEditableFields(user);

        model.addAttribute("userCommand", userCommand);
        
        editUserModelAttributes(model, user);
        
        return "/WEB-INF/jsp/user/editUser.jsp";
    }

    private void editUserModelAttributes(Model model, StandaloneUser user)
    {
        model.addAttribute("heading", "Edit information for user " + user.getUsername());
        model.addAttribute("submitLabel", "Save");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(UserController.class, "editUserPost", (Object)null, (Object)null, (Object)null, user.getId()).toUriString());
        model.addAttribute("resetPasswordLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "changePasswordForUserGet", (Object)null, user.getId()).toUriString());
        model.addAttribute("editUsername", false);
        model.addAttribute("timezones", UserInputUtil.getTimeZoneIds());
        UserState[] userStates = UserState.values();
        String[] userStateNames = new String[userStates.length];
        for (int i = 0; i < userStates.length; i++)
            userStateNames[i] = userStates[i].name();
        model.addAttribute("userStateNames", userStateNames);
        model.addAttribute("userTypeNames", UserType.getDisplayNames());
        model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "showUsers", null, null).toUriString());
        model.addAttribute("termsOfService", null);

    }
    
    @RequestMapping(value = "/user/{id}/edit", method = RequestMethod.POST)
    public String editUserPost(Model model, @ModelAttribute UserCommand userCommand, BindingResult bindingResult, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneUser user = populateUser(model, id);
       
        if (bindingResult.hasErrors())
        {
            editUserModelAttributes(model, user);
            return "/WEB-INF/jsp/user/editUser.jsp";
        }

        userCommand.updateAdminEditableFields(user);
        m_userDao.updateUser(user);
        
        
        return "redirect:/ui/user";
    }
    
    @RequestMapping(value = "/user/{id}/delete", method = RequestMethod.GET)
    public String deleteUserGet(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneUser user = populateUser(model, id);
        
        model.addAttribute("user", user);
        
        return "/WEB-INF/jsp/user/deleteUser.jsp";
    }

    @RequestMapping(value = "/user/{id}/delete", method = RequestMethod.POST)
    public String deleteUser(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneUser user = populateUser(model, id);

        m_userDao.deleteUser(user);
       
        
        return "redirect:/ui/user";
    }
    
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String showUsers(HttpServletRequest request, Model model) 
    {
        SortPageManager spm = new SortPageManager(request, MvcUriComponentsBuilder.fromMethodName(UserController.class, "showUsers", null, null));
        
        if (spm.getSortKey() == null)
            model.addAttribute("users", m_userDao.getAllUsers());
        else
            model.addAttribute("users", m_userDao.getAllUsers(spm.getSortKey(), spm.getSortDirection() == SortOrder.Direction.ASC ? +1 : -1));
        model.addAttribute("spm", spm);
        
        return "/WEB-INF/jsp/user/showUsers.jsp";
    }

    
    private StandaloneUser populateUser(Model model, String id) throws NoSuchResourceException
    {
        if (id == null)
            throw new IllegalArgumentException();
        
        StandaloneUser user = m_userDao.getUser(id);
        if (user == null)
            throw new NoSuchResourceException();
        
        model.addAttribute("user", user);
        return user;
    }

    
    @RequestMapping(value = "/registered/{id}", method = RequestMethod.GET)
    public String registered(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneUser user = populateUser(model, id);
        
        return "/WEB-INF/jsp/user/registered.jsp";
    }

    @RequestMapping(value = "/pwreset/{pwresetkey}", method = RequestMethod.GET)
    public String passwordResetGet(Model model, @PathVariable String pwresetkey) throws NoSuchResourceException
    {
        StandaloneUser user = m_userDao.getUserByPasswordReset(pwresetkey);
        
        if (user == null)
            throw new NoSuchResourceException();
        else if (System.currentTimeMillis() > user.getPasswordResetExpiration().getTime())
        {
            if (user.getState() == UserState.CREATED)
                model.addAttribute("errorMessage", "Your password reset link has expired. Please re-register your account to get a new link.");
            else
                model.addAttribute("errorMessage", "Your password reset link has expired. If you still want to reset your password, go back to \"My Account\"  or the \"Forgot Password\" link and try again.");
            return "/WEB-INF/jsp/genericError.jsp";
        }
        
        model.addAttribute("user", user);
        
        return "/WEB-INF/jsp/user/resetPassword.jsp";
    }
    
    @RequestMapping(value = "/pwreset/{pwresetkey}", method = RequestMethod.POST)
    public String passwordResetPost(Model model, @PathVariable String pwresetkey, @RequestParam("pw1") String pw1, @RequestParam("pw2") String pw2) throws NoSuchResourceException
    {
        StandaloneUser user = m_userDao.getUserByPasswordReset(pwresetkey);
        
        if (user == null)
            throw new NoSuchResourceException();
        else if (System.currentTimeMillis() > user.getPasswordResetExpiration().getTime())
        {
            if (user.getState() == UserState.CREATED)
                model.addAttribute("errorMessage", "Your password reset link has expired. Please re-register your account to get a new link.");
            else
                model.addAttribute("errorMessage", "Your password reset link has expired. If you still want to reset your password, go back to \"My Account\"  or the \"Forgot Password\" link and try again.");
            return "/WEB-INF/jsp/genericError.jsp";
        }
        
        String pw1trimmed = UserInputUtil.trimToNull(pw1);
        String pw2trimmed = UserInputUtil.trimToNull(pw2);
        
        // If a password is missing or improperly formatted, better to say this first than to report a mismatch
        if (!isAcceptablePassword(pw1trimmed) || !isAcceptablePassword(pw2trimmed))
        {
            model.addAttribute("errorMessage", "Your password must be at least 7 characters, and contain at least three of (a) a digit; (b) an uppercase letter; (c) a lowercase letter; and (d) a special character");
            return "/WEB-INF/jsp/user/resetPassword.jsp";            
        }
        else if (!pw1trimmed.equals(pw2trimmed))
        {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "/WEB-INF/jsp/user/resetPassword.jsp";            
        }
        else
        {
            String encodedPassword = m_passwordEncoder.encode(pw1trimmed);
            if (user.getState() == UserState.CREATED)
                user.setState(UserState.ACTIVE);
            user.setPassword(encodedPassword);
            user.setPasswordReset(null);
            user.setPasswordResetExpiration(null);
            m_userDao.updateUser(user);
            
            model.addAttribute("errorMessage", "Your password has been reset. Please log in below.");
            return "/WEB-INF/jsp/login.jsp";
        }
            
    }

    
    private boolean isAcceptablePassword(String pw)
    {
        if (pw == null || pw.length() < 7)
            return false;
        
        boolean hasDigit = false;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasOther = false;
        
        for (int i = 0; i < pw.length(); i++)
        {
            char c = pw.charAt(i);
            if (Character.isDigit(c))
                hasDigit = true;
            else if (Character.isUpperCase(c))
                hasUpper = true;
            else if (Character.isLowerCase(c))
                hasLower = true;
            else
                hasOther = true;
        }
        int count = 0;
        if (hasDigit)
            count++;
        if (hasUpper)
            count++;
        if (hasLower)
            count++;
        if (hasOther)
            count++;
        
        return count >= 3;
    }


    public static class UserCommand
    {
        private String m_email;
        private String m_firstName;
        private String m_lastName;
        private String m_timezone;
        private String m_userStateName;
        private String m_userTypeName;
        
        public String getEmail()
        {
            return m_email;
        }
        public void setEmail(String email)
        {
            m_email = email;
        }
        public String getFirstName()
        {
            return m_firstName;
        }
        public void setFirstName(String firstName)
        {
            m_firstName = firstName;
        }
        public String getLastName()
        {
            return m_lastName;
        }
        public void setLastName(String lastName)
        {
            m_lastName = lastName;
        }
        public String getTimezone()
        {
            return m_timezone;
        }
        public void setTimezone(String timezone)
        {
            m_timezone = timezone;
        }
        
        public String getUserStateName()
        {
            return m_userStateName;
        }
        public void setUserStateName(String userStateName)
        {
            m_userStateName = userStateName;
        }
        public String getUserTypeName()
        {
            return m_userTypeName;
        }
        public void setUserTypeName(String userTypeName)
        {
            m_userTypeName = userTypeName;
        }
        

        public void setUserEditableFields(StandaloneUser user)
        {
            setFirstName(user.getFirstName());
            setLastName(user.getLastName());
            setTimezone(user.getTimezone());
        }
        public void updateUserEditableFields(StandaloneUser user)
        {
            String firstName = UserInputUtil.trimToNull(getFirstName());
            String lastName = UserInputUtil.trimToNull(getLastName());
            String timezone = getTimezone();
            
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setTimezone(timezone);

        }
        public void setAdminEditableFields(StandaloneUser user)
        {
            setUserEditableFields(user);
            UserType userType = UserType.getByRole(user.getRoles());
            if (userType != null)
                setUserTypeName(userType.getDisplayName());
            setUserStateName(user.getState().name());
        }
        public void updateAdminEditableFields(StandaloneUser user)
        {
            updateUserEditableFields(user);
            user.setRoles(UserType.getByDisplayName(getUserTypeName()).getRoles());
            user.setState(UserState.valueOf(getUserStateName()));
        }
        
    }
    
    public static class UserType
    {
        public static UserType USER = new UserType("User", "ROLE_USER");
        //public static UserType CURATOR = new UserType("Curator", "ROLE_USER", "ROLE_CURATOR");
        public static UserType ADMIN = new UserType("Administrator", "ROLE_USER", /* "ROLE_CURATOR", */ "ROLE_ADMIN");
        
        private static UserType[] s_userTypes = {USER, /*CURATOR, */ ADMIN};
        
        public static String[] getDisplayNames()
        {
            String[] result = new String[s_userTypes.length];
            for (int i = 0; i < s_userTypes.length; i++)
            {
                UserType userType = s_userTypes[i];
                result[i] = userType.getDisplayName();
            }
            return result;
        }
        
        public static UserType getByDisplayName(String displayName)
        {
            for (UserType userType : s_userTypes)
            {
                if (userType.getDisplayName().equals(displayName))
                    return userType;
            }
            return null;
        }
        
        public static UserType getByRole(String[] roles)
        {
            // Relies on the order in which roles are stored within a user
            for (UserType userType : s_userTypes)
            {
                if (Arrays.equals(userType.getRoles(), roles))
                    return userType;
            }
            return null;
        }
        
        private String m_displayName;
        private String[] m_roles;
        
        private UserType(String displayName, String... roles)
        {
            m_displayName = displayName;
            m_roles = roles;
        }

        public String getDisplayName()
        {
            return m_displayName;
        }

        public String[] getRoles()
        {
            return m_roles;
        }
        
        
    }
    
    // The bootstrap mechanism allows for the creation of an initial user when there are no users in the DB
    // It's basically identical to /member/new, but at a different URL so that it can be executed without being logged in
    // It only functions if the user database is empty
    @RequestMapping(value = "/bootstrap", method = RequestMethod.GET)
    public String bootstrapGet(Model model) throws GlobalBrokerException
    {
        List<StandaloneUser> users = m_userDao.getAllUsers();
        if (users == null || users.size() == 0)
        {
            String view = newMemberGet(model);
            model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(UserController.class, "bootstrapPost", (Object)null, (Object)null, (Object)null, (Object)null, (Object)null).toUriString());
            return view;
        }
        else
            return "/WEB-INF/jsp/notFound.jsp";
    }
    
    // The bootstrap mechanism allows for the creation of an initial user when there are no users in the DB
    // It's basically identical to /member/new, but at a different URL so that it can be executed without being logged in
    // It only functions if the user database is empty
    @RequestMapping(value = "/bootstrap", method = RequestMethod.POST)
    public String bootstrapPost(HttpServletRequest request, Model model, Principal principal, @ModelAttribute MemberAndUserCommand memberAndUserCommand, BindingResult bindingResult) throws GlobalBrokerException
    {
        List<StandaloneUser> users = m_userDao.getAllUsers();
        if (users == null || users.size() == 0)
        {
            return newMemberPost(request, model, principal, memberAndUserCommand, bindingResult);
        }
        else
            return "/WEB-INF/jsp/notFound.jsp";
    }
    
    @RequestMapping(value = "/member/new", method = RequestMethod.GET)
    public String newMemberGet(Model model) throws GlobalBrokerException
    {
        MemberAndUserCommand memberAndUserCommand = new MemberAndUserCommand();
        memberAndUserCommand.getUser().setTimezone(DEFAULT_TIMEZONE);
        model.addAttribute("memberAndUserCommand", memberAndUserCommand);
        
        newMemberModelAttributes(model, null, null, null);
        model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null).toUriString());
        
        return "/WEB-INF/jsp/user/newMember.jsp";
    }

    private void newMemberModelAttributes(Model model, AttributeSet attributes, List<ProductValidationError> errorList, String timeZoneId) throws GlobalBrokerException
    {
        model.addAttribute("heading", "Add a new member and initial user to the " + WebappUtil.longProductHtml());
        model.addAttribute("submitLabel", "Add");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(UserController.class, "newMemberPost", (Object)null, (Object)null, (Object)null, (Object)null, (Object)null).toUriString());
        model.addAttribute("editUsername", true);  // TODO: needed?
        model.addAttribute("timezones", UserInputUtil.getTimeZoneIds());
        
        editMemberAttributesModelAttributes(model, attributes, Action.CREATE, errorList, timeZoneId);
    }
    
    @RequestMapping(value = "/member/new", method = RequestMethod.POST)
    public String newMemberPost(HttpServletRequest request, Model model, Principal principal, @ModelAttribute MemberAndUserCommand memberAndUserCommand, BindingResult bindingResult) throws GlobalBrokerException
    {
        String timeZoneId;
        // principal can be null if this is a bootstrap request
        if (principal == null)
            timeZoneId = "GMT";
        else
        {
            User user = (User)((Authentication)principal).getPrincipal();
            timeZoneId = user.getTimezone();
        }

        MemberCommand memberCommand = memberAndUserCommand.getMember();
        UserCommand userCommand = memberAndUserCommand.getUser();
        
        String bindingPrefix = "member.";
        memberCommand.validateAdminEditableFields(bindingResult, bindingPrefix);
        
        String username = UserInputUtil.trimToNull(userCommand.getEmail());
        StandaloneUser existingUser = validateOptionalUserForm(userCommand, bindingResult, username, "user.");
        
        Collection<? extends AppDesc> appDescs = getGbService().getAppDescs(null);
        AttributeSet attributes = new InboundAttributeSet();
        attributes.setAttributes(new HashMap<String, String>());
        WebappUtil.parseAttributeSetFormParametersForApps(request, appDescs, Scope.ACCOUNT, "", attributes);
        
        if (bindingResult.hasErrors())
        {
            newMemberModelAttributes(model, attributes, null, timeZoneId);
            model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null).toUriString());
            return "/WEB-INF/jsp/user/newMember.jsp";
        }
        
        try
        {
            GBAccount gbAccount = memberCommand.toGbAccount(getGbService(), attributes);
            // principal can be null if this is a bootstrap
            String principalUsername = principal == null ? null : getUser(principal).getUsername();
            getGbService().putAccount(principalUsername, gbAccount, Action.CREATE);
            if ("DMCustomer".equals(attributes.getAttribute("dmLicenseType")))
                autoSubscribeDigimarc(principal, gbAccount);
        }
        catch (ValidationException e)
        {
            updateBindingResultFromValidationException(bindingResult, "member.", e);
            newMemberModelAttributes(model, attributes, e.getErrors(), timeZoneId);
            model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null).toUriString());
            return "/WEB-INF/jsp/user/newMember.jsp";
        }

        StandaloneMember member = m_memberDao.createMember();
        member.setGln(UserInputUtil.trimToNull(memberCommand.getGln()));
        memberCommand.updateAdminEditableFields(member);

        StandaloneUser newUser = null;
        if (username != null)
            newUser = createOrUpdateUser(existingUser, member,username, userCommand);

        m_memberDao.updateMember(member);
       
        if (newUser != null)
            sendResetPasswordEmail(newUser, INITIAL_USER_SUBJECT, INITIAL_USER_EMAIL);

        if (newUser == null)
            return "redirect:/ui/member";
        else
            return "redirect:/ui/registered/" + newUser.getId();
    }

    private void autoSubscribeDigimarc(Principal principal, GBAccount gbAccount) throws GlobalBrokerException
    {
        String gln = gbAccount.getGln();

        AppSubscription sub = getGbService().newAppSubscription("dwcode", null);
        getGbService().createAppSubscription(getUser(principal).getUsername(), gln, sub);
        
        
    }


    private StandaloneUser createOrUpdateUser(StandaloneUser existingUser,
            StandaloneMember member, String username, UserCommand userCommand)
    {
        StandaloneUser user = existingUser == null ? m_userDao.createUser() : existingUser;
        userCommand.updateUserEditableFields(user);
        user.setMember(member);
        Date created = new Date();
        user.setUsername(username.toLowerCase());
        user.setCreated(created);
        user.setRoles(new String[]{"ROLE_USER"});
        user.setState(UserState.CREATED);
        user.setPasswordReset(SecurityUtil.generatePasswordResetKey());
        user.setPasswordResetExpiration(new Date(created.getTime() + PASSWORD_RESET_EXPIRATION_INTERVAL_MILLIS));
        m_userDao.updateUser(user);
        return user;
    }

    private StandaloneUser validateOptionalUserForm(UserCommand userCommand, BindingResult bindingResult, String username, String bindingPrefix)
    {
        if (username == null)
        {
            if (UserInputUtil.trimToNull(userCommand.getFirstName()) != null || UserInputUtil.trimToNull(userCommand.getLastName()) != null)
            {
                bindingResult.rejectValue(bindingPrefix + "email", "EmailRequired", "A valid e-mail address is required to create a user account");
            }
            return null;
        }
        else
            return validateUserForm(bindingResult, username, bindingPrefix);
    }

    private StandaloneUser validateUserForm(BindingResult bindingResult, String username, String bindingPrefix)
    {
        if (username == null || !UserInputUtil.isValidEmailAddress(username))
        {
            bindingResult.rejectValue(bindingPrefix + "email", "EmailRequired", "A valid e-mail address is required");
            return null;
        }
        
        StandaloneUser existingUser = m_userDao.getUserByUsername(username);
        // If state is CREATED, we need to allow re-registration in case they lost the password key
        if (existingUser != null && existingUser.getState() != UserState.CREATED)
        {
            bindingResult.rejectValue(bindingPrefix + "email", "DuplicateUsername", "A " + WebappUtil.shortProductName() + " account already exists for " + username + ".");
        }
        return existingUser;
    }
    

   
    private StandaloneMember populateMember(Model model, String id) throws NoSuchResourceException
    {
        if (id == null)
            throw new IllegalArgumentException();
        
        StandaloneMember member = m_memberDao.getMember(id);
        if (member == null)
            throw new NoSuchResourceException();
        
        model.addAttribute("member", member);
        return member;
    }

    
    @RequestMapping(value = "/member/{id}/edit", method = RequestMethod.GET)
    public String editMemberGet(Model model, Principal principal, @PathVariable String id) throws NoSuchResourceException, GlobalBrokerException
    {
        User user = (User)((Authentication)principal).getPrincipal();
        String timeZoneId = user.getTimezone();

        StandaloneMember member = populateMember(model, id);
        
        MemberCommand memberCommand = new MemberCommand();
        memberCommand.setAdminEditableFields(member);

        model.addAttribute("memberCommand", memberCommand);
        
        GBAccount account = getGbService().getAccount(member.getGln());
        AttributeSet attributes = account == null ? null : account.getAttributes();
        
        editMemberModelAttributes(model, member, attributes, null, timeZoneId);
        
        return "/WEB-INF/jsp/user/editMember.jsp";
    }

    private void editMemberModelAttributes(Model model, StandaloneMember member, AttributeSet attributes, List<ProductValidationError> errorList, String timeZoneId) throws GlobalBrokerException
    {
        model.addAttribute("heading", "Edit information for member " + member.getCompanyName());
        model.addAttribute("submitLabel", "Save");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(UserController.class, "editMemberPost", (Object)null, (Object)null, (Object)null, (Object)null, member.getId(), (Object)null).toUriString());
        model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null).toUriString());

        Action action = Action.UPDATE;

        editMemberAttributesModelAttributes(model, attributes, action, errorList, timeZoneId);
    }

    /**
     * Sets up the model attributes needed for editing the attributes part of a member record.
     */
    private void editMemberAttributesModelAttributes(Model model, AttributeSet attributes, Action action, List<ProductValidationError> errorList, String timeZoneId)
        throws GlobalBrokerException
    {
        Collection<? extends AppDesc> appDescs = getGbService().getAppDescs(null);
        AppDesc.Scope scope = Scope.ACCOUNT;
        Map<String,List<String>> validationErrors = WebappUtil.errorListToMap(errorList);
        
        model.addAttribute("appDescs", appDescs);
        model.addAttribute("scope", scope);
        model.addAttribute("validationErrors", validationErrors);
        model.addAttribute("parameters", WebappUtil.gatherAttributeSetFormParametersForApps(appDescs, scope, attributes, timeZoneId));
        model.addAttribute("action", action);
    }
    
    
    @RequestMapping(value = "/member/{id}/edit", method = RequestMethod.POST)
    public String editMemberPost(Model model, 
                                 Principal principal, 
                                 @ModelAttribute MemberCommand memberCommand, 
                                 BindingResult bindingResult, 
                                 @PathVariable String id, 
                                 HttpServletRequest request) throws NoSuchResourceException, GlobalBrokerException
    {
        User user = (User)((Authentication)principal).getPrincipal();
        String timeZoneId = user.getTimezone();


        StandaloneMember member = populateMember(model, id);
        
        memberCommand.setGln(member.getGln());
        memberCommand.validateAdminEditableFields(bindingResult, "");
        
        Collection<? extends AppDesc> appDescs = getGbService().getAppDescs(null);
        AttributeSet attributes = new InboundAttributeSet();
        attributes.setAttributes(new HashMap<String, String>());
        WebappUtil.parseAttributeSetFormParametersForApps(request, appDescs, Scope.ACCOUNT, "", attributes);
        
        if (bindingResult.hasErrors())
        {
            editMemberModelAttributes(model, member, attributes, null, timeZoneId);
            return "/WEB-INF/jsp/user/editMember.jsp";
        }

        try
        {
            GBAccount existingGbAccount = getGbService().getAccount(member.getGln());
            GBAccount gbAccount = memberCommand.toGbAccount(getGbService(), attributes);

            getGbService().putAccount(getUser(principal).getUsername(), gbAccount, null);

            // If there wasn't previously a corresponding GB account, reset the state that says the brand
            // owner agreement was accepted. This is mainly to support testing, where we clear out the 
            // global broker database (but not the local database) to reset to a state with fresh accounts.
            if (existingGbAccount == null)
            {
                member.setBrandOwnerAgreementSignedByUser(null);
                member.setBrandOwnerAgreementSignedDate(null);
            }
            memberCommand.updateAdminEditableFields(member);
            m_memberDao.updateMember(member);
        }
        catch (ValidationException e)
        {
            updateBindingResultFromValidationException(bindingResult, "", e);
            editMemberModelAttributes(model, member, attributes, e.getErrors(), timeZoneId);
            return "/WEB-INF/jsp/user/editMember.jsp";
        }
        catch (GlobalBrokerException e)
        {
            // TODO what?
        }
        
        
        return "redirect:/ui/member";
    }
    
    private void updateBindingResultFromValidationException(
            BindingResult bindingResult, String bindingPrefix, ValidationException e)
    {
        for (ProductValidationError pve : e.getErrors())
        {
            switch (pve.getPath())
            {
            case "gcps":
                bindingResult.rejectValue(bindingPrefix + "gcps", "GcpError", pve.getErrorMessage());

            }
        }
        
    }

    @RequestMapping(value = "/member/{id}/delete", method = RequestMethod.GET)
    public String deleteMemberGet(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneMember member = populateMember(model, id);
        
        model.addAttribute("member", member);
        
        return "/WEB-INF/jsp/user/deleteMember.jsp";
    }

    @RequestMapping(value = "/member/{id}/delete", method = RequestMethod.POST)
    public String deleteMember(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneMember member = populateMember(model, id);

        m_memberDao.deleteMember(member);
        
        // TODO: cascade? to standalone user and also GB
       
        
        return "redirect:/ui/member";
    }
    
    @RequestMapping(value = "/member/{id}/newUser", method = RequestMethod.GET)
    public String memberNewUserGet(Model model, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneMember member = populateMember(model, id);

        UserCommand userCommand = new UserCommand();
        userCommand.setTimezone(DEFAULT_TIMEZONE);
        model.addAttribute("userCommand", userCommand);
        
        memberNewUserModelAttributes(model, member);
        
        return "/WEB-INF/jsp/user/editUser.jsp";
    }

    private void memberNewUserModelAttributes(Model model, StandaloneMember member)
    {
        model.addAttribute("heading", "Register a new user for member " + member.getCompanyName());
        model.addAttribute("submitLabel", "Register");
        model.addAttribute("actionUrl", MvcUriComponentsBuilder.fromMethodName(UserController.class, "memberNewUserPost", (Object)null, (Object)null, (Object)null, member.getId()).toUriString());
        model.addAttribute("cancelLink", MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null).toUriString());
        model.addAttribute("editUsername", true);
        model.addAttribute("timezones", UserInputUtil.getTimeZoneIds());
        model.addAttribute("member", member);
    }
 
    @RequestMapping(value = "/member/{id}/newUser", method = RequestMethod.POST)
    public String memberNewUserPost(Model model, @ModelAttribute UserCommand userCommand, BindingResult bindingResult, @PathVariable String id) throws NoSuchResourceException
    {
        StandaloneMember member = populateMember(model, id);

        String username = UserInputUtil.trimToNull(userCommand.getEmail());
        StandaloneUser existingUser = validateUserForm(bindingResult, username, "");

        if (bindingResult.hasErrors())
        {
            memberNewUserModelAttributes(model, member);
            return "/WEB-INF/jsp/user/editUser.jsp";
        }

        StandaloneUser user = createOrUpdateUser(existingUser, member, username, userCommand);
        
        sendResetPasswordEmail(user, ADDITIONAL_USER_SUBJECT, ADDITIONAL_USER_EMAIL);
        
        return "redirect:/ui/registered/" + user.getId();
    }
 
    @RequestMapping(value = "/member", method = RequestMethod.GET)
    public String showMembers(HttpServletRequest request, Model model) 
    {
        SortPageManager spm = new SortPageManager(request, MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null));
        
        if (spm.getSortKey() == null)
            model.addAttribute("members", m_memberDao.getAllMembers());
        else
            model.addAttribute("members", m_memberDao.getAllMembers(spm.getSortKey(), spm.getSortDirection() == SortOrder.Direction.ASC ? +1 : -1));
        model.addAttribute("spm", spm);
       
        return "/WEB-INF/jsp/user/showMembers.jsp";
    }
    
    @RequestMapping(value = "/member/{id}/account", method = RequestMethod.GET)
    public String showAccount(Model model, Principal principal, @PathVariable String id) throws GlobalBrokerException, NoSuchResourceException
    {
        StandaloneMember member = populateMember(model, id);
        String gln = member.getGln();
       
        Collection<? extends SalesOrder> uninvoiced = getGbService().getUninvoicedOrders(gln);
        model.addAttribute("uninvoiced", uninvoiced);
        Collection<? extends BillingTransaction> xactions = getGbService().getAllBillingTransactions(gln);
        model.addAttribute("xactions", xactions);
        model.addAttribute("forMember", member);
        
        return "/WEB-INF/jsp/account/showAccount.jsp";
    }
    
    @RequestMapping(value = "/member/{id}/product", method = RequestMethod.GET)
    public String showProducts(Model model, Principal principal, @PathVariable String id) throws GlobalBrokerException, NoSuchResourceException 
    {
        StandaloneMember member = populateMember(model, id);
        String gln = member.getGln();
        
        Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gln, true);
        
        Collection<? extends Product> products = getGbService().getProducts(gln);
        model.addAttribute("subs", subs);
        model.addAttribute("products", products);
        model.addAttribute("productLine1AttrName", "brandName");
        model.addAttribute("productLine2AttrName", "productName");
        
        model.addAttribute("forMember", member);

        return "/WEB-INF/jsp/product/showProducts.jsp";
    }


    
    public static class MemberAndUserCommand
    {
        private MemberCommand m_member = new MemberCommand();
        private UserCommand m_user = new UserCommand();
        public MemberCommand getMember()
        {
            return m_member;
        }
        public void setMember(MemberCommand member)
        {
            m_member = member;
        }
        public UserCommand getUser()
        {
            return m_user;
        }
        public void setUser(UserCommand user)
        {
            m_user = user;
        }
        
    }

    public static class MemberCommand
    {
        private String m_gln;
        private String m_companyName;
        private String m_address1;
        private String m_address2;
        private String m_city;
        private String m_state;
        private String m_postalCode;
        private String m_memberId;
        private String m_gcps;
         

        

        public String getGln()
        {
            return m_gln;
        }
        public void setGln(String gln)
        {
            m_gln = gln;
        }
        public String getCompanyName()
        {
            return m_companyName;
        }
        public void setCompanyName(String companyName)
        {
            m_companyName = companyName;
        }
        
        public String getAddress1()
        {
            return m_address1;
        }
        public void setAddress1(String address1)
        {
            m_address1 = address1;
        }
        public String getAddress2()
        {
            return m_address2;
        }
        public void setAddress2(String address2)
        {
            m_address2 = address2;
        }
        public String getCity()
        {
            return m_city;
        }
        public void setCity(String city)
        {
            m_city = city;
        }
        public String getState()
        {
            return m_state;
        }
        public void setState(String state)
        {
            m_state = state;
        }
        public String getPostalCode()
        {
            return m_postalCode;
        }
        public void setPostalCode(String postalCode)
        {
            m_postalCode = postalCode;
        }
        public String getMemberId()
        {
            return m_memberId;
        }
        public void setMemberId(String memberId)
        {
            m_memberId = memberId;
        }
        public String getGcps()
        {
            return m_gcps;
        }
        public void setGcps(String gcps)
        {
            m_gcps = gcps;
        }
        
        
         public void setAdminEditableFields(StandaloneMember member)
        {
            setCompanyName(member.getCompanyName());
            setAddress1(member.getAddress1());
            setAddress2(member.getAddress2());
            setCity(member.getCity());
            setState(member.getState());
            setPostalCode(member.getPostalCode());
            setMemberId(member.getMemberId());
            setGln(member.getGln());
            String[] gcps = member.getGcps();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < gcps.length; i++)
            {
                if (i > 0)
                    buf.append(' ');
                buf.append(gcps[i]);
            }
            setGcps(buf.toString());
        }
        public void updateAdminEditableFields(StandaloneMember member)
        {
            String companyName = UserInputUtil.trimToNull(getCompanyName());
            String address1 = UserInputUtil.trimToNull(getAddress1());
            String address2 = UserInputUtil.trimToNull(getAddress2());
            String city = UserInputUtil.trimToNull(getCity());
            String state = UserInputUtil.trimToNull(getState());
            String postalCode = UserInputUtil.trimToNull(getPostalCode());
            String memberId = UserInputUtil.trimToNull(getMemberId());
            String[] gcps = parseGcps();
            
            member.setCompanyName(companyName);
            member.setAddress1(address1);
            member.setAddress2(address2);
            member.setCity(city);
            member.setState(state);
            member.setPostalCode(postalCode);
            member.setMemberId(memberId);
            member.setGcps(gcps);
        }
        
        public void validateAdminEditableFields(BindingResult bindingResult, String bindingPrefix)
        {
            String gln = UserInputUtil.trimToNull(getGln());
            if (gln == null || !UserInputUtil.isValidGln(gln))
                bindingResult.rejectValue(bindingPrefix + "gln", "GlnRequired", "A valid GLN is required");

            String companyName = UserInputUtil.trimToNull(getCompanyName());
            if (companyName == null)
                bindingResult.rejectValue(bindingPrefix + "companyName", "CompanyNameRequired", "A company name is required");

            if (UserInputUtil.trimToNull(getGcps()) == null)
            {
                bindingResult.rejectValue(bindingPrefix + "gcps", "InvalidGcps", "One or more GCPs must be specified");
            }
            else
            {
                String[] gcps = parseGcps();
                for (String gcp : gcps)
                {
                    if (!UserInputUtil.isValidGcp(gcp))
                        bindingResult.rejectValue(bindingPrefix + "gcps", "InvalidGcps", gcp + " is not a valid GCP");
                }
            }
        }
        
        public GBAccount toGbAccount(GlobalBrokerService gbService, AttributeSet attributes)
        {
            return gbService.newGBAccount(UserInputUtil.trimToNull(getGln()), 
                                          UserInputUtil.trimToNull(getCompanyName()),
                                          parseGcps(), 
                                          attributes);
        }
        
        private String[] parseGcps()
        {
            String trimmed = UserInputUtil.trimToNull(getGcps());
            if (trimmed == null)
                return null;
            else
                return trimmed.split("[ ,;:\t\n\r]+");
        }
    }

    

}
