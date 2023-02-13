package net.springboot.synpulse8challenges.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.service.AccountOps;
import net.springboot.synpulse8challenges.service.TransactionOps;
import net.springboot.synpulse8challenges.service.UserOps;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.TransactionQuery;
import net.springboot.synpulse8challenges.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@NoArgsConstructor
@Log4j2
public class AccountController {
    @Autowired
    AccountOps accountOps;
    @Autowired
    UserOps userOps;
    @Autowired
    TransactionOps transactionOps;

    @PostMapping("/createUser")
    @ApiOperation(value = "Create user based on ID",
            notes = "Provide userId inside UserCreation Class and call Creation API. Creation is sent to Kafka")
    public ResponseEntity<ResponseObject> createUser(@RequestBody User user) {
        log.info("URL:{} ,Request Body:{} {}", "createUser", user);
        ResponseEntity<ResponseObject> response = userOps.createUser(user.getUserId());
        return response;
    }

    @GetMapping("/getAllCountries")
    @ApiOperation(value = "Find all available countries in the application for account creation",
            notes = "Return all available countries for account creation")
    public ResponseEntity<ResponseObject> getAllCountries() {
        log.info("URL:{} ", "getAllCountries");
        ResponseEntity<ResponseObject> response = accountOps.findAllCountries();
        return response;
    }

    @PostMapping("/createCurrencyAccount")
    @ApiOperation(value = "Create a financial account(Specific Country/Currency)",
            notes = "Provide existing userId and country to create new account in the country. " +
                    "Countries can be retrieve via /getAllCountries API" +
                    "Creation is sent to Kafka")
    public ResponseEntity<ResponseObject> createCurrencyAccount(@RequestParam("userId") String userId,
                                                                @RequestParam("country") String country) {
        log.info("URL:{} ,Request Params:{} {}", "createCurrencyAccount", userId, country);
        ResponseEntity<ResponseObject> response = accountOps.createAccount(userId, country);
        return response;
    }

    @GetMapping("/getCurrencyAccounts")
    @ApiOperation(value = "Retrieve all accounts owned by the user",
            notes = "Provide userId to retrieve all accounts")
    public ResponseEntity<ResponseObject> getCurrencyAccounts(@RequestParam("userId") String userId) {
        log.info("URL:{} ,Request Param:{}", "getCurrencyAccounts", userId);
        ResponseEntity<ResponseObject> response = accountOps.findCurrencyAccounts(userId);
        return response;
    }

    @PostMapping("/sendTransaction")
    @ApiOperation(value = "Send transaction to Kafka Topic",
            notes = "Provide transaction body which consists of accountNo, amount, description and date")
    public ResponseEntity<ResponseObject> sendTransaction(@RequestBody Transaction transaction) {
        log.info("URL:{} ,Request Body:{}", "sendTransaction", transaction);
        ResponseEntity<ResponseObject> response = transactionOps.createTransaction(transaction);
        return response;
    }

    @GetMapping("/findTransactionByUser")
    @ApiOperation(value = "Find all transactions performed by user",
            notes = "Provide userId (optionally provide transactionStartDateValue and " +
                    "transactionEndDateValue to filter specific period")
    public ResponseEntity<ResponseObject> findTransactionByUser(TransactionQuery transactionQuery) {
        log.info("URL:{} ,Request Param:{}", "findTransactionByUser", transactionQuery);
        ResponseEntity<ResponseObject> response = transactionOps.findTransactionSummaryByUser(transactionQuery);
        return response;
    }

    @GetMapping("/findTransactionByAccount")
    @ApiOperation(value = "Find all transactions performed by specific account",
            notes = "Provide account (optionally provide transactionStartDateValue and " +
                    "transactionEndDateValue to filter specific period")
    public ResponseEntity<ResponseObject> findTransactionByAccount(TransactionQuery transactionQuery) {
        log.info("URL:{} ,Request Param:{}", "findTransactionByAccount", transactionQuery);
        ResponseEntity<ResponseObject> response = transactionOps.findTransactionSummaryByAccount(transactionQuery);
        return response;
    }
}
