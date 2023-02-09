package net.springboot.synpulse8challenges.controllers;

import lombok.NoArgsConstructor;
import net.springboot.synpulse8challenges.kafka.AccountOpsImpl;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.UserCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@NoArgsConstructor
public class AccountController {
    @Autowired
    AccountOpsImpl accountOps;

    @PostMapping("/createAccount")
    public ResponseEntity<ResponseObject> createAccount(@RequestBody UserCreation userCreation){
        ResponseEntity<ResponseObject> response = accountOps.createUser(userCreation.getUserId());
        return response;
    }
}
