package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.dto.ModuleDTO;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Lazy
@Service
public class ModuleDAOImpl implements ModuleDAO {

    @Autowired
    private BasicDataSource basicDataSource;
    private final Logger infoMysqlLogger = LogManager.getLogger("info_mysql");
    private final Logger errorMysqlLogger = LogManager.getLogger("error_mysql");

    //<editor-fold defaultstate="collapsed" desc="ADD MODULE">
    @Override
    public String addModule(ModuleDTO moduleDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            //IS_BETA, CATEGORY_CODE nam sau MENU_CODE
            con = basicDataSource.getConnection();
            String sql = "INSERT INTO A_MODULE(MODULE_CODE, MODULE_NAME, MENU_CODE, CATEGORY_ID, IS_BETA, ORDER_NUMBER, CREATED_BY, UPDATED_BY, CREATED_DATE) \n"
                    + "VALUES (?,?,?,?,?,?,?,?,NOW());";
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, moduleDTO.getModuleCode());
            ps.setString(i++, moduleDTO.getModuleName());
            ps.setString(i++, moduleDTO.getMenuCode());
            ps.setInt(i++, moduleDTO.getCategoryId());
            ps.setInt(i++, moduleDTO.getIsBeta());
            ps.setInt(i++, moduleDTO.getOrderNumber());
            ps.setInt(i++, moduleDTO.getCreatedBy());
            ps.setInt(i++, moduleDTO.getCreatedBy());

            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
            return Constant.FAIL;
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
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE MODULE">
    @Override
    public String updateModule(ModuleDTO moduleDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = basicDataSource.getConnection();
            String sql = "UPDATE A_MODULE SET MODULE_NAME=?, MENU_CODE=?, CATEGORY_ID=?, IS_BETA=?, ORDER_NUMBER=?,UPDATED_BY=? WHERE MODULE_ID=?";
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, moduleDTO.getModuleName());
            ps.setString(i++, moduleDTO.getMenuCode());
            ps.setInt(i++, moduleDTO.getCategoryId());
            ps.setInt(i++, moduleDTO.getIsBeta());
            ps.setInt(i++, moduleDTO.getOrderNumber());
            ps.setInt(i++, moduleDTO.getUpdatedBy());
            ps.setInt(i++, moduleDTO.getModuleId());
            infoMysqlLogger.info(ps.toString());
            return ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
        } catch (SQLException ex) {
            errorMysqlLogger.error(ExceptionUtils.getStackTrace(ex));
            return Constant.FAIL;
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
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE MODULE">
    @Override
    public String deleteModule(int moduleId) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = basicDataSource.getConnection();
            String sql = "DELETE FROM A_MODULE WHERE MODULE_ID=?";
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, moduleId);
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

    //<editor-fold defaultstate="collapsed" desc="GET ALL MODULE">
    @Override
    public List<ModuleDTO> getAllModule() {
        List<ModuleDTO> moduleDTOList = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT A_MODULE.MODULE_ID, \n"
                    + "A_MODULE.MODULE_CODE, \n"
                    + "A_MODULE.MODULE_NAME, \n"
                    + "A_MODULE.MENU_CODE, \n"
                    + "A_MENU.MENU_NAME, \n"
                    + "A_MODULE.CATEGORY_ID, \n"
                    + "A_CATEGORY.CATEGORY_NAME,\n"
                    + "A_MODULE.IS_BETA, \n"
                    + "A_MODULE.ORDER_NUMBER,\n"
                    + "A_MODULE.CREATED_DATE,\n"
                    + "A_MODULE.UPDATED_DATE\n"
                    + "FROM A_MODULE \n"
                    + "LEFT JOIN A_MENU \n"
                    + "ON A_MODULE.MENU_CODE = A_MENU.MENU_CODE\n"
                    + "LEFT JOIN A_CATEGORY \n"
                    + "ON A_MODULE.CATEGORY_ID = A_CATEGORY.CATEGORY_ID;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                ModuleDTO moduleDTO = new ModuleDTO();

                moduleDTO.setModuleId(rs.getInt("MODULE_ID"));
                moduleDTO.setModuleName(rs.getString("MODULE_NAME"));
                moduleDTO.setModuleCode(rs.getString("MODULE_CODE"));
                moduleDTO.setMenuCode(rs.getString("MENU_CODE"));
                moduleDTO.setMenuName(rs.getString("MENU_NAME"));
                moduleDTO.setCategoryId(rs.getInt("CATEGORY_ID"));
                moduleDTO.setCategoryName(rs.getString("CATEGORY_NAME"));
                moduleDTO.setIsBeta(rs.getInt("IS_BETA"));
                moduleDTO.setOrderNumber(rs.getInt("ORDER_NUMBER"));
                moduleDTO.setCreatedDate(rs.getString("CREATED_DATE"));
                moduleDTO.setUpdatedDate(rs.getString("UPDATED_DATE"));

                moduleDTOList.add(moduleDTO);
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
        return moduleDTOList;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET MODULE BY GROUP USER">
    @Override
    public List<ModuleDTO> getModuleByGroupUser(int groupUser, String menuCode, int categoryId, int isAdmin) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ModuleDTO> moduleDTOList = new ArrayList<>();
        try {
            String sql;
            if (isAdmin == 1) {
                sql = "SELECT MODULE_CODE, MODULE_NAME\n"
                        + "FROM A_MODULE\n"
                        + "WHERE MENU_CODE = ? AND CATEGORY_ID = ? \n"
                        + "ORDER BY ORDER_NUMBER;";
            } else {
                sql = "SELECT MODULE_CODE, MODULE_NAME\n"
                        + "FROM A_MODULE\n"
                        + "WHERE (\n"
                        + "		FIND_IN_SET (MODULE_CODE, (SELECT LIST_MODULE FROM A_GROUP_USER WHERE GROUP_ID = ?))\n"
                        + "	  OR \n"
                        + "		FIND_IN_SET (MODULE_CODE, (SELECT LIST_MODULE FROM A_OR_GROUP_USER WHERE GROUP_ID = ?))\n"
                        + "	  )\n"
                        + "AND MENU_CODE = ? AND CATEGORY_ID = ? \n"
                        + "ORDER BY ORDER_NUMBER;";
            }
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            if (isAdmin == 0) {
                ps.setInt(i++, groupUser);
                ps.setInt(i++, groupUser);
            }
            ps.setString(i++, menuCode);
            ps.setInt(i++, categoryId);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                String moduleCode = rs.getString("MODULE_CODE");
                String moduleName = rs.getString("MODULE_NAME");
                ModuleDTO moduleDTO = new ModuleDTO();
                moduleDTO.setModuleCode(moduleCode);
                moduleDTO.setModuleName(moduleName);
                moduleDTOList.add(moduleDTO);
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
        return moduleDTOList;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET MODULE BY GROUP ID">
    @Override
    public List<ModuleDTO> getModuleByGroupId(int groupUserId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ModuleDTO> listModule = new ArrayList<>();
        try {
            String sql = "SELECT M.MODULE_CODE,M.MODULE_NAME,MN.MENU_NAME, C.CATEGORY_NAME\n"
                    + "FROM A_MODULE AS M, A_MENU AS MN, A_GROUP_USER AS G, A_CATEGORY AS C\n"
                    + "WHERE G.GROUP_ID=? AND FIND_IN_SET(M.MODULE_CODE,G.LIST_MODULE)\n"
                    + "AND M.MENU_CODE != 'organization-agency'\n"
                    + "AND M.CATEGORY_ID = C.CATEGORY_ID\n"
                    + "AND M.MENU_CODE=MN.MENU_CODE ORDER BY M.MENU_CODE";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, groupUserId);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                ModuleDTO moduleDTO = new ModuleDTO();
                moduleDTO.setModuleCode(rs.getString("MODULE_CODE"));
                moduleDTO.setModuleName(rs.getString("MODULE_NAME"));
                moduleDTO.setMenuName(rs.getString("MENU_NAME"));
                moduleDTO.setCategoryName(rs.getString("CATEGORY_NAME"));
                listModule.add(moduleDTO);
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
        return listModule;
    }
    //</editor-fold>	 

}
