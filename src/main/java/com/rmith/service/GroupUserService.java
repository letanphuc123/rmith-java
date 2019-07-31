package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.GroupUserDTO;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface GroupUserService {

    boolean addGroupUser(GroupUserDTO groupUserDTO);

    List<GroupUserDTO> getGroupUser();

    boolean isExistFreeGroup();

    String deleteGroupUser(int id);

    boolean updateGroupUser(GroupUserDTO accountDTO);

}
