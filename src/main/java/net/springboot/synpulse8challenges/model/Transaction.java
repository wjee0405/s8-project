package net.springboot.synpulse8challenges.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
@Document(collection = "Transaction")
@Data
@JsonInclude(Include.NON_NULL)
public class Transaction {
    private String transactionId;
    private Double amount;
    private String accountNo;
    private Date transactionDate;
    private String valueDate;
    private String description;
    private String currency;
    private String currencyAmount;
}
