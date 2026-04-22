package com.example.fca.config;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class Datasource {
    private final Dotenv dotenv = Dotenv.load();

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                dotenv.get("DB_URL"),
                dotenv.get("DB_USERNAME"),
                dotenv.get("DB_PASSWORD")
        );
    }
}
