package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    UserDetailsService userDetailService;

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        return a;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
