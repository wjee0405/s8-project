package net.springboot.synpulse8challenges.repositories;

import net.springboot.synpulse8challenges.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepositories extends MongoRepository<Account,String> {
    Optional<Account> findByAccountNo(String accountNo);
    List<Account> findByAccountOwner(String accountOwner);
}
