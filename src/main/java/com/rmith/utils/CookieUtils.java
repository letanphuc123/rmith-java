package com.rmith.utils;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import javax.servlet.http.Cookie;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public class CookieUtils {

    private static final Logger LOGGER = LogManager.getLogger("error_format");

    //<editor-fold defaultstate="collapsed" desc="CREATE COOKIE">
    public static Cookie createCookie(String cookieName, String cookieValue) {
        Cookie userCookie = null;
        try {
            userCookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
            Calendar cal = Calendar.getInstance();
            userCookie.setMaxAge((24 - cal.get(Calendar.HOUR_OF_DAY)) * 60 * 60); //Hour expired in day
            userCookie.setPath("/");
            return userCookie;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
            return userCookie;
        }
    }

    public static Cookie createCookie(String cookieName, String cookieValue, int expHours) {
        Cookie userCookie = null;
        try {
            userCookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
            userCookie.setMaxAge(expHours * 60 * 60); //Hour expired in day
            userCookie.setPath("/");
            return userCookie;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
            return userCookie;
        }
    }
    //</editor-fold>

}
