package net.springboot.synpulse8challenges.repositories;

import net.springboot.synpulse8challenges.model.User;
import org.springframework.data.mongodb.repository.*;

import java.util.Optional;


public interface UserRepositories extends MongoRepository<User, String> {
    Optional<User> findByUserId(String userId);
}
