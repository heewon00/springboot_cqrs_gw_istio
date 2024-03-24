package com.edu.kt.gw.simple.common.filter;


import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
public class RequestBodyDecorator extends ServerHttpRequestDecorator {

    private final byte[] bytes;
    private final ServerWebExchange exchange;

    // RequestBodyDecorator 생성자
    public RequestBodyDecorator(ServerWebExchange exchange, byte[] bytes) {
        // 부모 클래스(ServerHttpRequestDecorator)의 생성자 호출
        super(exchange.getRequest());
        // 전달받은 매개변수들을 멤버 변수에 저장
        this.bytes = bytes;  // Request Body를 저장하는 바이트 배열
        this.exchange = exchange;  // ServerWebExchange 객체
    }

    // Request Body를 변경하여 반환하는 메서드
    @Override
    public Flux<DataBuffer> getBody() {
        // 변경된 Request Body를 Flux로 감싸서 반환
        // bytes가 null이거나 길이가 0인 경우 Flux.empty()를 반환하여 비어있는 Flux를 생성
        // 그렇지 않으면 변경된 Request Body를 Flux로 감싸서 반환
        return bytes == null || bytes.length == 0 ?
                Flux.empty() : Flux.just(exchange.getResponse().bufferFactory().wrap(bytes));
    }
}