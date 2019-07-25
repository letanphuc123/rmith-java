/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmith.dto;


//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.io.Serializable;
//</editor-fold>

/**
 *
 * @author chauphuoctuong
 */




public class CandidateDTO implements Serializable {
    
    private int accountId;
    private String firstName;
    private String lastName;
    private String birthDay;
    private String email;
    private String major;
    private String address;

    public CandidateDTO(int accountId, String firstName, String lastName, String birthDay, String email, String major) {
        this.accountId = 0;
        this.firstName = "";
        this.lastName = "";
        this.birthDay = "";
        this.email = "";
        this.major = "";
        this.address = "";
    }

    public CandidateDTO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
    



}
