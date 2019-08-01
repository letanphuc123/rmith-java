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
 * @author Le Tan Phuc
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${custom.config.cors-domain-allowed}")
    private String domainAllowCORS;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("\n################## Init Spring Security Config ##################");
        /* require specific role for request */
        http
                .authorizeRequests()
                /**
                 * Account function security
                 */
                .antMatchers(
                        "/accounts", //Open account screen
                        "/new-account", //Open new account screen
                        "/add-account", //Event add new account
                        "/edit-account", //Event edit account information
                        "/delete-account" //Event when delete account
                ).hasAnyRole("admin", "accounts")
                /**
                 * Group user function security
                 */
                .antMatchers(
                        "/group-user", //Open group user screen
                        "/add-group-user", //Event add new group user
                        "/update-group-user", //Event update group user information
                        "/delete-group-user", //Event delete group user
                        "/update-permission" //Event update permission when click to button Permission
                ).hasAnyRole("admin", "group-user")
                /**
                 * Module function security
                 */
                .antMatchers(
                        "/modules", //Open modules screen
                        "/add-new-module", //Event add new module
                        "/update-module", //Event update module information
                        "/delete-module" //Event delete module
                ).hasAnyRole("admin", "modules")
                /**
                 * Category function security
                 */
                .antMatchers(
                        "/categories", //Open category screen
                        "/add-new-category", //Event add new category
                        "/update-category", //Event update category information
                        "/delete-category" //Event delete category
                ).hasAnyRole("admin", "categories")
                /**
                 * All event security after login application
                 */
                .antMatchers(
                        "/profile",
                        "/update-profile",
                        "/update-password",
                        "/sign-out"
                ).hasAnyRole("admin", "logged")
                .and()
                .formLogin()
                .loginPage("/login");

        /* Ignore check login for paths */
        http.authorizeRequests().antMatchers("/",
                "/login",
                "/check-login",
                "/forgot-password",
                "/send-forgot-password").permitAll();

        /* all request need to be authenticate (login) execept listed requests above */
        http.authorizeRequests().anyRequest().authenticated();

        /* enable CORS */
        http.cors().and();
        /* Add custom access denied handler */
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedExceptionHandler());
        /* disable only allow https */
        http.headers().httpStrictTransportSecurity().disable();
        /* enable content security policy, only allow content from our domain or fonts.gstatic.com */
        http.headers().contentSecurityPolicy(
                "default-src 'self' *.google.com.vn *.google.co.jp *.google.com 'unsafe-inline'; "
                + "img-src 'self' *.googleusercontent.com *.google.com.vn *.google.co.jp *.google.com *.googleapis.com *.googleapis.com.vn *.googleapis.co.jp *.gstatic.com *.gstatic.com.vn *.gstatic.co.jp data:; "
                + "media-src localhost; "
                + "script-src 'self' 'unsafe-inline' *.googleapis.com *.googleapis.com.vn *.googleapis.co.jp *.google.com.vn *.google.co.jp *.google.com *.gstatic.com *.gstatic.com.vn *.gstatic.co.jp 'unsafe-eval'; "
                + "font-src 'self' fonts.gstatic.com; "
                + "style-src 'self' 'unsafe-inline'  fonts.googleapis.com");//Add csp
        System.out.println(
                "################## Init Spring Security Config Successfully ##################");
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

    @Bean
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
