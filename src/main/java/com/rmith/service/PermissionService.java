package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.GroupUserDTO;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface PermissionService {

    boolean editPermission(GroupUserDTO groupUserDTO);

    boolean checkPermissionSite(int packageId, int accountId, int siteId);

    boolean checkPermissionBeforeGetGoogleAPI(int packageId, int accountId, String googleAccountId, String locationId);
}
