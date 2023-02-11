package net.springboot.synpulse8challenges.kafka;

import lombok.NoArgsConstructor;
import net.springboot.synpulse8challenges.config.KafkaTopicConfigs;
import net.springboot.synpulse8challenges.constants.ResponseConstants;
import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.UserCreation;
import net.springboot.synpulse8challenges.repositories.UserRepositories;
import net.springboot.synpulse8challenges.utilities.ResponseUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class UserOps {

    @Autowired
    KafkaTopicOps kafkaTopicOps;
    @Autowired
    UserRepositories userRepositories;
    @Autowired
    KafkaTopicConfigs kafkaTopicConfigs;
    @Autowired
    private KafkaTemplate<String, Account> accountsKafkaTemplate;

//    public boolean findUser(String userId) {
//        boolean result = Boolean.FALSE;
//        String userAccount = PrefixConstants.USER_PREFIX + userId;
//        Set<String> userList = kafkaTopicOps.getTopics();
//        userList = userList.stream()
//                .filter(p -> p.startsWith(PrefixConstants.USER_PREFIX))
//                .collect(Collectors.toSet());
//        if (userList.contains(userAccount)) {
//            result = Boolean.TRUE;
//        }
//        return result;
//    }

    public boolean findUser(String userId){
        Optional<UserCreation> user = userRepositories.findByUserId(userId);
        return (user.isPresent());
    }

    public ResponseEntity<ResponseObject> createUser(String userId){
        List<String> msg=new ArrayList<>();
        HttpStatus httpStatus = null;
        if(StringUtils.isEmpty(userId)){
            msg.add(ResponseConstants.USERID_CANNOT_BE_NULL);
            httpStatus = HttpStatus.BAD_REQUEST;
        }else{
            boolean userExist = findUser(userId);
            if(userExist){
                msg.add(ResponseConstants.USER_EXISTS);
                httpStatus = HttpStatus.BAD_REQUEST;
            }else{
                UserCreation userCreation = new UserCreation();
                userCreation.setUserId(userId);
                Message<UserCreation> message = MessageBuilder.withPayload(userCreation)
                        .setHeader(KafkaHeaders.TOPIC,kafkaTopicConfigs.getUsersTopic())
                        .build();

                accountsKafkaTemplate.send(message);
                msg.add(ResponseConstants.USER_CREATION_SUCCESS);
                httpStatus = HttpStatus.CREATED;
            }
        }
        return ResponseUtility.buildResponse(msg,httpStatus,null);
    }
}
