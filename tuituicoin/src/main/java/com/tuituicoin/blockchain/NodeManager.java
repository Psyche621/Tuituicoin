package com.tuituicoin.blockchain;

import java.sql.SQLException;

import com.tuituicoin.repository.DatabaseManager;

public class NodeManager {
    private static NodeManager instance;
    private boolean isRunning = false;
    private boolean isInit = false;

    private NodeManager() {}

    public static NodeManager getInstance() {
        if (instance == null) {
            instance = new NodeManager();
        }
        return instance;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isInit() {
        return isInit;
    }

    public void initialize() throws SQLException{
        if (isInit) {
            throw new IllegalStateException("Node is already initialized.");
        }

        DatabaseManager.initialize();
        Genesis.initialize();
        isInit = true;
    }
}
