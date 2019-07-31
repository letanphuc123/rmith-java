package com.rmith.dto;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.utils.ValidateUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public class CategoryDTO implements Serializable {

    private int categoryId;
    private String categoryName;
    private String menuCode;
    private String menuName;
    private int orderNumber;
    private String icon;
    private String categoryCode;
    private int createdBy;
    private String createdDate;
    private String updatedDate;
    private int updatedBy;
    private List<ModuleDTO> listModule;

    private Map<String, Object> errorMap = new HashMap<>();

    public CategoryDTO() {
        this.categoryId = 0;
        this.categoryName = "";
        this.menuCode = "";
        this.menuName = "";
        this.orderNumber = 0;
        this.icon="";
        this.createdBy = 0;
        this.categoryCode = "";
        this.createdDate = "";
        this.updatedDate = "";
        this.updatedBy = 0;
        this.listModule = new ArrayList<>();
    }

    public String getMenuCode() {
        return menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        String validate = ValidateUtils.validateString(menuName,
                Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.menuName = menuName;
        } else {
            errorMap.put("menuName", validate);
        }
    }

    public void setMenuCode(String menuCode) {
        String validate = ValidateUtils.validateString(menuCode,
                Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.menuCode = menuCode;
        } else {
            errorMap.put("menuCode", validate);
        }
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        String validate = ValidateUtils.validateString(categoryName,
                Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.categoryName = categoryName;
        } else {
            errorMap.put("categoryName", validate);
        }
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String created) {
        if (ValidateUtils.validateDateTime(created).equals(Constant.SUCCESS)) {
            this.createdDate = created;
        }
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updated) {
        if (ValidateUtils.validateDateTime(updated).equals(Constant.SUCCESS)) {
            this.updatedDate = updated;
        }
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Map<String, Object> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, Object> errorMap) {
        this.errorMap = errorMap;
    }

    public List<ModuleDTO> getListModule() {
        return listModule;
    }

    public void setListModule(List<ModuleDTO> listModule) {
        this.listModule = listModule;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
      String validate = ValidateUtils.validateString(categoryCode,
                Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.categoryCode = categoryCode;
        } else {
            errorMap.put("categoryCode", validate);
        }
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
