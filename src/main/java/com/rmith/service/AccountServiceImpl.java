package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.dao.AccountDAO;
import com.rmith.dao.ModuleDAO;
import com.rmith.dto.AccountDTO;
import com.rmith.dto.ModuleDTO;
import com.rmith.utils.GeneratedIDUtils;
import com.rmith.utils.ValidateUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Service
public class AccountServiceImpl implements AccountService {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Autowired
    @Lazy
    private AccountDAO accountDAO;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    private GeneratedIDUtils generatedIDUtils;

    @Autowired
    @Lazy
    private ModuleDAO moduleDAO;

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> repository;
    //</editor-fold>

    //Author
    //<editor-fold defaultstate="collapsed" desc="CHECK LOGIN">
    @Override
    public String checkLogin(AccountDTO accountDTO, HttpSession session, HttpServletRequest request) {
        String result = accountDAO.checkLogin(accountDTO, session, request);

        if (result.equals(Constant.SUCCESS)) {
            HttpSession newSession = request.getSession();
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

            if ((int) newSession.getAttribute("isAdmin") == 1) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_admin"));
            } else if ((int) newSession.getAttribute("isGroupAdmin") == 1) {
                int groupId = (int) newSession.getAttribute("groupID");
                List<ModuleDTO> listModuleToGrandPermission = moduleDAO.getModuleByGroupId(groupId);
                listModuleToGrandPermission.forEach((moduleDTO) -> {
                    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + moduleDTO.getModuleCode()));
                });
            } else {
                newSession.invalidate();
                return "NOT_PERMISSION";
            }
            //role for all user has logged
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_logged"));
            //Make a user with username and password and added spring security context
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    accountDTO.getEmail(), accountDTO.getSecretKey(), grantedAuthorities);
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(auth);
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SEND FORGOT PASSWORD">
    @Override
    public String sendForgotPassword(String email) {
        //Validate email format
        if (!ValidateUtils.validateEmail(email).equals(Constant.SUCCESS)) {
            return "wrong_email";
        }

        //Check email exits in system
        if (!accountDAO.isEmailExistInDatabase(email)) {
            return "email_not_exist";
        }

        //Create token forgot password
        String resultCreateToken = accountDAO.createToken(email);
        if (resultCreateToken.equals(Constant.FAIL)) {
            return Constant.FAIL;
        }

        
        /* MISSING LOG */
        return Constant.FAIL;
    }
    //</editor-fold>

    //Account
    //<editor-fold defaultstate="collapsed" desc="GET ALL ACCOUNT">															
    @Override
    public List<AccountDTO> getAllAccount() {
        return accountDAO.getAllAccount();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD ACCOUNT">
    @Override
    public String addAccount(AccountDTO accountDTO) {
        if (accountDAO.isEmailExistInDatabase(accountDTO.getEmail())) {
            return Constant.EMAIL_EXIST;
        }

        int accountId = accountDAO.addAccount(accountDTO);
        if (accountId == 0) {
            return Constant.FAIL;
        }

        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE ACCOUNT">
    @Override
    public String updateAccount(int oldPackageId, int oldGroupID, AccountDTO accountDTO, String oldEmail) {
        int accountId = accountDTO.getAccountId();

        //Check if it don't have email wait active then don't allow update email
        if (StringUtils.isEmpty(accountDTO.getEmailWaitActive())) {
            String newEmail = accountDTO.getEmail();
            if (accountDAO.isEmailExistWithoutThisAccount(newEmail, accountId)) {
                return Constant.EMAIL_EXIST;
            } else {
                //Handle change email
                changeNewMail(accountId, oldEmail, newEmail);
            }
        }

        String result = accountDAO.updateAccount(accountDTO);
        // return soon when can not update account
        if (result.equals(Constant.FAIL)) {
            return result;
        }

        // return soon when do not change group or start/end date
        if (accountDTO.getGroupId() == oldGroupID && accountDTO.getIsChangeDateEnd() != 1) {
            return result;
        }
        
        removeSessionAccount(oldGroupID, accountId);
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE ACCOUNT">
    @Override
    public String deleteAccount(int groupId, int accountId, int packageId) {
        String result = accountDAO.deleteAccount(accountId);
        int[] packageIds = new int[1];
        packageIds[0] = packageId;
        if (result.equals(Constant.SUCCESS)) { // Delete package contain this account
            removeSessionAccount(groupId, accountId);
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RESEND ACTIVE EMAIL">
    @Override
    public String resendActiveEmail(String email) {
        String token = "";
        
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RESEND ACTIVE CHANGE EMAIL">
    @Override
    public String resendActiveChangeEmail(String email) {
        String token = accountDAO.getTokenChangeEmail(email);
        //Send email to client
        
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHANGE NEW MAIL">
    private void changeNewMail(int accountId, String oldEmail, String newEmail) {
        if (StringUtils.isEmpty(oldEmail) || StringUtils.isEmpty(newEmail) || oldEmail.equals(newEmail)) {
            return;
        }

        String token = generatedIDUtils.generatedUUID();
        String resultChangeMail = accountDAO.addNewEmail(accountId, newEmail, token);

        if (Constant.FAIL.equals(resultChangeMail)) {
            return;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REMOVE ACCOUNT SESSION">
    private void removeSessionAccount(int groupId, int accountId) {
        Collection<Session> sessionList = (Collection<Session>) repository
                .findByPrincipalName(String.valueOf(groupId)).values();
        for (Session session : sessionList) {
            if ((int) session.getAttribute("accountID") == accountId) {
                repository.deleteById(session.getId());
                break;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE LANGUAGE">
    @Override
    public String updateLanguage(HttpSession session, int accountID, String language) {
        return accountDAO.updateLanguage(session, accountID, language);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET PROFILE">
    @Override
    public AccountDTO getProfile(int accountID) {
        return accountDAO.getProfile(accountID);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE PROFILE">
    @Override
    public String updateProfile(AccountDTO accountDTO, HttpSession session, String oldEmail) {
        //Check if it don't have email wait active then don't allow update email
        if (StringUtils.isEmpty(accountDTO.getEmailWaitActive())) {
            String newEmail = accountDTO.getEmail();
            if (accountDAO.isEmailExistWithoutThisAccount(newEmail, accountDTO.getAccountId())) {
                return Constant.EMAIL_EXIST;
            }
            //Handle change email
            changeNewMail(accountDTO.getAccountId(), oldEmail, newEmail);
        }

        return accountDAO.updateProfile(accountDTO, session);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE PASSWORD">
    @Override
    public String updatePassword(AccountDTO accountDTO) {
        if (!accountDAO.isCorrectPassword(accountDTO)) {
            return "WRONGPASSWORD";
        }

        return accountDAO.updatePassword(accountDTO);

    }
    //</editor-fold>

}
