package com.tuituicoin.repository;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.tuituicoin.blockchain.Transaction;
import com.tuituicoin.util.PublicKeyStringDecoder;

public class SQLiteTransactionRepository implements TransactionRepository {
    @Override
    public void save(Transaction transaction, String blockHash) {
        String sql = """
            INSERT INTO transactions (
                transaction_id,
                block_hash,
                sender,
                recipient,
                amount,
                signature
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getBlockHash());
            stmt.setString(3, Base64.getEncoder().encodeToString(transaction.getSender().getEncoded()));
            stmt.setString(4, Base64.getEncoder().encodeToString(transaction.getRecipient().getEncoded()));
            stmt.setLong(5, transaction.getAmount());
            stmt.setBytes(6, transaction.getSignature());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findById(String id) {
        String sql = """
            SELECT * FROM transactions WHERE transaction_id = ?
        """;

        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                List<Transaction> transactions = new ArrayList<>();

                while(rs.next()) {
                    String transactionId = rs.getString("transaction_id");
                    String blockHash = rs.getString("block_hash");
                    PublicKey sender = PublicKeyStringDecoder.stringToPublicKey(rs.getString("sender"));
                    PublicKey recipient = PublicKeyStringDecoder.stringToPublicKey(rs.getString("recipient"));
                    long amount = rs.getLong("amount");
                    byte[] signature = rs.getBytes("signature");

                    transactions.add(new Transaction(transactionId, blockHash, amount, sender, recipient, signature));
                }

                return transactions;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Select a transaction by the associated block */
    @Override
    public List<Transaction> findByBlockHash(String hash) {
        String sql = """
            SELECT * FROM transactions WHERE block_hash = ?
        """;

        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, hash);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                List<Transaction> transactions = new ArrayList<>();

                while(rs.next()) {
                    String transactionId = rs.getString("transaction_id");
                    String blockHash = rs.getString("block_hash");
                    PublicKey sender = PublicKeyStringDecoder.stringToPublicKey(rs.getString("sender"));
                    PublicKey recipient = PublicKeyStringDecoder.stringToPublicKey(rs.getString("recipient"));
                    long amount = rs.getLong("amount");
                    byte[] signature = rs.getBytes("signature");

                    transactions.add(new Transaction(transactionId, blockHash, amount, sender, recipient, signature));
                }

                return transactions;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override 
    public List<Transaction> findAll() throws SQLException {
        String sql = """
            SELECT * FROM transactions 
        """;
        
        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                List<Transaction> transactions = new ArrayList<>();

                while(rs.next()) {
                    String transactionId = rs.getString("transaction_id");
                    String blockHash = rs.getString("block_hash");
                    PublicKey sender = PublicKeyStringDecoder.stringToPublicKey(rs.getString("sender"));
                    PublicKey recipient = PublicKeyStringDecoder.stringToPublicKey(rs.getString("recipient"));
                    long amount = rs.getLong("amount");
                    byte[] signature = rs.getBytes("signature");

                    transactions.add(new Transaction(transactionId, blockHash, amount, sender, recipient, signature));
                }

                return transactions;
            }
        }
    }
}
