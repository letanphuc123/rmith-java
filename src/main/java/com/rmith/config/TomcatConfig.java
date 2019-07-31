package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Configuration
public class TomcatConfig {

    @Value("${custom.config.tomcat.context-path}")
    private String contextPath;
    @Value("${custom.config.tomcat.port}")
    private int port;

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        System.out.println("\n################## Init Tomcat Config ##################");

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        /* Use Http11Nio2Protocol instead of Http11NioProtocol for better performance */
        factory.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");
        factory.setContextPath(contextPath);
        factory.setPort(port);
        System.out.println("################## Init Tomcat Config Successfully ##################");
        return factory;
    }
}
