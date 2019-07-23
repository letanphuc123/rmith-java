package com.rmith.controller;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
@Controller
public class UserController {
    
    //<editor-fold defaultstate="collapsed" desc="DASHBOARD">
    @GetMapping("/dashboard")
    public String dashboard() {
        return "/user/dashboard";
    }
    //</editor-fold>
    
}
