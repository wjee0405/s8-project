package net.springboot.synpulse8challenges.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TransactionSummary {
  private Double debit;
  private Double credit;
  private Map<String,Double> debitInOriginalCurrency;
  private Map<String,Double> creditInOriginalCurrency;
  private String exchangedCurrency;
  List<Transaction> transactionList;
}
