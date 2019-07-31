package com.rmith.dao;

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
public interface AccountDAO {

    //Author
    String checkLogin(AccountDTO accountDTO, HttpSession session, HttpServletRequest request);

    boolean isEmailExistInDatabase(String email);

    String createToken(String email);

    //Account
    List<AccountDTO> getAllAccount();

    int addAccount(AccountDTO accountDTO);

    String updateAccount(AccountDTO accountDTO);

    String deleteAccount(int accountId);

    boolean isEmailExistWithoutThisAccount(String email, int accountID);

    String getTokenChangeEmail(String email);

    String addNewEmail(int accountId, String email, String token);

    String deleteNewEmailByToken(String token);

    String updateLanguage(HttpSession session, int accountID, String language);

    String updateAgencyForAccount(int accountId, int isAgency);

    AccountDTO getProfile(int accountID);

    String updateProfile(AccountDTO accountDTO, HttpSession session);

    boolean isCorrectPassword(AccountDTO accountDTO);

    String updatePassword(AccountDTO accountDTO);
}
