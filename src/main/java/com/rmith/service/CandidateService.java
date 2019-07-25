/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmith.service;

import com.rmith.dto.CandidateDTO;
import java.util.List;


/**
 *
 * @author chauphuoctuong
 */
public interface CandidateService {
    
    List<CandidateDTO> getListCandidate();
    
}
