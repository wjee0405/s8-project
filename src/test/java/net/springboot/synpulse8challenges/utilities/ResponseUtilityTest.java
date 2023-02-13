package net.springboot.synpulse8challenges.utilities;

import net.springboot.synpulse8challenges.model.ResponseObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

public class ResponseUtilityTest {

    @Test
    public void testBuildResponse() {
        ResponseEntity<ResponseObject> result = ResponseUtility.buildResponse(
                Collections.singletonList("Test123"), HttpStatus.OK, Collections.singletonList("Object"));
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals("Test123", result.getBody().getMessage().get(0));
    }
}
