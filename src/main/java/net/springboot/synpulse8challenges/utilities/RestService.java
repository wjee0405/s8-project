package net.springboot.synpulse8challenges.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestService {
    @Autowired
    RestTemplate restTemplate;

    public Object callExternalAPI(String url, HttpHeaders httpHeaders, Object requestBody, HttpMethod httpMethod, Class<?> resultClass){
        HttpEntity<?> httpEntity = new HttpEntity<Object>(requestBody,httpHeaders);
        return restTemplate.exchange(url, httpMethod, httpEntity,resultClass).getBody();
    }
}
