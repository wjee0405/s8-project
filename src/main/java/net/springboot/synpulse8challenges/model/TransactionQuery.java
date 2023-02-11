package net.springboot.synpulse8challenges.model;

import lombok.Data;

@Data
public class TransactionQuery {
  private String userId;
  private String accountNo;
  private String transactionStartDateValue;
  private String transactionEndDateValue;
}
