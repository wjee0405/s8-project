package net.springboot.synpulse8challenges.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RestServiceTest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    RestService restService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCallExternalAPI() {
        ResponseEntity<String> mockClass = new ResponseEntity<>("123", HttpStatus.OK);
        ObjectMapper objectMapper = new ObjectMapper();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class),
                any(HttpEntity.class), any(Class.class))).thenReturn(mockClass);

        String result = (String) restService.callExternalAPI("123", null, null, HttpMethod.GET, String.class);
        Assertions.assertEquals("123", result);
    }
}
