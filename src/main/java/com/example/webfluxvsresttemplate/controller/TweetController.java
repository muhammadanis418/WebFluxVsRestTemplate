package com.example.webfluxvsresttemplate.controller;


import com.example.webfluxvsresttemplate.entity.Tweets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@RestController
public class TweetController {

    Logger log = LoggerFactory.getLogger(TweetController.class);


    private final RestTemplate rt;

    public TweetController(RestTemplate rt) {
        this.rt = rt;
    }

    @GetMapping("/slow-tweets")
    private List<Tweets> getAllTweets() throws InterruptedException {
        Thread.sleep(2000l);
        return Arrays.asList(new Tweets("Resttemplate ", "user1"),
                new Tweets("WebClient", "user2"),
                new Tweets("Both are Better", "user3"));
    }

    @GetMapping("/tweets-blocking")
    public List<Tweets> getBlockingTweets() throws InterruptedException {
        log.info("Start Blocking Controller");
        final String uri = "http://localhost:8080/slow-tweets";
 //       RestTemplate rt = new RestTemplate();
        ResponseEntity<List<Tweets>> response = rt.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Tweets>>() {
        });
        List<Tweets> result = response.getBody();
        result.forEach(tweets -> log.info(tweets.toString()));
        log.info("Exit Blocking Controller");
        return result;
    }

    @GetMapping("/tweets-NonBlocking")
    public Flux<Tweets> getNonBlockingTweets() {
        log.info("Start Non-Blocking Api");
        Flux<Tweets> tweetsFlux = WebClient.create().get()
                .uri("http://localhost:8080/slow-tweets")
                .retrieve().
                bodyToFlux(Tweets.class);
        tweetsFlux.subscribe(tweets -> log.info(tweets.toString()));
        log.info("Exit Non-Blocking Api");
        return tweetsFlux;
    }
}
