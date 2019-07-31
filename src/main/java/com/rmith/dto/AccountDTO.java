package com.rmith.dto;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import com.rmith.utils.ValidateUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
//</editor-fold>

/**
 *
 * @author Nguyen Duc Thien
 * @email nguyenducthien@fabercompany.co.jp
 */
public class AccountDTO implements Serializable {

    private int accountId;
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private String emailWaitActive;
    private String mobile;
    private String accountLanguage;
    private String secretKey;
    private String bcryptSecretKey;
    private String dateStart;
    private String dateEnd;
    private String memo;
    private int verifierEmail;
    private int accountStatus;
    private int isAdmin;
    private int groupId;
    private int packageId;
    private String groupName;
    private String packageName;
    private int createdBy;
    private String createdDate;
    private int updatedBy;
    private String updatedDate;
    private String newSecretKey;
    private String address;
    private String city;
    private String country;
    private String website;
    private String avatar;
    private int isPackageBuy;
    private int trialDayLeft;
    private int isChangeDateEnd;
    private int typeAccount;
    private String logo;
    private boolean isSetPassword;

    private Map<String, String> errorMap = new HashMap<>();

    public AccountDTO() {
        this.accountId = 0;
        this.firstName = "";
        this.lastName = "";
        this.companyName = "";
        this.email = "";
        this.emailWaitActive = "";
        this.mobile = "";
        this.accountLanguage = "";
        this.secretKey = "";
        this.bcryptSecretKey = "";
        this.dateStart = "";
        this.dateEnd = "";
        this.memo = "";
        this.verifierEmail = 0;
        this.accountStatus = 0;
        this.isAdmin = 0;
        this.groupId = 0;
        this.packageId = 0;
        this.groupName = "";
        this.packageName = "";
        this.createdBy = 0;
        this.createdDate = "";
        this.updatedBy = 0;
        this.updatedDate = "";
        this.newSecretKey = "";
        this.address = "";
        this.city = "";
        this.country = "";
        this.website = "";
        this.avatar = "";
        this.isPackageBuy = 0;
        this.trialDayLeft = 0;
        this.isChangeDateEnd = 0;
        this.typeAccount = 0;
        this.logo = "";
        this.isSetPassword = false;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
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
        String validate = ValidateUtils.validateString(firstName, 50);
        if (validate.equals(Constant.SUCCESS)) {
            this.firstName = firstName;
        } else {
            errorMap.put("firstName", validate);
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        String validate = ValidateUtils.validateString(lastName, 50);
        if (validate.equals(Constant.SUCCESS)) {
            this.lastName = lastName;
        } else {
            errorMap.put("lastName", validate);
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (companyName == null) {
            return;
        }
        String validate = ValidateUtils.validateStringLength(companyName, 500);
        if (validate.equals(Constant.SUCCESS)) {
            this.companyName = companyName;
        } else {
            errorMap.put("companyName", validate);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String validate = ValidateUtils.validateEmail(email);
        if (!validate.equals(Constant.SUCCESS)) {
            errorMap.put("email", validate);
        } else {
            this.email = email;
        }
    }
    
    public String getEmailWaitActive() {
        return emailWaitActive;
    }

    public void setEmailWaitActive(String emailWaitActive) {
        this.emailWaitActive = emailWaitActive;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        // only validate if user input not null data
        if (mobile == null) {
            return;
        }
        String validate = ValidateUtils.validateStringLength(mobile, 50);
        if (validate.equals(Constant.SUCCESS)) {
            this.mobile = mobile;
        } else {
            errorMap.put("mobile", validate);
        }
    }

    public String getAccountLanguage() {
        return accountLanguage;
    }

    public void setAccountLanguage(String accountLanguage) {
        // only validate if user input not null data
        if (accountLanguage == null) {
            return;
        }
        String validate = ValidateUtils.validateStringLength(accountLanguage, 2);
        if (validate.equals(Constant.SUCCESS)) {
            this.accountLanguage = accountLanguage;
        } else {
            errorMap.put("accountLanguage", validate);
        }
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        if(secretKey == "unknown"){
            this.secretKey = secretKey;
        } else {
            String validate = ValidateUtils.validatePassword(secretKey);
            if (!validate.equals(Constant.SUCCESS)) {
                errorMap.put("secretKey", validate);
            } else {
                this.secretKey = secretKey;
            }
        }
    }

    public String getBcryptSecretKey() {
        return bcryptSecretKey;
    }

    public void setBcryptSecretKey(String bcryptSecretKey) {
        if (bcryptSecretKey != null) {
            this.bcryptSecretKey = bcryptSecretKey;
        }
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        if (dateStart != null) {
            this.dateStart = dateStart;
        }
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        if (dateEnd != null) {
            this.dateEnd = dateEnd;
        }
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        if (memo != null) {
            this.memo = memo;
        }
    }

    public int getVerifierEmail() {
        return verifierEmail;
    }

    public void setVerifierEmail(int verifierEmail) {
        this.verifierEmail = verifierEmail;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        if (groupName != null) {
            this.groupName = groupName;
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        if (packageName != null) {
            this.packageName = packageName;
        }
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
        if (createdDate != null) {
            this.createdDate = createdDate;
        }
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

    public String getNewSecretKey() {
        return newSecretKey;
    }

    public void setNewSecretKey(String newSecretKey) {
        String validate = ValidateUtils.validatePassword(newSecretKey);
        if (validate.equals(Constant.SUCCESS)) {
            this.newSecretKey = newSecretKey;
        } else {
            errorMap.put("newSecretKey", validate);
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        // only validate if user input not null data
        if (address == null) {
            return;
        }
        String validate = ValidateUtils.validateStringLength(address, 500);
        if (validate.equals(Constant.SUCCESS)) {
            this.address = address;
        } else {
            errorMap.put("address", validate);
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        // only validate if user input not null data
        if (city == null) {
            return;
        }
        String validate = ValidateUtils.validateStringLength(city, 100);
        if (validate.equals(Constant.SUCCESS)) {
            this.city = city;
        } else {
            errorMap.put("city", validate);
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        // only validate if user input not null data
        if (country == null) {
            return;
        }
        String validate = ValidateUtils.validateStringLength(country, 100);
        if (validate.equals(Constant.SUCCESS)) {
            this.country = country;
        } else {
            errorMap.put("country", validate);
        }
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        // only validate if user input not null data
        if (website == null) {
            return;
        }
        String validate = ValidateUtils.validateWebsite(website, 500);
        if (validate.equals(Constant.SUCCESS) || website.equals("")) {
            this.website = website;
        } else {
            errorMap.put("website", validate);
        }
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        if (avatar != null) {
            this.avatar = avatar;
        }
    }

    public int getIsPackageBuy() {
        return isPackageBuy;
    }

    public void setIsPackageBuy(int isPackageBuy) {
        this.isPackageBuy = isPackageBuy;
    }

    public int getTrialDayLeft() {
        return trialDayLeft;
    }

    public void setTrialDayLeft(int trialDayLeft) {
        this.trialDayLeft = trialDayLeft;
    }

    public int getIsChangeDateEnd() {
        return isChangeDateEnd;
    }

    public void setIsChangeDateEnd(int isChangeDateEnd) {
        this.isChangeDateEnd = isChangeDateEnd;
    }

    public int getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(int typeAccount) {
        this.typeAccount = typeAccount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        if (logo != null) {
            this.logo = logo;
        }
    }
    
    public boolean isSetPassword() {
        return isSetPassword;
    }

    public void setIsSetPassword(boolean isSetPassword) {
        this.isSetPassword = isSetPassword;
    }

}
