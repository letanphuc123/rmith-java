package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.dto.CategoryDTO;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Component
@Lazy
public class CategoryDAOImpl implements CategoryDAO {

    @Autowired
    private BasicDataSource basicDataSource;

    @Autowired
    @Lazy
    private GeneratedIDUtils generatedIDUtils;

    private final Logger infoMysqlLogger = LogManager.getLogger("info_mysql");
    private final Logger errorMysqlLogger = LogManager.getLogger("error_mysql");
    private final PreparedStatementUtils preparedStatementUtils = new PreparedStatementUtils();

    //<editor-fold defaultstate="collapsed" desc="GET ALL CATEGORY">
    @Override
    public List<CategoryDTO> getAllCategory() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoryDTO> listCategory = new ArrayList<>();
        try {
            String sql = "SELECT A_CATEGORY.CATEGORY_ID, "
                    + "A_CATEGORY.CATEGORY_NAME, A_CATEGORY.CATEGORY_CODE, "
                    + "A_CATEGORY.MENU_CODE, "
                    + "A_MENU.MENU_NAME, A_CATEGORY.ORDER_NUMBER, "
                    + "A_CATEGORY.UPDATED_DATE\n"
                    + "FROM A_CATEGORY\n"
                    + "LEFT JOIN A_MENU\n"
                    + "ON A_CATEGORY.MENU_CODE = A_MENU.MENU_CODE "
                    + "ORDER BY A_CATEGORY.MENU_CODE, A_CATEGORY.ORDER_NUMBER;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setCategoryId(rs.getInt("CATEGORY_ID"));
                categoryDTO.setCategoryName(rs.getString("CATEGORY_NAME"));
                categoryDTO.setCategoryCode(rs.getString("CATEGORY_CODE"));
                categoryDTO.setMenuCode(rs.getString("MENU_CODE"));
                categoryDTO.setMenuName(rs.getString("MENU_NAME"));
                categoryDTO.setOrderNumber(rs.getInt("ORDER_NUMBER"));
                categoryDTO.setUpdatedDate(rs.getString("UPDATED_DATE"));
                listCategory.add(categoryDTO);
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
                errorMysqlLogger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return listCategory;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET CATEGORY BY MENU CODE">
    @Override
    public List<CategoryDTO> getCategoryByMenuCode(String menuCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoryDTO> listCategory = new ArrayList<>();
        try {
            String sql = "SELECT CATEGORY_ID, CATEGORY_NAME, CATEGORY_CODE, ICON\n"
                    + "FROM A_CATEGORY\n"
                    + "WHERE MENU_CODE = ?\n"
                    + "ORDER BY ORDER_NUMBER;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, menuCode);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setCategoryId(rs.getInt("CATEGORY_ID"));
                categoryDTO.setCategoryName(rs.getString("CATEGORY_NAME"));
                categoryDTO.setCategoryCode(rs.getString("CATEGORY_CODE"));
                categoryDTO.setIcon(rs.getString("ICON") );
                listCategory.add(categoryDTO);
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
                errorMysqlLogger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return listCategory;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD CATEGORY">
    @Override
    public String addCategory(CategoryDTO categoryDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            //IS_BETA, CATEGORY_CODE nam sau MENU_CODE
            con = basicDataSource.getConnection();
            String sql = "INSERT INTO A_CATEGORY(CATEGORY_ID, CATEGORY_NAME, MENU_CODE, ORDER_NUMBER, CATEGORY_CODE, CREATED_BY, UPDATED_BY, CREATED_DATE)\n"
                    + "SELECT ?,?,?,?,?,?,?,NOW() FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM A_CATEGORY WHERE CATEGORY_ID =?);";
            ps = (PreparedStatement) con.prepareStatement(sql);

            infoMysqlLogger.info(ps.toString());
            do {
                int categoryId = generatedIDUtils.generatedID();
                int i = 1;
                ps.setInt(i++, categoryId);
                ps.setString(i++, categoryDTO.getCategoryName());
                ps.setString(i++, categoryDTO.getMenuCode());
                ps.setInt(i++, categoryDTO.getOrderNumber());
                ps.setString(i++, categoryDTO.getCategoryCode());
                ps.setInt(i++, categoryDTO.getCreatedBy());
                ps.setInt(i++, categoryDTO.getCreatedBy());
                ps.setInt(i++, categoryId);
            } while (ps.executeUpdate() != 1);

            return Constant.SUCCESS;
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

    //<editor-fold defaultstate="collapsed" desc="UPDATE CATEGORY">
    @Override
    public String updateCategory(CategoryDTO categoryDTO) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = basicDataSource.getConnection();
            String sql = "UPDATE A_CATEGORY SET CATEGORY_NAME=?, MENU_CODE=?, ORDER_NUMBER=?,UPDATED_BY=? WHERE CATEGORY_ID=?;";
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, categoryDTO.getCategoryName());
            ps.setString(i++, categoryDTO.getMenuCode());
            ps.setInt(i++, categoryDTO.getOrderNumber());
            ps.setInt(i++, categoryDTO.getUpdatedBy());
            ps.setInt(i++, categoryDTO.getCategoryId());
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

    //<editor-fold defaultstate="collapsed" desc="DELETE CATEGORY">
    @Override
    public String deleteCategory(int categoryId) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = basicDataSource.getConnection();
            String sql = "DELETE IGNORE FROM A_CATEGORY WHERE CATEGORY_ID=?";
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, categoryId);
            infoMysqlLogger.info(ps.toString());
            String result = ps.executeUpdate() == 1 ? Constant.SUCCESS : Constant.FAIL;
            return preparedStatementUtils.checkConstraint(ps, "FK_A_MODULE_A_CATEGORY") ? Constant.HAS_REF : result;

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
