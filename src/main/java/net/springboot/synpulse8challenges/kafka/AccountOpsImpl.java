package net.springboot.synpulse8challenges.kafka;

import lombok.NoArgsConstructor;
import net.springboot.synpulse8challenges.constants.ResponseConstants;
import net.springboot.synpulse8challenges.model.Accounts;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.UserCreation;
import net.springboot.synpulse8challenges.utilities.ResponseUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class AccountOpsImpl {
    @Autowired
    KafkaTopicOps kafkaTopicOps;
    private KafkaTemplate<String, Accounts> accountsKafkaTemplate;

    private void createAccount(UserCreation userCreation ){
        Message<UserCreation> message = MessageBuilder.withPayload(userCreation)
                .setHeader(KafkaHeaders.TOPIC,userCreation.getUserId())
                .build();
        accountsKafkaTemplate.send(message);
    }

//    public ResponseEntity<ResponseObject> createAccount(String userId,String country){
//        List<String> msg=new ArrayList<>();
//        HttpStatus httpStatus = null;
//        if(StringUtils.isEmpty((userId)) || StringUtils.isEmpty(country)){
//            msg.add(ResponseConstants.INSUFFICIENT_INPUT_PARAMETER);
//            httpStatus = HttpStatus.BAD_REQUEST;
//        }else{
//
//        }
//    }

}
