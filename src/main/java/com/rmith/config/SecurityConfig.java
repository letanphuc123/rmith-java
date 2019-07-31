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
                 * Package function security
                 */
                .antMatchers(
                        "/packages", //Open package screen
                        "/add-package", //Event add new package
                        "/update-package", //Event update package information
                        "/delete-package", //Event delete package
                        "/get-limit-package", //Event get list limit for each package when click to button Limit
                        "/update-limit-package" //Event update limit information for package
                ).hasAnyRole("admin", "packages")
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
                 * Monitor used function security
                 */
                .antMatchers(
                        "/monitor-used", //Open monitor used screen
                        "/get-list-monitor-used-by-account" //Get information monitor used
                ).hasAnyRole("admin", "monitor-used")
                /**
                 * Google place type function security
                 */
                .antMatchers(
                        "/google-place-type", //Open google place type screen
                        "/modify-google-place-type", //Update google place type information
                        "/delete-google-place-type" //Delete google place type
                ).hasAnyRole("admin", "google-place-type")
                /**
                 * Proxy function security
                 */
                .antMatchers(
                        "/proxy", //Open proxy screen
                        "/add-proxy", //Add new proxy
                        "/import-proxy" //Import proxy information from database
                ).hasAnyRole("admin", "proxy")
                /**
                 * Notification manager function security
                 */
                .antMatchers(
                        "/notification-manager", //Open notification manager screen
                        "/change-notification-active", //Event change status active notification
                        "/save-notification", //Event save notification
                        "/delete-notification", //Event delete notification
                        "/notification-making",
                        "/notification-review"
                ).hasAnyRole("admin", "notification-manager")
                /**
                 * Manage billing function security
                 */
                .antMatchers(
                        "/customer-billing", //Open customer billing screen
                        "/billing", //Open billing screen by payment id
                        "/modify-billing", //Event update billing information
                        "/delete-billing" //Event delete billing information,
                ).hasAnyRole("admin", "manage-billing")
                /**
                 * Manage plan function security
                 */
                .antMatchers(
                        "/plan", //Open plan screen
                        "/add-plan", //Open add plan screen
                        "/update-plan", //Open update plan screen
                        "/modify-plan", //Event add/update plan information
                        "/delete-plan" //Event delete plan
                ).hasAnyRole("admin", "plan")
                /**
                 * Payment function security
                 */
                .antMatchers(
                        "/payment", //Open payment screen
                        "/add-payment", //Open add payment screen
                        "/update-payment", //Open update payment screen by payment id
                        "/modify-payment", //Event update payment information
                        "/delete-payment" //Event delete payment information,
                ).hasAnyRole("admin", "payment")
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
                 * Language function security
                 */
                .antMatchers(
                        "/languages", //Open language screen
                        "/load-language-function", //Event load language data
                        "/update-language-function" //Event update language information
                ).hasAnyRole("admin", "languages")
                /**
                 * Monitor API function security
                 */
                .antMatchers(
                        "/monitor-api", //Open monitor screen
                        "/column-chart-monitor-api", //Event load data for chart
                        "/detail-monitor-api" //Event load data monitor for table
                ).hasAnyRole("admin", "monitor-api")
                /**
                 * Log function security
                 */
                .antMatchers(
                        "/logs", //Open log screen
                        "/detail-file-log", //Event load log detail
                        "/set-cookie-filter-log", //Event set cookie when filter
                        "/download-file-log" //Event download file log
                ).hasAnyRole("admin", "logs")
                /**
                 * Agency approve function security
                 */
                .antMatchers(
                        "/agency-approve", //Open agency approve
                        "/approve-agency", //Event approve for agency request
                        "/deny-approve-agency", //Event deny for agency request
                        "/cancel-approve-agency", //Event cancel for agency request
                        "/agency-detail" //Event view agency detail information
                ).hasAnyRole("admin", "agency-approve")
                /**
                 * Set cookie date event security
                 */
                .antMatchers(
                        "/set-cookie-date"
                ).hasAnyRole("admin", "monitor-api", "monitor-used")
                /**
                 * All event security after login application
                 */
                .antMatchers(
                        "/notifications",
                        "/update-seen-notification",
                        "/payment",
                        "/update-language-header",
                        "/billing",
                        "/profile",
                        "/update-profile",
                        "/update-password",
                        "/contact-us",
                        "/privacy-policy",
                        "/term-of-service",
                        "/release-version",
                        "/sign-out"
                ).hasAnyRole("admin", "logged")
                .antMatchers("/citations").hasAnyRole("admin", "citations")
                .and()
                .formLogin()
                .loginPage("/login");

        /* Ignore check login for paths */
        http.authorizeRequests().antMatchers("/",
                "/login",
                "/check-login",
                "/forgot-password",
                "/send-forgot-password",
                "/resend-active-email", //Event when click to button to resend email active 
                "/active-account",
                "/active-add-new-account",
                "/active-confirm-add-new-email",
                "/active-new-email",
                "/active-confirm-new-email",
                "/active-or-account",
                "/active-organization-account",
                "/reset-password",
                "/update-resetting-password").permitAll();

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
