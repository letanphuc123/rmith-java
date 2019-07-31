package com.rmith.controller;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.dto.AccountDTO;
import com.rmith.service.AccountService;
import com.rmith.utils.ValidateUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Controller
public class AuthorController {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Autowired
    @Lazy
    private AccountService accountService;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LOGIN">
    @GetMapping("/")
    public String home(
            HttpSession session) {
        if (ValidateUtils.isSessionNull(session)) {
            return "redirect:/login";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login(
            HttpSession session,
            ModelMap modelMap,
            HttpServletRequest request, HttpServletResponse response,
            @CookieValue(value = "_.language_out_of_login", defaultValue = "JA", required = false) String languageCookie) {
        if (ValidateUtils.isSessionNull(session)) {
            // Add log for user access this page has not session
            modelMap.addAttribute("languageCookie", languageCookie);
            return "author/login";
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/check-login")
    public String checkLogin(
            AccountDTO accountDTO,
            HttpSession session,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @CookieValue(value = "_.language_out_of_login", defaultValue = "", required = false) String languageCookie,
            HttpServletResponse response) {

        if (accountDTO.getErrorMap().size() > 0) {
            redirectAttrs.addFlashAttribute("wrongAccount", true);
            return "redirect:/login";
        }
        String result = accountService.checkLogin(accountDTO, session, request);
        switch (result) {
            case Constant.SUCCESS: {
                if (languageCookie.length() > 0) {
                    HttpSession newSession = request.getSession();
                    newSession.setAttribute("language", languageCookie);
                    //update profile same cookie value
                    //accountService.updateLanguage(newSession, (int) newSession.getAttribute("accountID"), languageCookie);
                }
                return "redirect:/dashboard";
            }
            case "NOT_YET_TO_USE": {
                redirectAttrs.addFlashAttribute("notYet", true);
                return "redirect:/login";
            }
            case "NOT_ACTIVE": {
                redirectAttrs.addFlashAttribute("blocked", true);
                return "redirect:/login";
            }
            case "NOT_VERIFIER": {
                session.setAttribute("emailActivate", accountDTO.getEmail());
                redirectAttrs.addFlashAttribute("notActive", true);
                return "redirect:/login";
            }
            case "NOT_PERMISSION": {
                redirectAttrs.addFlashAttribute("notPermission", true);
                return "redirect:/login";
            }
            default:
                redirectAttrs.addFlashAttribute("wrongAccount", true);
                return "redirect:/login";
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LOG OUT">
    @PostMapping("/sign-out")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DASHBOARD">
    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session,
            ModelMap modelMap) {
        modelMap.addAttribute("menuModule", "dashboard");
        return "/author/dashboard";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FORGOT PASSWORD">
    @GetMapping("/forgot-password")
    public String forgotPassword(
            HttpSession session,
            HttpServletRequest request,
            ModelMap modelMap,
            @CookieValue(value = "_.language_out_of_login", defaultValue = "JA", required = false) String languageCookie) {
        if (ValidateUtils.isSessionNull(session)) {
            // Add log for user access this page has not session
            modelMap.addAttribute("languageCookie", languageCookie);
            return "author/forgot-password";
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/send-forgot-password")
    public String sendForgotPassword(
            HttpSession session,
            @RequestParam String emailForgot,
            RedirectAttributes redirectAttributes) {
        String result = accountService.sendForgotPassword(emailForgot);
        switch (result) {
            case "wrong_email": {
                redirectAttributes.addFlashAttribute("errorWrongEmail", true);
                return "redirect:/forgot-password";
            }
            case "email_not_exist": {
                redirectAttributes.addFlashAttribute("errorEmailNotExist", true);
                return "redirect:/forgot-password";
            }
            case Constant.SUCCESS: {
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:/forgot-password";
            }
        }
        redirectAttributes.addFlashAttribute("error", true);
        return "redirect:/forgot-password";
    }
    //</editor-fold>

}
