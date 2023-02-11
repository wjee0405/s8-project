package net.springboot.synpulse8challenges.model;

import lombok.Data;

import java.util.List;

@Data
public class TransactionSummary {
  private Double debit;
  private Double credit;
  private Double debitInOriginalCurrency;
  private Double creditInOriginalCurrency;
  private String originalCurrency;
  private String exchangedCurrency;
  private Double exchangeRate;
  List<Transaction> transactionList;
}
