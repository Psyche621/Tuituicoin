package com.tuituicoin.blockchain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Before;
import org.junit.Test;

public class ChainTest {
    private static final Path DATABASE_PATH = Paths.get("target/test-blockchain.db");

    @Before
    public void setUp() throws Exception {
        System.setProperty("tuituicoin.db.url", "jdbc:sqlite:target/test-blockchain.db");
        Files.deleteIfExists(DATABASE_PATH);
        resetChainSingleton();
    }

    @Test
    public void testEmptyChainIsValid() {
        Chain chain = Chain.getInstance();
        boolean valid = chain.isValid();
        System.out.println("[ChainTest] empty chain valid=" + valid + " lastBlock=" + chain.getLastBlock());

        assertTrue(valid);
        assertTrue(chain.getLastBlock() == null);
    }

    @Test
    public void testAddBlockCreatesGenesisBlockAndChainRemainsValid() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        PublicKey sender = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey recipient = generator.generateKeyPair().getPublic();

        Transaction transaction = new Transaction(50, "0", sender, recipient);
        transaction.sign(privateKey);

        Chain chain = Chain.getInstance();
        chain.addBlock(transaction);

        boolean valid = chain.isValid();
        Block lastBlock = chain.getLastBlock();
        System.out.println("[ChainTest] added genesis block height=" + lastBlock.getHeight() + " hash=" + lastBlock.getHash() + " valid=" + valid);

        assertTrue(valid);
        assertNotNull(lastBlock);
        assertEquals(0, lastBlock.getHeight());
        assertTrue(lastBlock.getHash().startsWith("0000"));
    }

    private void resetChainSingleton() throws Exception {
        Field instanceField = Chain.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }
}
