package com.edu.kt.gw.simple.common.filter;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ObjectInputFilter;
import java.nio.charset.StandardCharsets;

@Component
//@CircuitBreaker(name="apigw")
public class ResponseLogger implements WebFilter {
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //⭐️Pre Filter
        //data 변환 조건
        if(true) {
            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            ServerHttpResponse response = exchange.getResponse();
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory().join(dataBuffers);

                            byte[] content = new byte[joinedBuffers.readableByteCount()];

                            joinedBuffers.read(content);

                            //
                            String responseBody = new String(content, StandardCharsets.UTF_8);
                            System.out.println("🔥🔥Response Header : "+exchange.getResponse().getHeaders());
                            System.out.println("🔥🔥Response Body : "+responseBody);
                            try {
                                return bufferFactory.wrap(content);
                            } catch (Exception e) {
                                // data 수정 없이 return
                                return joinedBuffers;
                            }
                        }));
                    }
                    return super.writeWith(body);
                }
            };

            //⭐️Post Filter
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }

        // 기존 return
        else{
            return chain.filter(exchange);
        }
    };
}