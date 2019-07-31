package com.rmith.utils;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.security.SecureRandom;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Component
@Lazy
public class GeneratedIDUtils {

    //<editor-fold defaultstate="collapsed" desc="GENERATE UUID">
    public String generatedUUID() {
        try {
            UUID uuid = UUID.randomUUID();
            return uuid.toString().replaceAll("-", "").toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GENERATE ID">
    public Integer generatedID() {
        try {
            SecureRandom rand = new SecureRandom();
            int randomNum = rand.nextInt((999999999 - 111111111) + 1) + 111111111;
            return randomNum;
        } catch (Exception e) {
            return 0;
        }
    }
    //</editor-fold>
}
