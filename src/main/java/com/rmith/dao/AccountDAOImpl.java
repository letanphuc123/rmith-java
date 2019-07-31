package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.dto.AccountDTO;
import com.rmith.utils.GeneratedIDUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Component;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Component
@Lazy
public class AccountDAOImpl implements AccountDAO {

    @Autowired
    private BasicDataSource basicDataSource;

    @Autowired
    @Lazy
    private GeneratedIDUtils generatedIDUtils;

    private final Logger infoMysqlLogger = LogManager.getLogger("info_mysql");
    private final Logger errorMysqlLogger = LogManager.getLogger("error_mysql");

    //Author
    //<editor-fold defaultstate="collapsed" desc="CHECK LOGIN">
    @Override
    public String checkLogin(AccountDTO accountDTO, HttpSession session, HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT \n"
                    + "    SECRET_KEY,\n"
                    + "    ACCOUNT_ID,\n"
                    + "    A_ACCOUNT.GROUP_ID,\n"
                    + "    PACKAGE_ID,\n"
                    + "    VERIFIER_EMAIL,\n"
                    + "    ACCOUNT_STATUS,\n"
                    + "    IS_ADMIN,\n"
                    + "    IF(ACCOUNT_LANGUAGE IS NULL OR ACCOUNT_LANGUAGE = '', 'JA', ACCOUNT_LANGUAGE) AS ACCOUNT_LANGUAGE,\n"
                    + "    IF(FIRST_NAME = '' \n"
                    + "			OR FIRST_NAME IS NULL \n"
                    + "            OR LAST_NAME = '' \n"
                    + "            OR LAST_NAME IS NULL, '', CONCAT(FIRST_NAME, ' ', LAST_NAME)) AS NAME,\n"
                    + "    IF(AVATAR IS NULL, '', AVATAR) AS AVATAR,\n"
                    + "    IF(LOGO IS NULL, '', LOGO) AS LOGO,\n"
                    + "    IF(DATE_START = ''\n"
                    + "            OR DATE_START IS NULL\n"
                    + "            OR DATE_END = ''\n"
                    + "            OR DATE_END IS NULL, 1,\n"
                    + "        CASE\n"
                    + "            WHEN NOW() < DATE_START THEN 0\n"
                    + "            ELSE 1\n"
                    + "        END) AS IS_VALID_DATE,\n"
                    + "        A_GROUP_USER.IS_ADMIN_GROUP\n"
                    + "FROM A_ACCOUNT LEFT JOIN A_GROUP_USER ON A_ACCOUNT.GROUP_ID = A_GROUP_USER.GROUP_ID\n"
                    + "WHERE EMAIL = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, accountDTO.getEmail());
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("VERIFIER_EMAIL") == 0) {
                    return "NOT_VERIFIER";
                }
                if (rs.getInt("ACCOUNT_STATUS") == 0) {
                    return "NOT_ACTIVE";
                }
                if (BCrypt.checkpw(accountDTO.getSecretKey(), rs.getString("SECRET_KEY"))) {
                    session.invalidate();
                    session = request.getSession();
                    session.setAttribute("accountID", rs.getInt("ACCOUNT_ID"));
                    session.setAttribute("packageID", rs.getInt("PACKAGE_ID"));
                    session.setAttribute("groupID", rs.getInt("GROUP_ID"));
                    session.setAttribute("email", accountDTO.getEmail());
                    session.setAttribute("isAdmin", rs.getInt("IS_ADMIN"));
                    session.setAttribute("isGroupAdmin", rs.getInt("IS_ADMIN_GROUP"));
                    session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, String.valueOf(rs.getInt("GROUP_ID")));
                    session.setAttribute("language", rs.getString("ACCOUNT_LANGUAGE"));
                    if (rs.getString("NAME").length() > 0) {
                        session.setAttribute("name", rs.getString("NAME"));
                    }
                    if (rs.getString("LOGO").length() > 0) {
                        session.setAttribute("logo", rs.getString("LOGO"));
                    }
                    if (rs.getString("AVATAR").length() > 0) {
                        session.setAttribute("avatar", rs.getString("AVATAR"));
                    }
                    return Constant.SUCCESS;
                }
            }
        } catch (SQLException | AuthenticationException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHECK EMAIL EXIST">
    @Override
    public boolean isEmailExistInDatabase(String email) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMAIL \n"
                    + "FROM A_ACCOUNT \n"
                    + "WHERE EMAIL = ? AND VERIFIER_EMAIL = 1\n"
                    + "UNION\n"
                    + "SELECT EMAIL \n"
                    + "FROM A_OR_USER \n"
                    + "WHERE EMAIL = ? AND VERIFIER_EMAIL = 1;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, email);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CREATE TOKEN">
    @Override
    public String createToken(String email) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO A_FORGOT_PASSWORD (EMAIL, TOKEN, CREATED_DATE) \n"
                    + "VALUES (?,?, NOW());";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            String token = generatedIDUtils.generatedUUID();
            ps.setString(1, email);
            ps.setString(2, token);
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? token : Constant.FAIL;
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //Account
    //<editor-fold defaultstate="collapsed" desc="GET ALL ACCOUNT">
    @Override
    public List<AccountDTO> getAllAccount() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AccountDTO> accountDTOList = new ArrayList<>();
        try {
            String sql = "SELECT A.ACCOUNT_ID,A.FIRST_NAME,A.LAST_NAME,A.COMPANY,A.MOBILE,A.EMAIL,"
                    + "CE.EMAIL AS 'EMAIL_WAIT_ACTIVE',A.MEMO,A.VERIFIER_EMAIL,A.ACCOUNT_STATUS,A.IS_ADMIN,A.DATE_START,"
                    + "A.DATE_END,A.GROUP_ID,A.PACKAGE_ID,A.ACCOUNT_LANGUAGE,A.CREATED_BY,"
                    + "A.CREATED_DATE,A.UPDATED_DATE,GU.GROUP_NAME, P.PACKAGE_NAME \n"
                    + "FROM A_ACCOUNT AS A\n"
                    + "LEFT JOIN A_GROUP_USER AS GU\n"
                    + "ON A.GROUP_ID = GU.GROUP_ID\n"
                    + "LEFT JOIN A_PACKAGE AS P\n"
                    + "ON A.PACKAGE_ID = P.PACKAGE_ID\n"
                    + "LEFT JOIN A_CHANGE_EMAIL AS CE\n"
                    + "ON A.ACCOUNT_ID = CE.ACCOUNT_ID AND CE.VERIFIER_EMAIL = 0;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setAccountId(rs.getInt("ACCOUNT_ID"));
                accountDTO.setFirstName(rs.getString("FIRST_NAME"));
                accountDTO.setLastName(rs.getString("LAST_NAME"));
                accountDTO.setCompanyName(rs.getString("COMPANY"));
                accountDTO.setMobile(rs.getString("MOBILE"));
                accountDTO.setEmail(rs.getString("EMAIL"));
                accountDTO.setEmailWaitActive(rs.getString("EMAIL_WAIT_ACTIVE"));
                accountDTO.setMemo(rs.getString("MEMO"));
                accountDTO.setVerifierEmail(rs.getInt("VERIFIER_EMAIL"));
                accountDTO.setAccountStatus(rs.getInt("ACCOUNT_STATUS"));
                accountDTO.setIsAdmin(rs.getInt("IS_ADMIN"));
                accountDTO.setDateStart(rs.getString("DATE_START"));
                accountDTO.setDateEnd(rs.getString("DATE_END"));
                accountDTO.setGroupId(rs.getInt("GROUP_ID"));
                accountDTO.setPackageId(rs.getInt("PACKAGE_ID"));
                accountDTO.setGroupName(rs.getString("GROUP_NAME"));
                accountDTO.setPackageName(rs.getString("PACKAGE_NAME"));
                accountDTO.setAccountLanguage(rs.getString("ACCOUNT_LANGUAGE"));
                accountDTO.setCreatedBy(rs.getInt("CREATED_BY"));
                accountDTO.setCreatedDate(rs.getString("CREATED_DATE"));
                accountDTO.setUpdatedDate(rs.getString("UPDATED_DATE"));
                accountDTOList.add(accountDTO);
            }
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
            }
        }
        return accountDTOList;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD ACCOUNT">
    @Override
    public int addAccount(AccountDTO accountDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO A_ACCOUNT (ACCOUNT_ID, FIRST_NAME, LAST_NAME, COMPANY, EMAIL, MOBILE, ACCOUNT_LANGUAGE, SECRET_KEY, "
                    + "DATE_START, DATE_END, MEMO, VERIFIER_EMAIL, ACCOUNT_STATUS, IS_ADMIN, GROUP_ID, PACKAGE_ID, CREATED_BY, CREATED_DATE, UPDATED_BY) "
                    + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?,IF(?='',NOW(),?),IF(?='',DATE_ADD(NOW(), INTERVAL 14 DAY),?), ?, ?, ?, ?, ?, ?, NOW(), ? FROM DUAL "
                    + "WHERE NOT EXISTS ( SELECT 1 FROM A_OR_USER WHERE ACCOUNT_ID=?) "
                    + "AND NOT EXISTS ( SELECT 1 FROM A_ACCOUNT WHERE ACCOUNT_ID=?);";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            /* when client register account: createdBy and updatedBy equal accountId */
            int accountId;
            do {
                accountId = generatedIDUtils.generatedID();
                int i = 1;
                ps.setInt(i++, accountId);
                ps.setString(i++, accountDTO.getFirstName());
                ps.setString(i++, accountDTO.getLastName());
                ps.setString(i++, accountDTO.getCompanyName());
                ps.setString(i++, accountDTO.getEmail());
                ps.setString(i++, accountDTO.getMobile());
                ps.setString(i++, accountDTO.getAccountLanguage());
                ps.setString(i++, BCrypt.hashpw(accountDTO.getSecretKey(), BCrypt.gensalt(12)));
                ps.setString(i++, accountDTO.getDateStart());
                ps.setString(i++, accountDTO.getDateStart());
                ps.setString(i++, accountDTO.getDateEnd());
                ps.setString(i++, accountDTO.getDateEnd());
                ps.setString(i++, accountDTO.getMemo());
                ps.setInt(i++, accountDTO.getVerifierEmail());
                ps.setInt(i++, accountDTO.getAccountStatus());
                ps.setInt(i++, accountDTO.getIsAdmin());
                ps.setInt(i++, accountDTO.getGroupId());
                ps.setInt(i++, accountDTO.getPackageId());
                ps.setInt(i++, accountDTO.getCreatedBy() == 0 ? accountId : accountDTO.getCreatedBy());
                ps.setInt(i++, accountDTO.getUpdatedBy() == 0 ? accountId : accountDTO.getUpdatedBy());
                ps.setInt(i++, accountId);
                ps.setInt(i++, accountId);
                infoMysqlLogger.info(ps.toString());
            } while (ps.executeUpdate() != 1);
            return accountId;
        } catch (SQLException | IllegalArgumentException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE ACCOUNT">
    @Override
    public String updateAccount(AccountDTO accountDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE A_ACCOUNT \n"
                    + "SET FIRST_NAME = ?, LAST_NAME = ?, COMPANY = ?, MOBILE = ?, ACCOUNT_LANGUAGE = ?, DATE_START = ?, DATE_END = ?, \n"
                    + "MEMO = ?, ACCOUNT_STATUS = ?, IS_ADMIN = ?, GROUP_ID = ?, PACKAGE_ID = ?, UPDATED_BY = ? \n"
                    + "WHERE ACCOUNT_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, accountDTO.getFirstName());
            ps.setString(i++, accountDTO.getLastName());
            ps.setString(i++, accountDTO.getCompanyName());
            ps.setString(i++, accountDTO.getMobile());
            ps.setString(i++, accountDTO.getAccountLanguage());
            ps.setString(i++, accountDTO.getDateStart());
            ps.setString(i++, accountDTO.getDateEnd());
            ps.setString(i++, accountDTO.getMemo());
            ps.setInt(i++, accountDTO.getAccountStatus());
            ps.setInt(i++, accountDTO.getIsAdmin());
            ps.setInt(i++, accountDTO.getGroupId());
            ps.setInt(i++, accountDTO.getPackageId());
            ps.setInt(i++, accountDTO.getUpdatedBy());
            ps.setInt(i++, accountDTO.getAccountId());
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException | IllegalArgumentException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE ACCOUNT">
    @Override
    public String deleteAccount(int accountId) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM A_ACCOUNT WHERE ACCOUNT_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, accountId);
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException | IllegalArgumentException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IS EMAIL EXIST WITHOUT THIS ACCOUNT">
    @Override
    public boolean isEmailExistWithoutThisAccount(String email, int accountID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMAIL \n"
                    + "FROM A_ACCOUNT \n"
                    + "WHERE EMAIL = ? AND ACCOUNT_ID != ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setInt(2, accountID);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET TOKEN CHANGE EMAIL BY EMAIL">
    @Override
    public String getTokenChangeEmail(String email) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT TOKEN FROM A_CHANGE_EMAIL WHERE EMAIL = ? AND VERIFIER_EMAIL = 0;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, email);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("TOKEN");
            }
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return "";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD NEW EMAIL WHEN USER CHANGE EMAIL">
    @Override
    public String addNewEmail(int accountId, String email, String token) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO LOCAL_SEARCH.A_CHANGE_EMAIL (ACCOUNT_ID, EMAIL, TOKEN, VERIFIER_EMAIL)\n"
                    + "SELECT ? ,? ,? ,0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM A_CHANGE_EMAIL WHERE VERIFIER_EMAIL = 0 AND ACCOUNT_ID= ?)";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, accountId);
            ps.setString(i++, email);
            ps.setString(i++, token);
            ps.setInt(i++, accountId);
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException | IllegalArgumentException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE A ROW IN TABLE A_CHANGE_EMAIL BY TOKEN">
    @Override
    public String deleteNewEmailByToken(String token) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM LOCAL_SEARCH.A_CHANGE_EMAIL WHERE TOKEN = ?";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, token);
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException | IllegalArgumentException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE LANGUAGE">
    @Override
    public String updateLanguage(HttpSession session, int accountID, String language) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE A_ACCOUNT \n"
                    + "SET \n"
                    + "    ACCOUNT_LANGUAGE = ?,\n"
                    + "    UPDATED_BY = ?\n"
                    + "WHERE\n"
                    + "    ACCOUNT_ID = ?\n"
                    + "        AND ? IN (SELECT \n"
                    + "            LANGUAGE_CODE\n"
                    + "        FROM\n"
                    + "            A_LANGUAGE);";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            infoMysqlLogger.info(ps.toString());
            int i = 1;
            ps.setString(i++, language);
            ps.setInt(i++, accountID);
            ps.setInt(i++, accountID);
            ps.setString(i++, language);
            if (ps.executeUpdate() == 1) {
                session.setAttribute("language", language);
                return Constant.SUCCESS;
            }
            return Constant.FAIL;
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE AGENCY FOR ACCOUNT">
    @Override
    public String updateAgencyForAccount(int accountId, int isAgency) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE A_ACCOUNT SET IS_AGENCY = ? WHERE ACCOUNT_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, isAgency);
            ps.setInt(2, accountId);
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET PROFILE">
    /**
     * @param accountID : get account profile by account ID
     *
     * @return account profile
     */
    @Override
    public AccountDTO getProfile(int accountID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AccountDTO accountDTO = new AccountDTO();
        try {
            String sql = "SELECT \n"
                    + "    A.FIRST_NAME,\n"
                    + "    A.LAST_NAME,\n"
                    + "    A.EMAIL,\n"
                    + "    CE.EMAIL AS 'EMAIL_WAIT_ACTIVE',\n"
                    + "    A.ADDRESS,\n"
                    + "    A.CITY,\n"
                    + "    A.COUNTRY,\n"
                    + "    A.ACCOUNT_LANGUAGE,\n"
                    + "    A.MOBILE,\n"
                    + "    A.COMPANY,\n"
                    + "    A.WEBSITE,\n"
                    + "    A.LOGO,\n"
                    + "    A.AVATAR,\n"
                    + "    A.SECRET_KEY\n"
                    + "FROM\n"
                    + "    A_ACCOUNT AS A\n"
                    + "        LEFT JOIN\n"
                    + "    A_CHANGE_EMAIL AS CE ON A.ACCOUNT_ID = CE.ACCOUNT_ID\n"
                    + "        AND CE.VERIFIER_EMAIL = 0\n"
                    + "WHERE\n"
                    + "    A.ACCOUNT_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, accountID);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                accountDTO.setFirstName(rs.getString("FIRST_NAME"));
                accountDTO.setLastName(rs.getString("LAST_NAME"));
                accountDTO.setEmail(rs.getString("EMAIL"));
                accountDTO.setEmailWaitActive(rs.getString("EMAIL_WAIT_ACTIVE"));
                accountDTO.setAddress(rs.getString("ADDRESS"));
                accountDTO.setCity(rs.getString("CITY"));
                accountDTO.setCountry(rs.getString("COUNTRY"));
                accountDTO.setAccountLanguage(rs.getString("ACCOUNT_LANGUAGE"));
                accountDTO.setMobile(rs.getString("MOBILE"));
                accountDTO.setCompanyName(rs.getString("COMPANY"));
                accountDTO.setWebsite(rs.getString("WEBSITE"));
                accountDTO.setAvatar((rs.getString("AVATAR") == null || rs.getString("AVATAR").length() == 0) ? "unknown" : rs.getString("AVATAR"));
                accountDTO.setLogo((rs.getString("LOGO") == null || rs.getString("LOGO").length() == 0) ? "unknown" : rs.getString("LOGO"));
                accountDTO.setIsSetPassword((rs.getString("SECRET_KEY") == null || rs.getString("SECRET_KEY").isEmpty()));
                return accountDTO;
            }
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return accountDTO;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE PROFILE">
    /**
     * @param accountDTO to get info to update
     * @param session set logo, email and name when update success
     *
     * @return SUCCESS if update success, otherwise FAIL
     */
    @Override
    public String updateProfile(AccountDTO accountDTO, HttpSession session) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            String sql;
            sql = "UPDATE A_ACCOUNT \n"
                    + "SET FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ?, CITY = ?, \n"
                    + "COUNTRY = ?, ACCOUNT_LANGUAGE = ?, MOBILE = ?, COMPANY = ?, WEBSITE = ?, \n"
                    + "AVATAR = ?, LOGO = ?, UPDATED_BY = ? \n"
                    + "WHERE ACCOUNT_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, accountDTO.getFirstName());
            ps.setString(i++, accountDTO.getLastName());
            ps.setString(i++, accountDTO.getAddress());
            ps.setString(i++, accountDTO.getCity());
            ps.setString(i++, accountDTO.getCountry());
            ps.setString(i++, accountDTO.getAccountLanguage());
            ps.setString(i++, accountDTO.getMobile());
            ps.setString(i++, accountDTO.getCompanyName());
            ps.setString(i++, accountDTO.getWebsite());
            ps.setString(i++, accountDTO.getAvatar());
            ps.setString(i++, accountDTO.getLogo());
            ps.setInt(i++, accountDTO.getAccountId());
            ps.setInt(i++, accountDTO.getAccountId());

            infoMysqlLogger.info(ps.toString());
            if (ps.executeUpdate() == 1) {
                session.setAttribute("avatar", accountDTO.getAvatar());
                session.setAttribute("logo", accountDTO.getLogo());
                session.setAttribute("email", accountDTO.getEmail());
                session.setAttribute("name", accountDTO.getFirstName() + ' ' + accountDTO.getLastName());
                session.setAttribute("language", accountDTO.getAccountLanguage());
                return Constant.SUCCESS;
            }
            return Constant.FAIL;
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE PASSWORD">
    /**
     * @param accountDTO to get password and accountID
     *
     * @return true if password is correct, otherwise false
     */
    @Override
    public boolean isCorrectPassword(AccountDTO accountDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT SECRET_KEY FROM A_ACCOUNT WHERE ACCOUNT_ID = ?;";

            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, accountDTO.getAccountId());
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return BCrypt.checkpw(accountDTO.getSecretKey(), rs.getString("SECRET_KEY"));
            }
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    /**
     * @param accountDTO find account by accountID and update password
     *
     * @return SUCCESS if update password success, otherwise FAIL
     */
    @Override
    public String updatePassword(AccountDTO accountDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE A_ACCOUNT SET SECRET_KEY = ?, UPDATED_BY = ? WHERE ACCOUNT_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, BCrypt.hashpw(accountDTO.getNewSecretKey(), BCrypt.gensalt(12)));
            ps.setInt(2, accountDTO.getAccountId());
            ps.setInt(3, accountDTO.getAccountId());
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return Constant.FAIL;
    }
    //</editor-fold>
}
