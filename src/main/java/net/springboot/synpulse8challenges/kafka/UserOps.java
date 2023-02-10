package net.springboot.synpulse8challenges.kafka;

import lombok.NoArgsConstructor;
import net.springboot.synpulse8challenges.constants.PrefixConstants;
import net.springboot.synpulse8challenges.constants.ResponseConstants;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.utilities.ResponseUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class UserOps {

    @Autowired
    KafkaTopicOps kafkaTopicOps;

    public boolean findUser(String userId) {
        boolean result = Boolean.FALSE;
        String userAccount = PrefixConstants.ACCOUNT_PREFIX + userId;
        Set<String> userList = kafkaTopicOps.getTopics();
        userList = userList.stream()
                .filter(p -> p.startsWith(PrefixConstants.ACCOUNT_PREFIX))
                .collect(Collectors.toSet());
        if (userList.contains(userAccount)) {
            result = Boolean.TRUE;
        }
        return result;
    }

    public ResponseEntity<ResponseObject> createUser(String userId){
        List<String> msg=new ArrayList<>();
        HttpStatus httpStatus = null;
        if(StringUtils.isEmpty(userId)){
            msg.add(ResponseConstants.USERID_CANNOT_BE_NULL);
            httpStatus = HttpStatus.BAD_REQUEST;
        }else{
            String userAccount = PrefixConstants.USER_PREFIX + userId;
            boolean result = kafkaTopicOps.createTopic(userAccount);
            if(result){
                msg.add(ResponseConstants.USER_CREATION_SUCCESS);
                httpStatus = HttpStatus.OK;
            }else{
                msg.add(ResponseConstants.USER_CREATION_FAIL);
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        }
        return ResponseUtility.buildResponse(msg,httpStatus,null);
    }
}
