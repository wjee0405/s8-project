package net.springboot.synpulse8challenges.controllers;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.kafka.AccountOpsImpl;
import net.springboot.synpulse8challenges.kafka.KafkaTopicOps;
import net.springboot.synpulse8challenges.kafka.TransactionOpsImpl;
import net.springboot.synpulse8challenges.kafka.UserOps;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.TransactionQuery;
import net.springboot.synpulse8challenges.model.UserCreation;
import net.springboot.synpulse8challenges.utilities.ResponseUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/account")
@NoArgsConstructor
@Log4j2
public class AccountController {
    @Autowired
    AccountOpsImpl accountOps;
    @Autowired
    UserOps userOps;
    @Autowired
    KafkaTopicOps kafkaTopicOps;
    @Autowired
    TransactionOpsImpl transactionOps;

    @PostMapping("/createUser")
    public ResponseEntity<ResponseObject> createUser(@RequestBody UserCreation userCreation){
        log.info("URL:{} ,Request Body:{} {}","createUser",userCreation);
        ResponseEntity<ResponseObject> response = userOps.createUser(userCreation.getUserId());
        return response;
    }

    @PostMapping("/createCurrencyAccount")
    public ResponseEntity<ResponseObject> createCurrencyAccount(@RequestParam("userId") String userId,
                                                                @RequestParam("country") String country){
        log.info("URL:{} ,Request Params:{} {}","createCurrencyAccount",userId,country);
        ResponseEntity<ResponseObject> response = accountOps.createAccount(userId,country);
        return response;
    }

    @GetMapping("/getCurrencyAccounts")
    public ResponseEntity<ResponseObject> getCurrencyAccounts(@RequestParam("userId")String userId){
        log.info("URL:{} ,Request Param:{}","getCurrencyAccounts",userId);
        ResponseEntity<ResponseObject> response = accountOps.findCurrencyAccounts(userId);
        return response;
    }

    @GetMapping("/getTopics")
    public ResponseEntity<ResponseObject> getTopics(){
        Set<String> topics = kafkaTopicOps.getTopics();
        ResponseEntity<ResponseObject> response = ResponseUtility.buildResponse(null, HttpStatus.OK,topics);
        return response;
    }

    @PostMapping("/sendTransaction")
    public ResponseEntity<ResponseObject> sendTransaction(@RequestBody Transaction transaction){
        log.info("URL:{} ,Request Body:{}","sendTransaction",transaction);
        ResponseEntity<ResponseObject> response = transactionOps.createTransaction(transaction);
        return response;
    }

    @GetMapping("/findTransactionByUser")
    public ResponseEntity<ResponseObject> findTransactionByUser(TransactionQuery transactionQuery){
        log.info("URL:{} ,Request Param:{}","findTransactionByUser",transactionQuery);
        ResponseEntity<ResponseObject> response = transactionOps.findTransactionSummaryByUser(transactionQuery);
        return response;
    }

    @GetMapping("/findTransactionByAccount")
    public ResponseEntity<ResponseObject> findTransactionByAccount(TransactionQuery transactionQuery){
        log.info("URL:{} ,Request Param:{}","findTransactionByAccount",transactionQuery);
        ResponseEntity<ResponseObject> response = transactionOps.findTransactionSummaryByAccount(transactionQuery);
        return response;
    }
}
