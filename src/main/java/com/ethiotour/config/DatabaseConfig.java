package com.ethiotour.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "database.properties";

    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        // Try multiple ways to find the config file
        String[] paths = { CONFIG_FILE, "resources/" + CONFIG_FILE, "/" + CONFIG_FILE };
        InputStream input = null;

        for (String path : paths) {
            input = DatabaseConfig.class.getClassLoader().getResourceAsStream(path);
            if (input != null) {
                System.out.println("[OK] Found configuration at: " + path);
                break;
            }
        }

        if (input == null) {
            System.err.println("[ERROR] Configuration file not found: " + CONFIG_FILE);
            System.err.println("  Searched in classpath: " + System.getProperty("java.class.path"));
            setDefaults();
            return;
        }

        try (InputStream in = input) {
            properties.load(in);
            System.out.println("[OK] Database configuration loaded successfully from " + CONFIG_FILE);
        } catch (IOException ex) {
            System.err.println("[ERROR] Failed to load database configuration: " + ex.getMessage());
            setDefaults();
        }
    }

    private static void setDefaults() {
        properties.setProperty("db.mode", "IN_MEMORY");
        properties.setProperty("db.mssql.server", "localhost");
        properties.setProperty("db.mssql.port", "1433");
        properties.setProperty("db.mssql.database", "EthioTourDB");
        properties.setProperty("db.mssql.username", "sa");
        properties.setProperty("db.mssql.password", "");
        properties.setProperty("db.pool.initialSize", "5");
        properties.setProperty("db.pool.maxSize", "20");
    }

    public static String getProperty(String key) {
        return getProperty(key, "");
    }

    public static String getProperty(String key, String defaultValue) {
        // Try to read from environment variables first (e.g. DB_MSSQL_SERVER)
        String envKey = key.toUpperCase().replace('.', '_');
        String envProp = System.getenv(envKey);
        if (envProp != null && !envProp.trim().isEmpty()) {
            return envProp;
        }
        return properties.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    // MSSQL Configuration Getters
    public static String getMSSQLServer() {
        return getProperty("db.mssql.server", "localhost");
    }

    public static int getMSSQLPort() {
        return getIntProperty("db.mssql.port", 1433);
    }

    public static String getMSSQLDatabase() {
        return getProperty("db.mssql.database", "EthioTourDB");
    }

    public static String getMSSQLUsername() {
        return getProperty("db.mssql.username", "sa");
    }

    public static String getMSSQLPassword() {
        return getProperty("db.mssql.password", "");
    }

    public static boolean getMSSQLEncrypt() {
        return getBooleanProperty("db.mssql.encrypt", false);
    }

    public static boolean getMSSQLTrustServerCertificate() {
        return getBooleanProperty("db.mssql.trustServerCertificate", true);
    }

    public static int getMSSQLLoginTimeout() {
        return getIntProperty("db.mssql.loginTimeout", 30);
    }

    // Connection Pool Configuration Getters
    public static int getPoolInitialSize() {
        return getIntProperty("db.pool.initialSize", 5);
    }

    public static int getPoolMaxSize() {
        return getIntProperty("db.pool.maxSize", 20);
    }

    public static int getPoolConnectionTimeout() {
        return getIntProperty("db.pool.connectionTimeout", 30000);
    }

    public static int getPoolIdleTimeout() {
        return getIntProperty("db.pool.idleTimeout", 600000);
    }

    public static int getPoolMaxLifetime() {
        return getIntProperty("db.pool.maxLifetime", 1800000);
    }

    // API Configuration Getters
    public static String getAPIBaseUrl() {
        return getProperty("api.base.url", "http://localhost:8080/api");
    }

    public static String getAPIKey() {
        return getProperty("api.key", "");
    }

    // SQLite Configuration Getters
    public static String getSQLitePath() {
        return getProperty("db.sqlite.path", "ethiotour.db");
    }

    // Database Mode
    public static DatabaseMode getDatabaseMode() {
        String mode = getProperty("db.mode", "IN_MEMORY");
        try {
            return DatabaseMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid database mode: " + mode + ". Using IN_MEMORY");
            return DatabaseMode.IN_MEMORY;
        }
    }

    public enum DatabaseMode {
        IN_MEMORY("In-Memory Simulation"),
        MSSQL("MS SQL Server"),
        SQLITE("SQLite (Local File)");

        private final String displayName;

        DatabaseMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Print configuration for debugging
    public static void printConfiguration() {
        System.out.println("\n=== EthioTour Database Configuration ===");
        System.out.println("Mode: " + getDatabaseMode().getDisplayName());
        if (getDatabaseMode() == DatabaseMode.MSSQL) {
            System.out.println("Server: " + getMSSQLServer() + ":" + getMSSQLPort());
            System.out.println("Database: " + getMSSQLDatabase());
            System.out.println("Connection Pool Max Size: " + getPoolMaxSize());
        }
        System.out.println("API Base URL: " + getAPIBaseUrl());
        System.out.println("=======================================\n");
    }
}
