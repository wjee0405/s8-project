package net.springboot.synpulse8challenges.model;

import lombok.Data;

@Data
public class TransactionDTO {
    private String transactionId;
    private Double amount;
    private String accountNo;
    private String valueDate;
    private String description;
    private String currencyAmount;
}
