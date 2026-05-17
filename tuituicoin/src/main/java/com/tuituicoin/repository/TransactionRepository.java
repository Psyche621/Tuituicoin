package com.tuituicoin.repository;

import java.security.PublicKey;
import java.sql.SQLException;
import java.util.List;

import com.tuituicoin.blockchain.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction, String blockHash) throws SQLException;

    List<Transaction> findById(String hash) throws SQLException;

    List<Transaction> findByBlockHash(String hash) throws SQLException;

    List<Transaction> findAll() throws SQLException;

    List<Transaction> findBySender(PublicKey sender) throws SQLException;
    
    List<Transaction> findByRecipient(PublicKey recipient) throws SQLException;
}

