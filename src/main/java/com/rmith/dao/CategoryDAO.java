package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.CategoryDTO;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface CategoryDAO {

    List<CategoryDTO> getAllCategory();

    List<CategoryDTO> getCategoryByMenuCode(String menuCode);

    String addCategory(CategoryDTO categoryDTO);

    String updateCategory(CategoryDTO categoryDTO);

    String deleteCategory(int categoryId);

}
