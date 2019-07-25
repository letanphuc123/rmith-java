/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmith.dto;

import java.io.Serializable;

/**
 *
 * @author Andrew
 */
public class JobDTO implements Serializable{
    
    private String companyName;
    private String address;
    private String time;
    private int numberEmp;
    private String date;
    private String nationality;
    private String desription;

    public JobDTO() {
        this.companyName = "";
        this.address = "";
        this.time = "";
        this.numberEmp = 0;
        this.date = "";
        this.nationality = "";
        this.desription = "";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public int getNumberEmp() {
        return numberEmp;
    }

    public void setNumberEmp(int numberEmp) {
        this.numberEmp = numberEmp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }
    
    
    
    
    
    
    
}
