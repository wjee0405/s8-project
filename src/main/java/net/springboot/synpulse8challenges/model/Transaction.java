package net.springboot.synpulse8challenges.model;

import lombok.Data;

import java.util.Date;
@Data
public class Transaction {
    private String id;
    private String amount;
    private String accountNo;
    private Date transactionDate;
    private String description;
    private String currency;
    private Double monetaryAmount;
}
