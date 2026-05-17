package com.tuituicoin.repository;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import com.tuituicoin.blockchain.Transaction;
import com.tuituicoin.util.PublicKeyStringDecoder;

public class SQLiteTransactionRepository implements TransactionRepository {
    private static final Logger LOGGER = Logger.getLogger(SQLiteTransactionRepository.class.getName());

    @Override
    public void save(Transaction transaction, String blockHash) {
        LOGGER.info("Saving transaction with ID: " + transaction.getTransactionId() + " to block hash: " + blockHash);

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
            LOGGER.info("Transaction saved successfully.");
        } catch (SQLException e) {
            LOGGER.severe("Failed to save transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findById(String id) {
        LOGGER.info("Finding transaction by ID: " + id);

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

                LOGGER.info("Transaction(s) found successfully.");
                return transactions;
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to find transaction by transaction ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /* Select a transaction by the associated block */
    @Override
    public List<Transaction> findByBlockHash(String hash) {
        LOGGER.info("Finding transactions by block hash: " + hash);

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

                LOGGER.info("Transactions for block hash: " + hash + " found successfully.");
                return transactions;
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to find transactions by block hash: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override 
    public List<Transaction> findAll() throws SQLException {
        LOGGER.info("Finding all transactions in the database.");

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

                LOGGER.info("All transactions found successfully.");
                return transactions;
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to find all transactions: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    } 

    @Override
    public List<Transaction> findBySender(PublicKey sender) throws SQLException {
        String senderString = Base64.getEncoder().encodeToString(sender.getEncoded());
        String sql = """
            SELECT * FROM transactions WHERE sender = ? 
                """;
        
        try (Connection conn = DatabaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, senderString);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                List<Transaction> transactions = new ArrayList<>();

                while(rs.next()) {
                    String transactionId = rs.getString("transaction_id");
                    String blockHash = rs.getString("block_hash");
                    PublicKey recipient = PublicKeyStringDecoder.stringToPublicKey(rs.getString("recipient"));
                    long amount = rs.getLong("amount");
                    byte[] signature = rs.getBytes("signature");

                    transactions.add(new Transaction(transactionId, blockHash, amount, sender, recipient, signature));
                }

                LOGGER.info("Transactions for sender: " + senderString + " found successfully.");
                return transactions;
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to find all transactions by sender: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Transaction> findByRecipient(PublicKey recipient) throws SQLException {
        String recipientString = Base64.getEncoder().encodeToString(recipient.getEncoded());
        String sql = """
            SELECT * FROM transactions WHERE recipient = ? 
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
                    long amount = rs.getLong("amount");
                    byte[] signature = rs.getBytes("signature");

                    transactions.add(new Transaction(transactionId, blockHash, amount, sender, recipient, signature));
                }

                LOGGER.info("Transactions for recipient: " + recipientString + " found successfully.");
                return transactions;
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to find all transactions by recipient: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
