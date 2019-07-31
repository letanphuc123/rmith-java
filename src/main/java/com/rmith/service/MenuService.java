package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.CategoryDTO;
import com.rmith.dto.MenuDTO;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface MenuService {

    List<MenuDTO> getMenuList();
    
    List<CategoryDTO> getModuleByGroupUser(int groupUser, int isAdmin);

}
