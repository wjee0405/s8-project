package net.springboot.synpulse8challenges.utilities;

import net.springboot.synpulse8challenges.model.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseUtility {
    public static ResponseEntity<ResponseObject> buildResponse(List<String> message, HttpStatus httpStatus, Object obj){
        ResponseObject responseObject = new ResponseObject();

        responseObject.setTimestamp(LocalDateTime.now());
        responseObject.setMessage(message);
        responseObject.setStatus(httpStatus.getReasonPhrase());
        responseObject.setCode(httpStatus.toString());
        responseObject.setObj(obj);

        return new ResponseEntity<>(responseObject,httpStatus);
    }
}
