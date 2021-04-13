package com.baeldung.reactivedemo;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/reactive")
public class ReactiveController {

    AtomicLong atomicLong = new AtomicLong(0);


    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Foo> emitter() {
        return Flux.fromStream(this::emitFoo).delayElements(Duration.of(1, ChronoUnit.SECONDS)).share();
    }


    private Stream<Foo> emitFoo() {
        return LongStream.range(0, Long.MAX_VALUE).mapToObj(l -> new Foo(l, "Bar"));
    }


}
