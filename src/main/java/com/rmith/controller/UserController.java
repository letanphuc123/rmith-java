package com.rmith.controller;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
@Controller
public class UserController {
    
    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Autowired
    @Lazy
    private AccountService accountService;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="DASHBOARD">
    @GetMapping("/dashboard")
    public String dashboard(ModelMap modelMap) {
        modelMap.addAttribute("accountDTOList", accountService.getListAccount());
        return "/user/dashboard";
    }
    //</editor-fold>
    
}
