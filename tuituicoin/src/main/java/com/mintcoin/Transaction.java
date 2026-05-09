package com.mintcoin;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Transaction {
    private long amount;
    private String payer;
    private String payee;

    public Transaction(long amount, String payer, String payee) {
        this.amount = amount;
        this.payer = payer;
        this.payee = payee;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
