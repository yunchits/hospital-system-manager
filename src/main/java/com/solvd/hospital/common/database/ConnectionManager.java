package com.solvd.hospital.common.database;

import com.solvd.hospital.common.AppProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL = AppProperties.getProperty("jdbc.url");
    private static final String USERNAME = AppProperties.getProperty("jdbc.username");
    private static final String PASSWORD = AppProperties.getProperty("jdbc.password");

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to connect to the database.", e);
        }
    }
}
