package com.tuituicoin.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuituicoin.util.Hash;

public class Transaction {
    private String transactionId;
    private String blockHash;
    private long amount;
    private PublicKey sender;
    private PublicKey recipient;
    private byte[] signature = null;


    /* Constructor for new transaction objects */
    public Transaction(long amount, String blockHash, PublicKey sender, PublicKey recipient) {
        transactionId = Hash.hash(serialize(), "SHA-256");
        this.blockHash = blockHash;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
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

    public void sign(PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(getSigningData().getBytes(StandardCharsets.UTF_8));
        this.signature = sign.sign();
    }

    public boolean verify() throws Exception {
        if (signature == null) {
            return false;
        }

        Signature verifier = Signature.getInstance("SHA256withRSA");

        verifier.initVerify(sender);
        verifier.update(getSigningData().getBytes(StandardCharsets.UTF_8));

        return verifier.verify(signature);
    }

    private String getSigningData() {
        return amount + sender.toString() + recipient.toString();
    }

    public String serialize() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
