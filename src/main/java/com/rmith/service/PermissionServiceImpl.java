package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dao.PermissionDAO;
import com.rmith.dto.GroupUserDTO;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Service
@Lazy
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    @Lazy
    private PermissionDAO permissionDAO;

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> repository;

    //<editor-fold defaultstate="collapsed" desc="EDIT PERMISSION">
    @Override
    public boolean editPermission(GroupUserDTO groupUserDTO) {
        if (permissionDAO.editPermission(groupUserDTO)) {
            Set<String> sessionIDKeyset = repository
                    .findByPrincipalName(String.valueOf(groupUserDTO.getGroupId())).keySet();
            sessionIDKeyset.forEach(session -> repository.deleteById(session));
            return true;
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHECK PERMISSION GOOGLE BUSINESS">
    @Override
    public boolean checkPermissionSite(int packageId, int accountId, int siteId) {
        return permissionDAO.checkPermissionSite(packageId, accountId, siteId);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHECK PERMISSION BEFORE GET GOOGLE API">
    @Override
    public boolean checkPermissionBeforeGetGoogleAPI(int packageId, int accountId, String googleAccountId, String locationId) {
        return permissionDAO.checkPermissionBeforeGetGoogleAPI(packageId, accountId, googleAccountId, locationId);
    }
    //</editor-fold>

}
