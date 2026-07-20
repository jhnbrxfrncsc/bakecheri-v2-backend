package config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationListener implements ServletContextListener {

    private static final Logger logger =
            LoggerFactory.getLogger(ApplicationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.info("========== APPLICATION START ==========");

        DatabaseConfig.initialize();

        logger.info("Database initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        logger.info("========== APPLICATION STOP ==========");

        DatabaseConfig.shutdown();

        logger.info("Database closed.");
    }
}
