package com.solvd.hospital.common.database;

import java.sql.Connection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final int INITIAL_POOL_SIZE = 10;

    private final BlockingQueue<Connection> connections;

    private ConnectionPool() {
        this.connections = new ArrayBlockingQueue<>(INITIAL_POOL_SIZE);
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            Connection connection = ConnectionManager.getConnection();
            connections.offer(connection);
        }
    }

    public ReusableConnection getConnection() {
        try {
            Connection connection = connections.take();
            return new ReusableConnection(connection, this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error getting a connection from the pool.", e);
        }
    }

    public void releaseConnection(Connection connection) {
        connections.offer(connection);
    }

    private static final class InstanceHolder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
