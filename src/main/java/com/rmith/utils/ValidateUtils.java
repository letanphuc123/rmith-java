package com.rmith.utils;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.constant.Constant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.UrlValidator;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public class ValidateUtils {

    private static final UrlValidator URL_VALIDATOR = new UrlValidator(new String[]{"http", "https"});
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
    private static final DateValidator VALIDATOR = DateValidator.getInstance();
    private static final IntegerValidator NUMBER_VALIDATOR = IntegerValidator.getInstance();

    //<editor-fold defaultstate="collapsed" desc="IS VALID PASSWORD">
    private static boolean isValidPassword(String password) {
        return !(password.length() < 6 || !password.matches("^.*(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W_]).*$"));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IS VALID SESSION">
    public static boolean isSessionNull(HttpSession session) {
        return session.getAttribute("accountID") == null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IS VALID STRING">
    public static boolean isValidString(String input) {
        return input != null && !input.equals("");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IS NUMBER">
    public static Boolean isNumber(String input) {
        return NUMBER_VALIDATOR.isValid(input);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE EMAIL">
    public static String validateEmail(String email) {
        if (email == null || email.equals("")) {
            return "Email must be not null";
        }
        if (!EMAIL_VALIDATOR.isValid(email)) {
            return "Email is in wrong format";
        }
        if (email.length() > 150) {
            return "Email is too long";
        }
        return Constant.SUCCESS;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE PASSWORD">
    public static String validatePassword(String password) {
        if (password == null || password.equals("")) {
            return "Password must be not null";
        }
        if (!isValidPassword(password)) {
            return "Password is in wrong format";
        }
        return Constant.SUCCESS;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE SHORT STRING">
    public static String validateString(String variable, int maxLength) {
        if (variable == null || variable.equals("")) {
            return "Field must be not null";
        }
        if (variable.length() > maxLength && maxLength != 0) {
            return "Field is in wrong format";
        }
        return Constant.SUCCESS;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE STRING LENGTH">
    public static String validateStringLength(String variable, int maxLength) {
        if (maxLength == Integer.MAX_VALUE) {
            return Constant.SUCCESS;
        }
        if (variable.length() > maxLength && maxLength != 0) {
            return "This field is not more than " + maxLength + " characters";
        }
        return Constant.SUCCESS;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE DATE">
    public static String validateDate(String variable) {
        if (VALIDATOR.isValid(variable, "yyyy-MM-dd")) {
            return Constant.SUCCESS;
        }
        return "The date is formated yyyy-mm-dd";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE DATE TIME">
    public static String validateDateTime(String variable) {
        if (VALIDATOR.isValid(variable, "yyyy-MM-dd HH:mm:ss")) {
            return Constant.SUCCESS;
        }
        return "The date is formated yyyy-MM-dd HH:mm:ss";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE WEBSITE">
    public static String validateWebsite(String variable, int maxLength) {
        if (variable.length() > maxLength && maxLength != 0) {
            return "This field is not more than " + maxLength + " characters";
        }
        if (URL_VALIDATOR.isValid(variable)) {
            return Constant.SUCCESS;
        }
        return "This field must be a valid URL";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE PHONE NUMBER">
    public static boolean validatePhoneNumber(String phoneNo) {
        return true;//phoneNo.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE REGEX">
    public static boolean validateRegex(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE URL'S PATH">
    public static boolean isValidUrlPath(String path) {
        return path.matches("^([a-zA-Z0-9-]+)$");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDATE URL">
    public static boolean validateURL(String url) {
        if (url == null || url.length() == 0) {
            return true;
        }
        return URL_VALIDATOR.isValid(url);
    }
    //</editor-fold>
}
