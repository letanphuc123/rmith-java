package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dao.ModuleDAO;
import com.rmith.dto.ModuleDTO;
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
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    @Lazy
    private ModuleDAO moduleDAO;

    //<editor-fold defaultstate="collapsed" desc="ADD MODULE">
    @Override
    public String addModule(ModuleDTO module) {
        return moduleDAO.addModule(module);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE MODULE">
    @Override
    public String updateModule(ModuleDTO module) {
        return moduleDAO.updateModule(module);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE MODULE">
    @Override
    public String deleteModule(int moduleId) {
        return moduleDAO.deleteModule(moduleId);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET ALL MODULE">
    @Override
    public List<ModuleDTO> getAllModule() {
        return moduleDAO.getAllModule();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET MODULE BY GROUP USER ID">
    @Override
    public List<ModuleDTO> getModuleByGroupUserId(int groupUserId) {
        return moduleDAO.getModuleByGroupId(groupUserId);
    }
    //</editor-fold>
}
