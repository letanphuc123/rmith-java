package com.rmith.main;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.security.GeneralSecurityException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
@SpringBootApplication              //define spring boot main class
@ComponentScan("com.rmith.*")       //allow spring to scan and init all class with annotation
@EnableCaching 
public class Main {
    
    public static void main(String[] args) throws GeneralSecurityException, Exception {
        SpringApplication.run(Main.class, args);
    }
    
}
