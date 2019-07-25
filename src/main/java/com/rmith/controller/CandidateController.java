/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmith.controller;

import com.rmith.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;



/**
 *
 * @author chauphuoctuong
 */
@Controller
public class CandidateController {
    
    // <editor-fold defaultstate="collapsed" desc="INIT">
    @Autowired
    @Lazy
    private CandidateService candidateService;
    
    
    @GetMapping("/dashboard")
    public String dashboard(ModelMap modelMap) {
        modelMap.addAttribute("candidateDTOList", candidateService.getListCandidate());
        return "/user/dashboard";
    
    }
}
