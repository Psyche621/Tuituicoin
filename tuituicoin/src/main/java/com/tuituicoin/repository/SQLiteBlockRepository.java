package com.tuituicoin.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.tuituicoin.blockchain.Block;
import com.tuituicoin.blockchain.Transaction;

public class SQLiteBlockRepository implements BlockRepository {
    private SQLiteTransactionRepository transactionRepository = new SQLiteTransactionRepository();

    @Override
    public void save(Block block) {
        String sql = """
            INSERT INTO blocks (
                hash,
                height,
                previous_hash,
                timestamp,
                nonce
            )     
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, block.getHash());
            stmt.setInt(2, block.getHeight());
            stmt.setString(3, block.getPrevHash());
            stmt.setString(4, block.getTimestamp().toString());
            stmt.setInt(5, block.getNonce());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Saving block failed");
            e.printStackTrace();
        }
    }

    @Override
    public Block findByHash(String hash) {
        String sql = """
            SELECT * FROM blocks WHERE hash = ?
        """;

        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, hash);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String blockHash = rs.getString("hash");
                int height = rs.getInt("height");
                String prevHash = rs.getString("previous_hash");
                List<Transaction> transactions = transactionRepository.findByBlockHash(blockHash);
                Instant timestamp = Instant.parse(rs.getString("timestamp"));
                int nonce = rs.getInt("nonce");

                return new Block(blockHash, height, prevHash, transactions, timestamp, nonce);
            }
        } catch (SQLException e) {
            System.err.println("Database query failed");
            e.printStackTrace();
            return null;
        }
    }

    /* Find the latest block in teh database for 
     * assigning height */
    @Override
    public Block findLatest() {
        String sql = """
            SELECT * FROM blocks ORDER BY height DESC LIMIT 1 
        """;

        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            SQLiteTransactionRepository transactionRepository = new SQLiteTransactionRepository();

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String blockHash = rs.getString("hash");
                int height = rs.getInt("height");
                String prevHash = rs.getString("previous_hash");
                List<Transaction> transactions = transactionRepository.findByBlockHash(blockHash);
                Instant timestamp = Instant.parse(rs.getString("timestamp"));
                int nonce = rs.getInt("nonce");

                return new Block(blockHash, height, prevHash, transactions, timestamp, nonce);
            }
        } catch (SQLException e) {
            System.err.println("Database query failed");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Block> findAll() {
        String sql = """
            SELECT * FROM blocks
        """;
        
        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Block> blockList = new ArrayList<>();

                while (rs.next()) {
                    String blockHash = rs.getString("hash");
                    int height = rs.getInt("height");
                    String prevHash = rs.getString("previous_hash");
                    List<Transaction> transactions = transactionRepository.findByBlockHash(blockHash);
                    Instant timestamp = Instant.parse(rs.getString("timestamp"));
                    int nonce = rs.getInt("nonce");
                    
                    Block block = new Block(blockHash, height, prevHash, transactions, timestamp, nonce);
                    blockList.add(block);
                }

                return blockList;
            }
        } catch (SQLException e) {
            System.err.println("Database query failed");
            e.printStackTrace();
            return null;
        }
    }
}
