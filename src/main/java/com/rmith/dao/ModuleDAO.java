package com.rmith.dao;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.ModuleDTO;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface ModuleDAO {

    String addModule(ModuleDTO module);

    String updateModule(ModuleDTO module);

    String deleteModule(int moduleId);

    List<ModuleDTO> getAllModule();

    List<ModuleDTO> getModuleByGroupUser(int groupUser, String menuCode, int categoryId, int isAdmin);

    List<ModuleDTO> getModuleByGroupId(int groupUserId);

}
