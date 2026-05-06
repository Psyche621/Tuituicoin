package com.mintcoin;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Transaction {
    private long amount;
    private String payer;
    private String payee;

    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
