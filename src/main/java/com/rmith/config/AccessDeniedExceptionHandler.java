package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 */
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendRedirect("403");
    }
}
