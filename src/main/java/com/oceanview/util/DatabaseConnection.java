package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/oceanview_resort";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private DatabaseConnection() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void testConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            System.out.println("Database connection successful!");
        } finally {
            closeConnection(conn);
        }
    }
}
