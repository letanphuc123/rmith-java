package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.ModuleDTO;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public interface ModuleService {

    String addModule(ModuleDTO module);

    String updateModule(ModuleDTO module);

    String deleteModule(int moduleId);

    List<ModuleDTO> getAllModule();

    List<ModuleDTO> getModuleByGroupUserId(int groupUserId);

}
