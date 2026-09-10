package com.inno.webproject.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (var c = DatabaseConnection.getConnection()) {
        } catch (Exception e) {
            throw new IllegalStateException("Failed to warm up the database connection pool", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConnection.shutdown();
    }
}
