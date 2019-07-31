package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.MenuDTO;
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
import org.springframework.stereotype.Service;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Service
@Lazy
public class MenuDAOImpl implements MenuDAO {

    @Autowired
    private BasicDataSource basicDataSource;

    private final Logger infoMysqlLogger = LogManager.getLogger("info_mysql");
    private final Logger errorMysqlLogger = LogManager.getLogger("error_mysql");

    //<editor-fold defaultstate="collapsed" desc="GET MENU LIST">
    @Override
    public List<MenuDTO> getMenuList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MenuDTO> menuList = new ArrayList<>();
        try {
            String sql
                    = "SELECT MENU_CODE, MENU_NAME FROM A_MENU;";
            con = basicDataSource.getConnection();
            ps = (PreparedStatement) con.prepareStatement(sql);
            infoMysqlLogger.info(ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                MenuDTO menuDTO = new MenuDTO();
                menuDTO.setMenuCode(rs.getString("MENU_CODE"));
                menuDTO.setMenuName(rs.getString("MENU_NAME"));
                menuList.add(menuDTO);
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
        return menuList;
    }
    //</editor-fold>

}
