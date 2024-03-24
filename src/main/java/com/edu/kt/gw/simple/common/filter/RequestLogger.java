package com.edu.kt.gw.simple.common.filter;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Component
//@CircuitBreaker(name="apigw")
public class RequestLogger implements WebFilter {

    private static final byte[] EMPTY_BYTES = new byte[0];

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        //⭐️Pre Filter

        // GUID 생성
        String guid = UUID.randomUUID().toString();

        // HTTP 요청의 헤더에 GUID 추가
        exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.add("guid", guid))
                .build();

        //Request Body, Header
        return DataBufferUtils
                .join(exchange.getRequest().getBody())
                .map(databuffer -> {

                    final byte[] bytes = new byte[databuffer.readableByteCount()];

                    DataBufferUtils.release(databuffer.read(bytes));

                    return bytes;
                })
                .defaultIfEmpty(EMPTY_BYTES)
                .doOnNext(
                        bytes -> Mono.fromRunnable(
                                        ()->{

                                            // 리퀘스트 헤더와 바디를 출력
                                            System.out.println("🔥🔥Request Header : "+exchange.getRequest().getHeaders());
                                            System.out.println("🔥🔥Request Body : "+new String(bytes));
                                        })
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe())
                .flatMap(
                        bytes -> chain.filter(
                                //⭐️Post Filter
                                exchange
                                        .mutate()
                                        .request(new RequestBodyDecorator(exchange, bytes))
                                        .build()));
    }
}