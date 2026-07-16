package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    private static final Properties PROPERTIES = loadProperties();

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static Properties loadProperties(){
        Properties properties = new Properties();

        try (InputStream input = ApplicationProperties.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new IllegalStateException("Could not load application.properties from the classpath.");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load application.properties", e);
        }

        return properties;
    }
}
