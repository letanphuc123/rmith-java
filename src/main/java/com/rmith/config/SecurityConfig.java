package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Value("${custom.config.cors-domain-allowed}")
    private String domainAllowCORS;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("\n################## Init Spring Security Config ##################");
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**")
                .antMatchers("/static")
                .antMatchers("/error/**")
                .antMatchers("/error");
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* Create custom authentication provider */
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
        auth.authenticationProvider(provider);
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "https://" + domainAllowCORS,
                "http://" + domainAllowCORS));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
