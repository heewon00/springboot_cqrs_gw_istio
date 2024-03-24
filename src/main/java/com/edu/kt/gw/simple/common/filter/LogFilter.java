//package com.edu.kt.gw.simple.common.filter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Random;
//import java.util.UUID;
//@Slf4j
//@Component
//public class LogFilter implements WebFilter {
//    long startTime = System.currentTimeMillis();
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//
//        // ⭐️pre
//
//        // GUID 생성
//        String guid = UUID.randomUUID().toString();
//
//        // HTTP 요청의 헤더에 GUID 추가
//        exchange.getRequest().mutate()
//                .headers(httpHeaders -> httpHeaders.add("GUID", guid))
//                .build();
//
//        // 요청 처리 전 로깅
//        logRequest(exchange, guid);
//
//        // 다음 필터 또는 핸들러 호출
//        return chain.filter(exchange).doAfterTerminate(() -> {
//            // ⭐post
//            // 응답 처리 후 로깅
//            logResponse(exchange, guid);
//        });
//    }
//
//    // 요청 처리 전 로깅
//    private void logRequest(ServerWebExchange exchange, String guid) {
//        String path = exchange.getRequest().getURI().getPath();
//        log.info("🔥Serving : '{}', guid : '{}'", path, guid);
//    }
//
//    // 요청 처리 후 로깅
//    private void logResponse(ServerWebExchange exchange, String guid) {
//        String path = exchange.getRequest().getURI().getPath();
//        exchange.getResponse().getHeaders().entrySet().forEach(e ->
//                log.info("🔥Served Response Headers '{}' : '{}'",e.getKey(),e.getValue()));
//
//        log.info("🔥Served '{}' as {}, guid : '{}' in {} ms",
//                path,
//                exchange.getResponse().getStatusCode(),
//                guid,
//                System.currentTimeMillis() - startTime);
//    }
//}