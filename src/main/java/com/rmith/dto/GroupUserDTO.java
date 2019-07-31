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
public class GroupUserDTO implements Serializable {

    private int groupId;
    private String groupName;
    private int numberUser;
    private int isAdminGroup;
    private int isFreeGroup;
    private String permission;
    private int createBy;
    private String created;
    private int updateBy;
    private String updated;
    private Map<String, String> errorMap = new HashMap<>();

    public GroupUserDTO() {
        this.groupId = 0;
        this.groupName = "";
        this.isAdminGroup = 0;
        this.isFreeGroup = 0;
        this.permission = "";
        this.createBy = 0;
        this.created = "";
        this.updateBy = 0;
        this.updated = "";
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        String validate = ValidateUtils.validateString(groupName, 0);
        if (!validate.equals(Constant.SUCCESS)) {
            errorMap.put("groupName", "group Name " + validate);
        } else {
            this.groupName = groupName;
        }
    }

    public int getNumberUser() {
        return numberUser;
    }

    public void setNumberUser(int numberUser) {
        this.numberUser = numberUser;
    }

    public int getIsAdminGroup() {
        return isAdminGroup;
    }

    public void setIsAdminGroup(int isAdminGroup) {
        this.isAdminGroup = isAdminGroup;
    }

    public int getIsFreeGroup() {
        return isFreeGroup;
    }

    public void setIsFreeGroup(int isFreeGroup) {
        this.isFreeGroup = isFreeGroup;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        String validate = ValidateUtils.validateString(permission,
                Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.permission = permission;
        } else {
            this.permission = "";
        }
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        String validate = ValidateUtils.validateString(created, Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.created = created;
        } else {
            this.created = "";
        }
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        String validate = ValidateUtils.validateString(updated, Integer.MAX_VALUE);
        if (validate.equals(Constant.SUCCESS)) {
            this.updated = updated;
        } else {
            this.updated = "";
        }
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

}
