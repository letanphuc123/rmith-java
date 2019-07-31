package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.GroupUserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
//</editor-fold>

/**
 *
 * @author Nguyen The An
 * @email nguyenthean@fabercompany.co.jp
 */
@Component
@Lazy
public class PermissionDAOImpl implements PermissionDAO {

    @Autowired
    private BasicDataSource basicDataSource;
    private final Logger infoMysqlLogger = LogManager.getLogger("info_mysql");
    private final Logger errorMysqlLogger = LogManager.getLogger("error_mysql");

    //<editor-fold defaultstate="collapsed" desc="EDIT PERMISSION">
    @Override
    public boolean editPermission(GroupUserDTO groupUserDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE A_GROUP_USER SET LIST_MODULE = ? WHERE GROUP_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, groupUserDTO.getPermission());
            ps.setInt(2, groupUserDTO.getGroupId());
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1;
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
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHECK PERMISSION SITE">
    @Override
    public boolean checkPermissionSite(int packageId, int accountId, int siteId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT S.SITE_ID\n"
                    + "FROM SITES AS S \n"
                    + "LEFT JOIN A_ACCOUNT AA ON S.CREATED_BY = AA.ACCOUNT_ID AND AA.PACKAGE_ID = ? \n"
                    + "LEFT JOIN SITE_SHARE AS SS ON FIND_IN_SET(S.SITE_ID ,LIST_SITE_ID) AND SS.ACCOUNT_ID=?\n"
                    + "WHERE S.SITE_ID = ?";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, packageId);
            ps.setInt(i++, accountId);
            ps.setInt(i++, siteId);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
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
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHECK PERMISSION BEFORE GET GOOGLE API">
    @Override
    public boolean checkPermissionBeforeGetGoogleAPI(int packageId, int accountId, String googleAccountId, String locationId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT \n"
                    + "    S.SITE_ID\n"
                    + "FROM\n"
                    + "    SITES AS S\n"
                    + "        LEFT JOIN\n"
                    + "    A_ACCOUNT AA ON S.CREATED_BY = AA.ACCOUNT_ID\n"
                    + "        LEFT JOIN\n"
                    + "    SITE_SHARE AS SS ON FIND_IN_SET(S.SITE_ID, LIST_SITE_ID)\n"
                    + "WHERE\n"
                    + "    (AA.PACKAGE_ID = ?\n"
                    + "        OR SS.ACCOUNT_ID = ?)\n"
                    + "        AND S.GOOGLE_ACCOUNT_ID = ?\n"
                    + "        AND S.LOCATION_ID = ?";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, packageId);
            ps.setInt(i++, accountId);
            ps.setString(i++, googleAccountId);
            ps.setString(i++, locationId);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
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
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }
    //</editor-fold>

}
