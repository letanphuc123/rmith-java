package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.dto.GroupUserDTO;
import com.rmith.utils.GeneratedIDUtils;
import com.rmith.utils.PreparedStatementUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
 * @author Le Tan Phuc
 
 */
@Component
@Lazy
public class GroupUserDAOImpl implements GroupUserDAO {

    @Autowired
    private BasicDataSource basicDataSource;

    @Autowired
    @Lazy
    private GeneratedIDUtils generatedIDUtils;

    private final Logger infoMysqlLogger = LogManager.getLogger("info_mysql");
    private final Logger errorMysqlLogger = LogManager.getLogger("error_mysql");
    private PreparedStatementUtils preparedStatementUtils = new PreparedStatementUtils();

    //<editor-fold defaultstate="collapsed" desc="GET GROUP USER">
    @Override
    public List<GroupUserDTO> getGroupUser() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<GroupUserDTO> listGroupUserDTO = new ArrayList<>();
        try {
            String sql = "SELECT AGU.GROUP_NAME,\n"
                    + "    AGU.GROUP_ID,\n"
                    + "    AGU.IS_ADMIN_GROUP,\n"
                    + "    AGU.IS_FREE_GROUP,\n"
                    + "    AGU.LIST_MODULE,\n"
                    + "    AGU.UPDATED_DATE,IF(USER.NUMBER_USED IS NULL, 0, USER.NUMBER_USED) AS NUMBER_USED \n"
                    + "FROM\n"
                    + "    A_GROUP_USER AS AGU \n"
                    + "    LEFT JOIN (SELECT GROUP_ID, COUNT(GROUP_ID) AS NUMBER_USED FROM A_ACCOUNT  GROUP BY GROUP_ID) AS USER\n"
                    + "ON AGU.GROUP_ID = USER.GROUP_ID";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                GroupUserDTO groupUserDTO = new GroupUserDTO();
                groupUserDTO.setGroupName(rs.getString("GROUP_NAME"));
                groupUserDTO.setGroupId(rs.getInt("GROUP_ID"));
                groupUserDTO.setIsAdminGroup(rs.getInt("IS_ADMIN_GROUP"));
                groupUserDTO.setIsFreeGroup(rs.getInt("IS_FREE_GROUP"));
                groupUserDTO.setPermission(rs.getString("LIST_MODULE"));
                groupUserDTO.setUpdated(rs.getString("UPDATED_DATE"));
                groupUserDTO.setNumberUser(rs.getInt("NUMBER_USED"));
                listGroupUserDTO.add(groupUserDTO);
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
        return listGroupUserDTO;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHECK EXIST FREE GROUP">
    @Override
    public int getFreeGroupId() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = ""
                    + "SELECT \n"
                    + "    GROUP_ID\n"
                    + "FROM\n"
                    + "    A_GROUP_USER\n"
                    + "WHERE\n"
                    + "    IS_FREE_GROUP = 1;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("GROUP_ID");
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
        return 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD GROUP USER">
    @Override
    public boolean addGroupUser(GroupUserDTO groupUserDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO A_GROUP_USER (GROUP_ID, GROUP_NAME, LIST_MODULE, IS_ADMIN_GROUP, IS_FREE_GROUP, \n"
                    + "CREATED_BY, UPDATED_BY, CREATED_DATE) \n"
                    + "SELECT ?, ?, ?, ?, ?, ?, ?, NOW() FROM DUAL "
                    + "WHERE NOT EXISTS ( SELECT 1 FROM A_OR_GROUP_USER WHERE GROUP_ID=?)"
                    + "AND NOT EXISTS ( SELECT 1 FROM A_GROUP_USER WHERE GROUP_ID=?);";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);

            do {
                int groupId = generatedIDUtils.generatedID();
                int i = 1;
                ps.setInt(i++, groupId);
                ps.setString(i++, groupUserDTO.getGroupName());
                ps.setString(i++, groupUserDTO.getPermission());
                // if this is free group, then can not be admin group
                ps.setInt(i++, groupUserDTO.getIsAdminGroup());
                ps.setInt(i++, groupUserDTO.getIsFreeGroup());
                ps.setInt(i++, groupUserDTO.getCreateBy());
                ps.setInt(i++, groupUserDTO.getUpdateBy());
                ps.setInt(i++, groupId);
                ps.setInt(i++, groupId);
                infoMysqlLogger.info(ps.toString());
            } while (ps.executeUpdate() != 1);

            return true;
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

    //<editor-fold defaultstate="collapsed" desc="DELETE GROUP USER">
    @Override
    public String deleteGroupUser(int groupId) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "DELETE IGNORE FROM A_GROUP_USER WHERE GROUP_ID = ? AND IS_FREE_GROUP = 0;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, groupId);
            infoMysqlLogger.info(ps.toString());
            String result = ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
            return preparedStatementUtils.checkConstraint(ps, "FK_A_ACCOUNT_A_GROUP_USER") ? Constant.HAS_REF : result;
            
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

    /**
     * @param groupId: current group will be checked has any account
     *
     * @return status this package has any account
     */
    @Override
    public boolean checkGroupHasAccount(int groupId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = basicDataSource.getConnection();
            String sql = "SELECT COUNT(*) \n"
                    + "FROM A_ACCOUNT \n"
                    + "WHERE GROUP_ID=?";
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, groupId);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
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

    //<editor-fold defaultstate="collapsed" desc="UPDATE GROUP USER">
    @Override
    public boolean updateGroupUser(GroupUserDTO groupUserDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE A_GROUP_USER \n"
                    + "SET GROUP_NAME = ?, IS_ADMIN_GROUP = ?, UPDATED_BY = ? \n"
                    + "WHERE GROUP_ID = ?;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, groupUserDTO.getGroupName());
            ps.setInt(i++, groupUserDTO.getIsAdminGroup());
            ps.setInt(i++, groupUserDTO.getUpdateBy());
            ps.setInt(i++, groupUserDTO.getGroupId());
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

    //<editor-fold defaultstate="collapsed" desc="CHECK MODULE EXIST IN GROUPUSER">
    @Override
    public boolean checkModuleExitInGroupUser(int moduleId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT 1 FROM DUAL WHERE EXISTS(SELECT 1 FROM A_GROUP_USER WHERE find_in_set((SELECT MODULE_CODE FROM LOCAL_SEARCH.A_MODULE WHERE MODULE_ID = ?),LIST_MODULE));";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, moduleId);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
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
//</editor-fold>
}
