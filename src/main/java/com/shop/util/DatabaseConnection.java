package com.shop.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final HikariDataSource DATA_SOURCE;

    static {
        Properties p = new Properties();
        try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) throw new RuntimeException("db.properties not found");
            p.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Database configuration error", e);
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

        DATA_SOURCE = new HikariDataSource(config);
    }

    private DatabaseConnection() {
    }

    /**
     * Берёт соединение из пула. Как и раньше, используется в DAO через
     * try-with-resources — close() не закрывает физическое соединение,
     * а возвращает его в пул.
     */
    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    /**
     * Вызывается при остановке приложения (см. AppLifecycleListener),
     * чтобы корректно закрыть пул и все соединения в нём.
     */
    public static void shutdown() {
        if (DATA_SOURCE != null && !DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
        }
    }
}
