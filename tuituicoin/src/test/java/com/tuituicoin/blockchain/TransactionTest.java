package com.tuituicoin.blockchain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Test;

public class TransactionTest {

    @Test
    public void testSignAndVerify() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey sender = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey recipient = keyPairGenerator.generateKeyPair().getPublic();

        Transaction transaction = new Transaction(100, "prev-hash", sender, recipient);
        transaction.sign(privateKey);

        boolean result = transaction.verify();
        System.out.println("[TransactionTest] signature verification result=" + result);
        assertTrue(result);
    }

    @Test
    public void testVerifyFailsAfterTampering() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey sender = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey recipient = keyPairGenerator.generateKeyPair().getPublic();

        Transaction transaction = new Transaction(100, "prev-hash", sender, recipient);
        transaction.sign(privateKey);

        boolean originalVerified = transaction.verify();
        System.out.println("[TransactionTest] verified before tampering=" + originalVerified);
        assertTrue(originalVerified);

        Field amountField = Transaction.class.getDeclaredField("amount");
        amountField.setAccessible(true);
        amountField.setLong(transaction, 200);

        boolean tamperedVerified = transaction.verify();
        System.out.println("[TransactionTest] verified after tampering=" + tamperedVerified);
        assertFalse(tamperedVerified);
    }
}
