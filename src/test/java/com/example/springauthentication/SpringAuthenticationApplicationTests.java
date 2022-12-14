package com.example.springauthentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringAuthenticationApplicationTests {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl(){
        return "http://localhost:"+port+"/greeting";

    }

    @DisplayName("1. 인증실패")
    @Test
    void test_1(){

         HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, ()-> {
            client.getForObject(greetingUrl(), String.class);

        });
        assertEquals(401, exception.getRawStatusCode());

    }

    @DisplayName("2. 인증성공")
    @Test
    void test_2(){

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                "user:1111".getBytes()
                )
        );
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);
        //TODO: 401 BODY값 안나오는 원인 찾아야함함
       assertEquals("hello", resp.getBody());
    }

    @DisplayName("3. 인증성공")
    @Test
    void test_3(){
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", resp);

    }
    @DisplayName("4. POST 인증확인")
    @Test
    void test_4(){
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> resp = testClient.postForEntity(greetingUrl(), "pwoogi", String.class);
        assertEquals("hello pwoogi", resp.getBody());

    }

}
