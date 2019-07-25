/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmith.service;

import com.rmith.dto.JobDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *
 * @author Andrew
 */

@Service
@Lazy
public class JobServiceImpl implements JobService {

    @Override
    public List<JobDTO> getListJob() {
        List<JobDTO> list = new ArrayList<>();
        JobDTO dto1 = new JobDTO();
        dto1.setAddress("Ba Dinh, Ha Noi");
        dto1.setCompanyName("Hello");
        dto1.setDate("20/06/2019");
        dto1.setDesription("Established in April 2007, Toshiba Software Development (Vietnam) Co., Ltd is a software company with 100% capital invested from Toshiba Corporation (Japan). As one of oversea R& D centers of Toshiba Corporate in software development field, we are developing software for variety of Toshiba products & solutions. Besides, TSDV have been also working on R&D activities of cutting-edge fundamental software technogies. We aims to become a leading company in software development field.");
        dto1.setNationality("Japan");
        dto1.setNumberEmp(30);
        dto1.setTime("Monday - Friday");
        
        JobDTO dto2 = new JobDTO();
        dto2.setAddress("Ba Dinh, Ha Noi");
        dto2.setCompanyName("Hello");
        dto2.setDate("20/06/2019");
        dto2.setDesription("Established in April 2007, Toshiba Software Development (Vietnam) Co., Ltd is a software company with 100% capital invested from Toshiba Corporation (Japan). As one of oversea R& D centers of Toshiba Corporate in software development field, we are developing software for variety of Toshiba products & solutions. Besides, TSDV have been also working on R&D activities of cutting-edge fundamental software technogies. We aims to become a leading company in software development field.");
        dto2.setNationality("Japan");
        dto2.setNumberEmp(30);
        dto2.setTime("Monday - Friday");
        list.add(dto1);
        list.add(dto2);
        return list;
        
    }
    
}
