package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
public class ThymeleafDispatcherServlet extends DispatcherServlet {
    
    private final Logger errorThymeleafLogger = LogManager.getLogger("error_thymeleaf");
    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            super.render(mv, request, response);
        } catch (Exception ex) {
            errorThymeleafLogger.error(ExceptionUtils.getStackTrace(ex));
            throw new Exception();
        }

    }
    
}
