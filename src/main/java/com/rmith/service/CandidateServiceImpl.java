/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmith.service;

import com.rmith.dto.CandidateDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *
 * @author chauphuoctuong
 */
@Service
@Lazy
public class CandidateServiceImpl implements CandidateService{
    
    @Override
    public List<CandidateDTO> getListCandidate() {
        List<CandidateDTO> list = new ArrayList<>();
        CandidateDTO dto3 = new CandidateDTO();
        dto3.setAccountId(1);
        dto3.setFirstName("Chau");
        dto3.setLastName("Phuoc Tuong");
        dto3.setMajor("Software Engineer");
        dto3.setBirthDay("09/12/1997");
        dto3.setEmail("tuongmario1997@gmail.com");
        
        
        CandidateDTO dto4 = new CandidateDTO();
        dto4.setAccountId(2);
        dto4.setFirstName("Doan");
        dto4.setLastName("Khoa");
        dto4.setEmail("khoadoan@gmail.com");
        dto3.setMajor("Software Engineer");
        dto3.setBirthDay("09/12/1995");
        list.add(dto4);
        return list;
        
        
     
    
    }

  
}
