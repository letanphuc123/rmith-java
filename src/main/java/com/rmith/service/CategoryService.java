package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.CategoryDTO;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface CategoryService {

    String getAllCategory();

    List<CategoryDTO> getAllCategoryList();

    String addCategory(CategoryDTO categoryDTO);

    String updateCategory(CategoryDTO categoryDTO);

    String deleteCategory(int categoryId);
}
