package net.springboot.synpulse8challenges.repositories;

import net.springboot.synpulse8challenges.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepositories extends MongoRepository<Transaction, String> {
    List<Transaction> findByAccountNo(String accountNo);
}
