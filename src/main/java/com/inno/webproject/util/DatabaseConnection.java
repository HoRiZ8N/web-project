package com.inno.webproject.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static volatile HikariDataSource dataSource;

    private DatabaseConnection() {
    }

    private static synchronized HikariDataSource getDataSource() throws DatabasePoolException {
        if (dataSource != null) {
            return dataSource;
        }

        Properties p = new Properties();
        try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new DatabasePoolException("db.properties not found");
            }
            p.load(in);
        } catch (IOException e) {
            throw new DatabasePoolException("Database configuration error", e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(p.getProperty("db.url"));
        config.setUsername(p.getProperty("db.user"));
        config.setPassword(p.getProperty("db.password"));
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(Integer.parseInt(p.getProperty("db.pool.maxSize", "10")));
        config.setMinimumIdle(Integer.parseInt(p.getProperty("db.pool.minIdle", "2")));
        config.setConnectionTimeout(Long.parseLong(p.getProperty("db.pool.connectionTimeoutMs", "30000")));
        config.setIdleTimeout(Long.parseLong(p.getProperty("db.pool.idleTimeoutMs", "600000")));
        config.setMaxLifetime(Long.parseLong(p.getProperty("db.pool.maxLifetimeMs", "1800000")));
        config.setPoolName("shop-db-pool");

        try {
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new DatabasePoolException("Failed to initialize the database connection pool", e);
        }

        return dataSource;
    }

    public static Connection getConnection() throws SQLException, DatabasePoolException {
        return getDataSource().getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
