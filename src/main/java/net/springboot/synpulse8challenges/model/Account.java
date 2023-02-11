package net.springboot.synpulse8challenges.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Account")
public class Account {
    String accountOwner;
    String accountNo;
    String currency;
    String country;
    boolean active;
}
