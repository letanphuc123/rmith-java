package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.GroupUserDTO;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface GroupUserDAO {

    List<GroupUserDTO> getGroupUser();

    int getFreeGroupId();

    boolean addGroupUser(GroupUserDTO accountDTO);

    String deleteGroupUser(int id);

    boolean updateGroupUser(GroupUserDTO groupUserDTO);

    boolean checkGroupHasAccount(int groupId);

    boolean checkModuleExitInGroupUser(int moduleId);

}
