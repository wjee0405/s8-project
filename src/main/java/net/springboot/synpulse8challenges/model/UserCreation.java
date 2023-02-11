package net.springboot.synpulse8challenges.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
@Data
public class UserCreation {
    private String userId;
}
