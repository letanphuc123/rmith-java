package com.rmith.controller;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.config.AsyncThreadConfig;
import com.rmith.constant.Constant;
import com.rmith.dto.AccountDTO;
import com.rmith.dto.GroupUserDTO;
import com.rmith.service.AccountService;
import com.rmith.service.GroupUserService;
import com.rmith.service.ModuleService;
import com.rmith.service.PermissionService;
import com.rmith.utils.CookieUtils;
import com.rmith.utils.ValidateUtils;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Controller
public class AccountController {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Autowired
    @Lazy
    private ModuleService moduleService;

    @Autowired
    @Lazy
    private AccountService accountService;

    @Autowired
    @Lazy
    private GroupUserService groupUserService;

    @Autowired
    @Lazy
    private PermissionService permissionService;

    private final Logger logger = LogManager.getLogger("error_future");
    //</editor-fold>

    /* 1: Accounts */
    //<editor-fold defaultstate="collapsed" desc="ACCOUNTS">
    @GetMapping("/accounts")
    public DeferredResult<String> account(HttpSession session, ModelMap modelMap) {
        modelMap.addAttribute("menuModule", "accounts");

        DeferredResult<String> output = new DeferredResult(30000L);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    modelMap.addAttribute("accountDTOList", accountService.getAllAccount());
                }, AsyncThreadConfig.executorPool),
                CompletableFuture.runAsync(() -> {
                    modelMap.addAttribute("listGroupUser", groupUserService.getGroupUser());
                }, AsyncThreadConfig.executorPool)
        ).exceptionally(t -> {
            logger.error(ExceptionUtils.getStackTrace(t));
            output.setResult("/account/accounts");
            return null;
        }).thenApply(t -> {
            output.setResult("/account/accounts");
            return null;
        });
        return output;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NEW ACCOUNT">
    @GetMapping("/new-account")
    public DeferredResult<String> newAccount(
            HttpSession session,
            ModelMap modelMap) {
        modelMap.addAttribute("menuModule", "accounts");
        DeferredResult<String> output = new DeferredResult(30000L);
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    modelMap.addAttribute("listGroupUser", groupUserService.getGroupUser());
                }, AsyncThreadConfig.executorPool)
        ).exceptionally(t -> {
            logger.error(ExceptionUtils.getStackTrace(t));
            output.setResult("/account/new-account");
            return null;
        }).thenApply(t -> {
            output.setResult("/account/new-account");
            return null;
        });
        return output;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD ACCOUNT">
    @PostMapping("/add-account")
    public String addAccount(
            AccountDTO accountDTO,
            HttpSession session,
            RedirectAttributes redirectAttrs,
            @RequestParam String confirmSecretKey) {

        /* Skip validate firstName and lastName for admin update */
        accountDTO.getErrorMap().remove("firstName");
        accountDTO.getErrorMap().remove("lastName");
        if (accountDTO.getErrorMap().size() > 0) {
            redirectAttrs.addFlashAttribute("accountDTO", accountDTO);
            return "redirect:/new-account";
        }
        /* If password and confirm password match */
        if (accountDTO.getSecretKey().equals(confirmSecretKey)) {
            int accountId = (int) session.getAttribute("accountID");
            accountDTO.setCreatedBy(accountId);
            accountDTO.setUpdatedBy(accountId);
            String result = accountService.addAccount(accountDTO);
            switch (result) {
                case Constant.EMAIL_EXIST:
                    redirectAttrs.addFlashAttribute("accountDTO", accountDTO);
                    redirectAttrs.addFlashAttribute("emailIsExist", true);
                    return "redirect:/new-account";
                case Constant.SUCCESS:
                    redirectAttrs.addFlashAttribute("addSuccess", true);
                    return "redirect:/accounts";
                default:
                    redirectAttrs.addFlashAttribute("error", true);
                    redirectAttrs.addFlashAttribute("accountDTO", accountDTO);
                    return "redirect:/new-account";
            }
        }
        redirectAttrs.addFlashAttribute("accountDTO", accountDTO);
        redirectAttrs.addFlashAttribute("passwordNotMatch", true);
        return "redirect:/new-account";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EDIT ACCOUNT">
    @PostMapping("/edit-account")
    public String editAccount(
            AccountDTO accountDTO,
            @RequestParam("oldEmail") String oldEmail,
            @RequestParam("oldGroupID") int oldGroupID,
            @RequestParam("oldPackageId") int oldPackageId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        /* Skip validate firstName and lastName for admin update */
        accountDTO.getErrorMap().remove("firstName");
        accountDTO.getErrorMap().remove("lastName");
        if (accountDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("updateAccountDTO", accountDTO);
            return "redirect:/accounts";
        }
        int accountId = (int) session.getAttribute("accountID");
        accountDTO.setUpdatedBy(accountId);

        String result = accountService.updateAccount(oldPackageId, oldGroupID, accountDTO, oldEmail);
        switch (result) {
            case Constant.EMAIL_EXIST:
                redirectAttributes.addFlashAttribute("emailIsExist", true);
                redirectAttributes.addFlashAttribute("updateAccountDTO", accountDTO);
                break;
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("updateSuccess", true);
                if (accountDTO.getAccountId() == accountId) {
                    session.setAttribute("name", (accountDTO.getFirstName() + ' ' + accountDTO.getLastName()));
                }
                break;
            default:
                redirectAttributes.addFlashAttribute("updateError", true);
                redirectAttributes.addFlashAttribute("updateAccountDTO", accountDTO);
                break;
        }
        return "redirect:/accounts";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE ACCOUNT">
    @PostMapping("/delete-account")
    public String deleteAccount(
            HttpSession session,
            RedirectAttributes redirectAttrs,
            @RequestParam int groupId,
            @RequestParam int accountId,
            @RequestParam int packageId) {
        int accountIdSession = (int) session.getAttribute("accountID");
        if (accountIdSession == accountId) {
            redirectAttrs.addFlashAttribute("currentAccount", true);
            return "redirect:/accounts";
        }
        String result = accountService.deleteAccount(groupId, accountId, packageId);
        switch (result) {
            case Constant.SUCCESS:
                redirectAttrs.addFlashAttribute("deleteSuccess", true);
                break;
            default:
                redirectAttrs.addFlashAttribute("deleteError", true);
                break;
        }
        return "redirect:/accounts";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RESEND ACTIVE EMAIL">
    @PostMapping("/resend-active-email")
    public String reSendActiveEmail(
            HttpSession session,
            RedirectAttributes redirectAttrs,
            @RequestParam(name = "emailVerify", required = false, defaultValue = "") String emailVerify,
            @RequestParam(name = "redirect", required = false, defaultValue = "") String redirect) {
        if (session.getAttribute("emailActivate") == null && redirect.equals("login")) {
            return "redirect:/login";
        }
        switch (redirect) {
            case "login":
                String email = session.getAttribute("emailActivate").toString();
                if (accountService.resendActiveEmail(email).equals(Constant.SUCCESS)) {
                    redirectAttrs.addFlashAttribute("resendSuccess", true);
                    break;
                }
                redirectAttrs.addFlashAttribute("resendError", true);
                break;
            case "accounts":
                if (accountService.resendActiveEmail(emailVerify).equals(Constant.SUCCESS)) {
                    redirectAttrs.addFlashAttribute("resendSuccess", true);
                    break;
                }
                redirectAttrs.addFlashAttribute("resendError", true);
                break;
            case "ChangeNewEmail":
                redirect = "accounts";
                if (accountService.resendActiveChangeEmail(emailVerify).equals(Constant.SUCCESS)) {
                    redirectAttrs.addFlashAttribute("resendChangeSuccess", true);
                    break;
                }
                redirectAttrs.addFlashAttribute("resendError", true);
                break;
            default:
                redirectAttrs.addFlashAttribute("resendError", true);
                break;
        }
        return "redirect:/" + redirect;
    }
    //</editor-fold>

    /* 3: Group user */
    //<editor-fold defaultstate="collapsed" desc="GROUP USER">
    @GetMapping("/group-user")
    public DeferredResult<String> getGroupUser(HttpSession session, ModelMap modelMap) {
        modelMap.addAttribute("menuModule", "group-user");

        DeferredResult<String> output = new DeferredResult(30000L);
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    modelMap.addAttribute("permissionDTOList", moduleService.getAllModule());
                }, AsyncThreadConfig.executorPool),
                CompletableFuture.runAsync(() -> {
                    modelMap.addAttribute("groupUserDTOList", groupUserService.getGroupUser());
                }, AsyncThreadConfig.executorPool),
                CompletableFuture.runAsync(() -> {
                    modelMap.addAttribute("isExistFreeGroup", groupUserService.isExistFreeGroup());
                }, AsyncThreadConfig.executorPool)
        ).exceptionally(t -> {
            output.setResult("/account/group-user");
            logger.error(ExceptionUtils.getStackTrace(t));
            return null;
        }).thenApply(t -> {
            output.setResult("/account/group-user");
            return null;
        });
        return output;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD GROUP USER">
    @PostMapping("/add-group-user")
    public String addGroup(
            GroupUserDTO groupUserDTO,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (groupUserDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("addError", true);
            return "redirect:/group-user";
        }
        int accountID = (int) session.getAttribute("accountID");
        groupUserDTO.setCreateBy(accountID);
        groupUserDTO.setUpdateBy(accountID);
        if (groupUserService.addGroupUser(groupUserDTO)) {
            redirectAttributes.addFlashAttribute("addSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("addError", true);
        }
        return "redirect:/group-user";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE GROUP USER">
    @PostMapping("/update-group-user")
    public String updateGroupName(
            GroupUserDTO groupUserDTO,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        groupUserDTO.setUpdateBy((int) session.getAttribute("accountID"));
        if (groupUserDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("updateGroupUserDTO", groupUserDTO);
            return "redirect:/group-user";
        }
        if (groupUserService.updateGroupUser(groupUserDTO)) {
            redirectAttributes.addFlashAttribute("updateSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("updateGroupUserDTO", groupUserDTO);
            redirectAttributes.addFlashAttribute("updateError", true);
        }
        return "redirect:/group-user";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE GROUP USER">
    @PostMapping("/delete-group-user")
    public String deleteGroupById(
            GroupUserDTO groupUserDTO,
            RedirectAttributes redirectAttributes) {
        String result = groupUserService.deleteGroupUser(groupUserDTO.getGroupId());
        switch (result) {
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("deteteSuccess", true);
                break;
            case Constant.HAS_REF:
                redirectAttributes.addFlashAttribute("groupHasAccount", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("deleteError", true);
                break;
        }
        return "redirect:/group-user";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE PERMISSION GROUP USER">
    @PostMapping("/update-permission")
    public String updatePermission(
            GroupUserDTO groupUserDTO,
            RedirectAttributes redirectAttributes) {
        if (!groupUserDTO.getErrorMap().isEmpty()) {
            redirectAttributes.addFlashAttribute("permissionError", true);
            return "redirect:/group-user";
        }
        if (permissionService.editPermission(groupUserDTO)) {
            redirectAttributes.addFlashAttribute("permissionSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("permissionError", true);
        }
        return "redirect:/group-user";
    }
    //</editor-fold>

    /* 3: Profile */
    //<editor-fold defaultstate="collapsed" desc="PROFILE">
    @GetMapping("/profile")
    public DeferredResult<String> profile(HttpSession session,
            ModelMap map) {
        DeferredResult<String> output = new DeferredResult(30000L);
        if (ValidateUtils.isSessionNull(session)) {
            output.setResult("/author/login");
            return output;
        }

        map.addAttribute("menuModule", "profile");
        int accountId = (int) session.getAttribute("accountID");
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    AccountDTO accountDTO = accountService.getProfile(accountId);
                    map.addAttribute("accountDTO", accountDTO);
                    if (accountDTO.isSetPassword()) {
                        map.addAttribute("passwordNotExist", true);
                    }
                }, AsyncThreadConfig.executorPool)
        ).exceptionally(t -> {
            logger.error(ExceptionUtils.getStackTrace(t));
            output.setResult("/account/profile");
            return null;
        }).thenApply(t -> {
            output.setResult("/account/profile");
            return null;
        });

        return output;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE PROFILE">
    @PostMapping("/update-profile")
    public String updateProfile(AccountDTO accountDTO,
            @RequestParam("oldEmail") String oldEmail,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) {
        if (accountDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("accountDTOupdateProfile", accountDTO);
            return "redirect:/profile";
        }

        accountDTO.setAccountId((int) session.getAttribute("accountID"));
        String result = accountService.updateProfile(accountDTO, session, oldEmail);
        switch (result) {
            case Constant.EMAIL_EXIST:
                redirectAttributes.addFlashAttribute("accountDTOupdateProfile", accountDTO);
                redirectAttributes.addFlashAttribute("emailIsExist", true);
                break;
            case Constant.SUCCESS:
                response.addCookie(CookieUtils.createCookie("_.language_out_of_login", session.getAttribute("language").toString(), 10 * 365 * 24 * 60 * 60));
                redirectAttributes.addFlashAttribute("updateProfileSuccess", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("updateProfileError", true);
                break;
        }
        return "redirect:/profile";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE PASSWORD">
    @PostMapping("/update-password")
    public String updatePassword(AccountDTO accountDTO,
            HttpSession session,
            @RequestParam String confirmSecretKey,
            RedirectAttributes redirectAttributes) {
        /* When DTO have error */
        if (accountDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("accountDTOupdatePassword",
                    accountDTO);
            return "redirect:/profile";
        }
        /* check new password match confirm password */
        if (accountDTO.getNewSecretKey().equals(confirmSecretKey)) {
            /* set accountID to check old password */
            accountDTO.setAccountId((int) session.getAttribute("accountID"));
            /* Update new password */
            String result = accountService.updatePassword(accountDTO);
            switch (result) {
                case "WRONGPASSWORD":
                    redirectAttributes.addFlashAttribute("wrongPassword", true);
                    break;
                case Constant.SUCCESS:
                    redirectAttributes.addFlashAttribute("updatePasswordSuccess", true);
                    break;
                default:
                    redirectAttributes.addFlashAttribute("updatePasswordError", true);
                    break;
            }
            return "redirect:/profile";
        }
        redirectAttributes.addFlashAttribute("passwordNotMatch", true);
        return "redirect:/profile";
    }
    //</editor-fold>
}
