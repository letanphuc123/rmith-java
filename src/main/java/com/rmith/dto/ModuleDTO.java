package com.rmith.dto;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.utils.ValidateUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public class ModuleDTO implements Serializable {

    private int moduleId;
    private String moduleCode;
    private String moduleName;
    private String menuCode;
    private String menuName;
    private int categoryId;
    private int orderNumber;
    private String categoryName;
    private int createdBy;
    private String createdDate;
    private String updatedDate;
    private int updatedBy;
    private int isBeta;
    private int hadPermission;
    private Map<String, Object> errorMap = new HashMap<>();

    public ModuleDTO() {
        this.moduleId = 0;
        this.moduleCode = "";
        this.moduleName = "";
        this.menuCode = "";
        this.orderNumber = 0;
        this.createdBy = 0;
        this.createdDate = "";
        this.updatedDate = "";
        this.updatedBy = 0;
        this.moduleId = 0;
        this.isBeta = 0;
        this.categoryId = 0;
        this.categoryName = "";
        this.hadPermission = 0;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        String validate = ValidateUtils.validateString(moduleCode, 50);
        if (validate.equals(Constant.SUCCESS) && ValidateUtils.isValidUrlPath(moduleCode)) {
            this.moduleCode = moduleCode;
        } else {
            errorMap.put("moduleCode", validate);
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        String validate = ValidateUtils.validateString(moduleName, Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.moduleName = moduleName;
        } else {
            errorMap.put("moduleName", validate);
        }
    }

    public String getMenuCode() {
        return menuCode;
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

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int id) {
        this.moduleId = id;
    }

    public Map<String, Object> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, Object> errorMap) {
        this.errorMap = errorMap;
    }

    public int getIsBeta() {
        return isBeta;
    }

    public void setIsBeta(int isBeta) {
        this.isBeta = isBeta;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public int getHadPermission() {
        return hadPermission;
    }

    public void setHadPermission(int hadPermission) {
        this.hadPermission = hadPermission;
    }
}
