package com.rmith.dto;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.io.Serializable;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
public class AccountDTO implements Serializable {

    private int accountId;
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private String memo;
    private int isAdmin;
    private int createdBy;
    private String createdDate;
    private int updatedBy;
    private String updatedDate;
    private String address;
    private String city;
    private String country;
    private String avatar;

    public AccountDTO() {
        this.accountId = 0;
        this.firstName = "";
        this.lastName = "";
        this.companyName = "";
        this.email = "";
        this.memo = "";
        this.isAdmin = 0;
        this.createdBy = 0;
        this.createdDate = "";
        this.updatedBy = 0;
        this.updatedDate = "";
        this.address = "";
        this.city = "";
        this.country = "";
        this.avatar = "";
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    

}
