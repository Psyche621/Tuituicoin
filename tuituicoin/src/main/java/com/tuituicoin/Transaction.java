package com.tuituicoin;

import java.security.PublicKey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuituicoin.util.Hash;

public class Transaction {
    private String transactionId;
    private long amount;
    private PublicKey payer;
    private PublicKey payee;

    public Transaction(long amount, PublicKey payer, PublicKey payee) {
        transactionId = Hash.hash(serialize(), "SHA-256");
        this.amount = amount;
        this.payer = payer;
        this.payee = payee;
    }

    public long getAmount() {
        return amount;
    }

    public PublicKey getPayer() {
        return payer;
    }

    public PublicKey getPayee() {
        return payee;
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
