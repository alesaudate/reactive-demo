package com.baeldung.reactivedemo;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveControllerIntegrationTest {

    @LocalServerPort
    private Integer localPort;

    @Test()
    @Timeout(value = 6, unit = TimeUnit.SECONDS)
    public void given_RequestIsSentToReactiveController_Then_ShouldEmitDataEverySecond() {
        var webClient = WebClient.builder().baseUrl(String.format("http://localhost:%d", localPort)).build();

        var fooList = webClient.get()
                .uri("/reactive")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchangeToFlux(response -> response.bodyToFlux(Foo.class))
                .buffer(5)
                .flatMap(Mono::just)
                .blockFirst();


        var atomicLong = new AtomicLong();
        assertEquals(5, fooList.size());
        fooList.forEach(f -> assertEquals(atomicLong.getAndIncrement(), f.getId()));
        fooList.forEach(f -> assertEquals("Bar", f.getName()));
    }
}
