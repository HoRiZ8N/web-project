package com.shop.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Прогреваем пул при старте приложения, а не при первом запросе пользователя
        try (var c = DatabaseConnection.getConnection()) {
            // соединение сразу возвращается в пул
        } catch (Exception e) {
            throw new RuntimeException("Не удалось инициализировать пул соединений к БД", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConnection.shutdown();
    }
}
