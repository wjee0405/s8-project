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
    TopicCreation topicCreation;
    private KafkaTemplate<String, Accounts> accountsKafkaTemplate;

    private void createAccount(UserCreation userCreation ){
        Message<UserCreation> message = MessageBuilder.withPayload(userCreation)
                .setHeader(KafkaHeaders.TOPIC,userCreation.getUserId())
                .build();
        accountsKafkaTemplate.send(message);
    }

    public ResponseEntity<ResponseObject> createUser(String userId){
        List<String> msg=new ArrayList<>();
        HttpStatus httpStatus = null;
        if(StringUtils.isEmpty(userId)){
            msg.add(ResponseConstants.USERID_CANNOT_BE_NULL);
            httpStatus = HttpStatus.BAD_REQUEST;
        }else{
            topicCreation.createTopic(userId);
            msg.add(ResponseConstants.USER_CREATION_SUCCESS);
            httpStatus = HttpStatus.OK;
        }
        return ResponseUtility.buildResponse(msg,httpStatus,null);
    }
}
