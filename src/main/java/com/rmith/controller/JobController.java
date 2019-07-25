/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmith.controller;

import com.rmith.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Andrew
 */

@Controller
public class JobController {
    
    @Autowired
    @Lazy
    private JobService jobService;
    
    @GetMapping("/job")
    public String job(ModelMap modelMap){
        modelMap.addAttribute("jobDTOList", jobService.getListJob());
        return "/user/job";
        
    }
    
}
