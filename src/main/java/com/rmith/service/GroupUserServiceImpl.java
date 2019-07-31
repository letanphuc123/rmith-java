package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dao.GroupUserDAO;
import com.rmith.dto.GroupUserDTO;
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
public class GroupUserServiceImpl implements GroupUserService {

    @Autowired
    @Lazy
    private GroupUserDAO groupUserDAO;

    //<editor-fold defaultstate="collapsed" desc="ADD GROUP USER">
    @Override
    public boolean addGroupUser(GroupUserDTO groupUserDTO) {
        // if this group can be not admin and free
        if (groupUserDTO.getIsFreeGroup() == 1 && groupUserDTO.getIsAdminGroup() == 1) {
            return false;
        }
        if (groupUserDTO.getIsAdminGroup() == 0 && groupUserDTO.getIsFreeGroup() == 0) {
            return groupUserDAO.addGroupUser(groupUserDTO);
        }
        /* Check exist free group, if exist then do not set this group is free */
        if (groupUserDTO.getIsFreeGroup() == 1 && groupUserDAO.getFreeGroupId() != 0) {
            return false;
        }
        return groupUserDAO.addGroupUser(groupUserDTO);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET GROUP USER">
    @Override
    public List<GroupUserDTO> getGroupUser() {
        return groupUserDAO.getGroupUser();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IS EXIST FREE GROUP">
    @Override
    public boolean isExistFreeGroup() {
        return groupUserDAO.getFreeGroupId() != 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE GROUP USER">
    @Override
    public String deleteGroupUser(int groupId) {
        return groupUserDAO.deleteGroupUser(groupId);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE GROUP USER">
    @Override
    public boolean updateGroupUser(GroupUserDTO groupUserDTO) {
        if (groupUserDTO.getIsFreeGroup() == 1 && groupUserDTO.getIsAdminGroup() == 1) {
            return false;
        }
        return groupUserDAO.updateGroupUser(groupUserDTO);
    }
    //</editor-fold>

}
