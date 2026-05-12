package com.tuituicoin.repository;

import java.sql.SQLException;
import java.util.List;

import com.tuituicoin.blockchain.Block;

public interface BlockRepository {
    void save(Block block) throws SQLException;

    Block findByHash(String hash) throws SQLException;

    Block findLatest() throws SQLException;

    List<Block> findAll() throws SQLException;
}
