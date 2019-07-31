package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.AccountDTO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface AccountService {

    //Author
    String checkLogin(AccountDTO accountDTO, HttpSession session, HttpServletRequest request);

    String sendForgotPassword(String email);
    
    //Account
    List<AccountDTO> getAllAccount();
    
    String addAccount(AccountDTO accountDTO);
    
    String updateAccount(int oldPackageId, int oldGroupID, AccountDTO accountDTO, String oldEmail);
    
    String deleteAccount(int groupId, int accountId, int packageId);
    
    String resendActiveEmail(String email);

    String resendActiveChangeEmail(String email);
    
    String updateLanguage(HttpSession session, int accountID, String language);
    
    AccountDTO getProfile(int accountID);

    String updateProfile(AccountDTO accountDTO, HttpSession session, String oldEmail);
    
    String updatePassword(AccountDTO accountDTO);

}
