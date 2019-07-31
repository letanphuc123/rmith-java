package com.rmith.dto;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public class MenuDTO implements Serializable {

    private String menuCode;
    private String menuName;
    private String icon;
    private List<CategoryDTO> listCategory;

    public MenuDTO() {
        this.menuCode = "";
        this.menuName = "";
        this.icon = "";
        this.listCategory = new ArrayList<>();
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        if (menuCode != null) {
            this.menuCode = menuCode;
        }
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        if (menuName != null) {
            this.menuName = menuName;
        }
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        if (icon != null) {
            this.icon = icon;
        }
    }

    public List<CategoryDTO> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<CategoryDTO> listCategory) {
        this.listCategory = listCategory;
    }

}
