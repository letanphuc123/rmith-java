package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dao.CategoryDAO;
import com.rmith.dao.MenuDAO;
import com.rmith.dao.ModuleDAO;
import com.rmith.dto.CategoryDTO;
import com.rmith.dto.MenuDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Service
@Lazy
public class MenuServiceImpl implements MenuService {

    @Autowired
    @Lazy
    private MenuDAO menuDAO;

    @Autowired
    @Lazy
    private CategoryDAO categoryDAO;

    @Autowired
    @Lazy
    private ModuleDAO moduleDAO;

    //<editor-fold defaultstate="collapsed" desc="GET MENU LIST">
    @Override
    public List<MenuDTO> getMenuList() {
        return menuDAO.getMenuList();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET MENU">
    @Override
    public List<CategoryDTO> getModuleByGroupUser(int groupUser, int isAdmin) {

        List<CategoryDTO> listCategory = categoryDAO.getCategoryByMenuCode("administrator");

        //Get list module for each category
        listCategory.forEach((categoryDTO) -> {
            categoryDTO.setListModule(moduleDAO.getModuleByGroupUser(groupUser, "administrator", categoryDTO.getCategoryId(), isAdmin));
        });
        return listCategory;
    }
    //</editor-fold>

}
