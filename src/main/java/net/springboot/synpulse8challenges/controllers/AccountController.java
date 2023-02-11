package net.springboot.synpulse8challenges.controllers;

import lombok.NoArgsConstructor;
import net.springboot.synpulse8challenges.kafka.AccountOpsImpl;
import net.springboot.synpulse8challenges.kafka.KafkaTopicOps;
import net.springboot.synpulse8challenges.kafka.TransactionOpsImpl;
import net.springboot.synpulse8challenges.kafka.UserOps;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.Transaction;
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
        ResponseEntity<ResponseObject> response = userOps.createUser(userCreation.getUserId());
        return response;
    }

    @PostMapping("/createCurrencyAccount")
    public ResponseEntity<ResponseObject> createCurrencyAccount(@RequestParam("userId") String userId,
                                                                @RequestParam("country") String country){
        ResponseEntity<ResponseObject> response = accountOps.createAccount(userId,country);
        return response;
    }

    @GetMapping("/getCurrencyAccounts")
    public ResponseEntity<ResponseObject> getCurrencyAccounts(@RequestParam("userId")String userId){
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
        ResponseEntity<ResponseObject> response = transactionOps.createTransaction(transaction);
        return response;
    }
}
