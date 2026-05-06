package com.mintcoin;

public class Main {
    public static void main(String[] args) {
        Transaction transaction = new Transaction(10, "Mike", "Bob");
        System.out.println(transaction.toString());
    }
}
