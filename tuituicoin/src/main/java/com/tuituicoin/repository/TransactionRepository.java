package com.tuituicoin.repository;

import java.sql.SQLException;
import java.util.List;

import com.tuituicoin.blockchain.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction, String blockHash) throws SQLException;

    List<Transaction> findById(String hash) throws SQLException;

    List<Transaction> findByBlockHash(String hash) throws SQLException;

    List<Transaction> findAll() throws SQLException;
}
