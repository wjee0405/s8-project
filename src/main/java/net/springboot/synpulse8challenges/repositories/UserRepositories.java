package net.springboot.synpulse8challenges.repositories;

import net.springboot.synpulse8challenges.model.UserCreation;
import org.springframework.data.mongodb.repository.*;

import java.util.Optional;


public interface UserRepositories extends MongoRepository<UserCreation, String> {
    Optional<UserCreation> findByUserId(String userId);
}
