package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 */
@Configuration
public class AsyncThreadConfig implements DisposableBean {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    public static volatile ExecutorService executorPool = null;
    private static final Logger LOGGER = LogManager.getLogger("error_config"); 
    //</editor-fold>

    /**
     *
     * Init a executor pool for all completable async task
     *
     * @return executorPool if exist or create new if not exist
     */
    @Bean
    public Executor initExecutorPool() {
        System.out.println("\n################## Init Async Executor Pool ##################");
        if (executorPool == null) {
            ThreadFactory threadFactory = (Runnable r) -> {
                Thread thread = new Thread(r, "Async Pool");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    t.interrupt();
                    LOGGER.error(ExceptionUtils.getStackTrace(e));
                });
                return thread;
            };
            executorPool = Executors.newFixedThreadPool(10, threadFactory);
        }
        System.out.println("################## Init Async Executor Pool Successfully ##################");
        return executorPool;
    }

    /**
     *
     * Destroy bean when close or redeploy application
     *
     */
    @Override
    public void destroy() {
        if (executorPool != null) {
            executorPool.shutdown();
            executorPool = null;
        }

    }
}
