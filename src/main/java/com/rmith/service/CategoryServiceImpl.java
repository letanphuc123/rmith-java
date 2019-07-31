package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dao.CategoryDAO;
import com.rmith.dto.CategoryDTO;
import com.google.gson.Gson;
import java.util.List;
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
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    @Lazy
    private CategoryDAO categoryDAO;

    //<editor-fold defaultstate="collapsed" desc="GET ALL CATEGORY">
    @Override
    public String getAllCategory() {
        return new Gson().toJson(categoryDAO.getAllCategory());
    }
    //</editor-fold>

    @Override
    public String addCategory(CategoryDTO categoryDTO) {
        return categoryDAO.addCategory(categoryDTO);
    }

    @Override
    public String updateCategory(CategoryDTO categoryDTO) {
        return categoryDAO.updateCategory(categoryDTO);
    }

    @Override
    public String deleteCategory(int categoryId) {
        return categoryDAO.deleteCategory(categoryId);
    }

    @Override
    public List<CategoryDTO> getAllCategoryList() {
        return categoryDAO.getAllCategory();
    }

}
