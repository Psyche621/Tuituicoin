package com.tuituicoin.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuituicoin.util.Hash;

public class Transaction {
    private String transactionId;
    private String blockHash;
    private long amount;
    private PublicKey sender;
    private PublicKey recipient;
    private byte[] signature = null;

    private static final Logger LOGGER = Logger.getLogger(Transaction.class.getName());

    /* Constructor for new transaction objects */
    public Transaction(long amount, String blockHash, PublicKey sender, PublicKey recipient) {
        this.transactionId = Hash.hash(serialize(), "SHA-256");
        this.blockHash = blockHash;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;

        LOGGER.info("Created new transaction with ID: " + transactionId);
    }

    /* Constructor for reconstructing transaction objects */
    public Transaction(String transactionId, String blockHash, long amount, PublicKey sender, PublicKey recipient,
            byte[] signature) {
        this.transactionId = transactionId;
        this.blockHash = blockHash;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.signature = signature;

        LOGGER.info("Loaded transaction with ID: " + transactionId);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public long getAmount() {
        return amount;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public byte[] getSignature() {
        return signature;
    }

    public long getSent(PublicKey sender) {
        return amount;
    }

    public long getReceived(PublicKey recipient) {
        return amount;
    }

    /* Signs the transaction with the provided private key */
    public void sign(PrivateKey privateKey) {
        try {
            LOGGER.info("Signing transaction with ID: " + transactionId);
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(getSigningData().getBytes(StandardCharsets.UTF_8));
            this.signature = sign.sign();

            LOGGER.info("Transaction " + transactionId + " signed successfully.");
        } catch (Exception e) {
            LOGGER.severe("Failed to sign transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /* Verifies the transaction's signature */
    public boolean verify() throws Exception {
        if (signature == null) {
            LOGGER.warning("Transaction " + transactionId + " has no signature.");
            return false;
        }

        LOGGER.info("Verifying transaction with ID: " + transactionId);
        Signature verifier = Signature.getInstance("SHA256withRSA");

        verifier.initVerify(sender);
        verifier.update(getSigningData().getBytes(StandardCharsets.UTF_8));

        return verifier.verify(signature);
    }

    /* Helper method to generate data for signing */
    private String getSigningData() {
        LOGGER.info("Generating signing data for transaction: " + transactionId);
        return amount + sender.toString() + recipient.toString();
    }

    /* Serializes the transaction to a JSON string */
    public String serialize() {
        LOGGER.info("Serializing transaction with ID: " + transactionId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
