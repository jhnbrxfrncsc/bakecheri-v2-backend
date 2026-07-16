package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private static HikariDataSource dataSource;

    private DatabaseConfig() {}

    public static void initialize() {
        logger.info("DatabaseConfig.initialize() - START");
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Pool already running.");
            return;
        }

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(ApplicationProperties.get("db.url"));
        config.setUsername(ApplicationProperties.get("db.username"));
        config.setPassword(ApplicationProperties.get("db.password"));
        config.setMaximumPoolSize(ApplicationProperties.getInt("db.pool.max-size"));
        config.setMinimumIdle(ApplicationProperties.getInt("db.pool.min-idle"));

        config.setPoolName("BakeCheriPool");

        dataSource = new HikariDataSource(config);

        logger.info("Pool has initialized.");
        logPoolStatus();
    }

    public static Connection getConnection() throws SQLException {
        logger.info("DatabseConfig#getConnection() - START");
        if (dataSource == null || dataSource.isClosed()) {
            logger.info("DatabseConfig#getConnection() - dataSource is null/closed.");
            initialize();
        }
        logger.info("Connection borrowed.");
        logPoolStatus();

        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Pool has shut down.");
        }
    }

    public static void restart() {
        shutdown();
        initialize();
        logger.info("Pool has restarted.");
    }

    public static void logPoolStatus() {
        if (dataSource != null) {
            int active = dataSource.getHikariPoolMXBean().getActiveConnections();
            int idle = dataSource.getHikariPoolMXBean().getIdleConnections();
            int total = dataSource.getHikariPoolMXBean().getTotalConnections();
            int waiting = dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();

            logger.info(
                    "Pool Status -> Active: {}, Idle: {}, Total: {}, Waiting: {}",
                    active,
                    idle,
                    total,
                    waiting
            );
        }
    }

    private static Properties loadProperties(){
        Properties properties = new Properties();

        try (InputStream input = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found.");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }

        return properties;
    }

}
