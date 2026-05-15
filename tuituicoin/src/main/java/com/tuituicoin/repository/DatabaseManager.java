package com.tuituicoin.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:blockchain.db";

    public DatabaseManager() {
        LOGGER.info("Initializing database manager.");

        try {
            initialize();
            LOGGER.info("Database manager initialized successfully.");
        } catch (SQLException e) {
            LOGGER.severe("Failed to initialize database manager: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database manager", e);
        }
    }

    private static String getUrl() {
        return System.getProperty("tuituicoin.db.url", DEFAULT_DB_URL);
    }

    public static Connection connect() throws SQLException {
        LOGGER.info("Connecting to database at URL: " + getUrl());
        return DriverManager.getConnection(getUrl());
    }

    public static void initialize() throws SQLException {
        LOGGER.info("Initializing database schema.");

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

            LOGGER.info("Database schema initialized successfully.");
        } catch (SQLException e) {
            LOGGER.severe("Failed to initialize database schema: " + e.getMessage());
            throw e;
        }
    }
}
