package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
@Configuration
public class MysqlConfig implements DisposableBean {
    
    @Value("${custom.config.mysql.host}")
    private String hostname;
    
    @Value("${custom.config.mysql.database}")
    private String database;
    
    @Value("${custom.config.mysql.username}")
    private String username;
    
    @Value("${custom.config.mysql.password}")
    private String password;
    
    @Value("${custom.config.mysql.maxtotal}")
    private int maxtotal;
    
    @Value("${custom.config.mysql.minidle}")
    private int minidle;
    
    @Value("${custom.config.mysql.maxidle}")
    private int maxidle;

    private BasicDataSource ds = null;
    
    //<editor-fold defaultstate="collapsed" desc="INIT BASIC MYSQL SERVER">
    @Bean
    public BasicDataSource getBasicDataSource() {
        System.out.println("\n################## Init MySQL Data Source Config ##################");
        if (ds != null) {
            System.out.println(
                    "\n################## Init MySQL Data Source Config Successfully ##################");
            return ds;
        }
        ds = new BasicDataSource();
        ds.setUrl(new StringBuilder().append("jdbc:mysql://").append(hostname).append(":3306/")
                .append(database).append("?autoReconnect=true&useSSL=false")
                .toString());
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaxTotal(maxtotal);
        ds.setMinIdle(minidle);
        ds.setMaxIdle(maxidle);
        /* Maximum time of connection life */
        ds.setMaxConnLifetimeMillis(30000);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        /* Number of thread in connection pool when init */
        ds.setInitialSize(50);
        /* Remove all abandoned connection */
        ds.setRemoveAbandonedOnBorrow(true);
        ds.setAbandonedUsageTracking(false);
        ds.setLogExpiredConnections(false);
        /* Set Last In First Out in connection queue */
        ds.setLifo(false);
        ds.addConnectionProperty("useUnicode", "true");
        ds.addConnectionProperty("characterEncoding", "UTF-8");
        ds.setRemoveAbandonedOnMaintenance(true);
        System.out.println(
                "################## Init MySQL Data Source Config Successfully ##################");
        return ds;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="DESTROY BEAN INIT BASIC MYSQL SERVER">
    @Override
    public void destroy() throws SQLException {
        if (ds != null) {
            ds.close();
            ds = null;
        }
    }
    //</editor-fold>
    
}
