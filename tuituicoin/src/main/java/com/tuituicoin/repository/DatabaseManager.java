package com.tuituicoin.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public DatabaseManager() {}

    private static String getUrl() {
        return System.getProperty("tuituicoin.db.url", "jdbc:sqlite:blockchain.db");
    }

    public static Connection connect() throws SQLException {
        initialize();
        return DriverManager.getConnection(getUrl());
    }

    public static void initialize() throws SQLException {
        try (Connection conn = DriverManager.getConnection(getUrl())) {
            Statement stmt = conn.createStatement();
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS blocks (
                    hash TEXT PRIMARY KEY,
                    height INTEGER UNIQUE NOT NULL,
                    previous_hash TEXT,
                    timestamp TEXT,
                    nonce INTEGER
                )     
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    transaction_id TEXT PRIMARY KEY,
                    block_hash TEXT,
                    sender TEXT,
                    recipient TEXT,
                    amount LONG,
                    signature TEXT,
                    
                    FOREIGN KEY(block_hash)
                        references blocks(hash)
                )
            """);
        }
    }
}
