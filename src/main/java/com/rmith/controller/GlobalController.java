package com.rmith.controller;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.config.TrialExpiredException;
import com.rmith.service.MenuService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.RedirectView;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@ControllerAdvice
public class GlobalController {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Autowired
    @Lazy
    private MenuService menuService;
    
    private final Logger logger = LogManager.getLogger();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD ATTRIBUTE REQUEST">
    @ModelAttribute
    public void addAttributes(ModelMap modelMap,
            HttpSession session,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws TrialExpiredException {
        if (!httpServletRequest.getMethod().equals("GET") || session.getAttribute("groupID") == null) {
            return;
        }

        int groupUser = (int) session.getAttribute("groupID");
        int isAdmin = (int) session.getAttribute("isAdmin");

        Map<String, String> languageDefault = new HashMap<>();
        modelMap.put("language", languageDefault);
        modelMap.put("listCategory", menuService.getModuleByGroupUser(groupUser, isAdmin));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="HANDLE SERVER ERROR">
    @ExceptionHandler(Exception.class)
    public RedirectView handleAllControllerException(Exception ex) {
        logger.error(ExceptionUtils.getStackTrace(ex));
        RedirectView rw = new RedirectView("500");
        return rw;
    }
//</editor-fold>
}
