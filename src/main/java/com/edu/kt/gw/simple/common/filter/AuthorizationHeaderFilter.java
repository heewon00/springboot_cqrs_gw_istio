package com.edu.kt.gw.simple.common.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>{

    //---------secretKey 불러오기---------
    @Value("${jwt.secret}")

    private String secretKey;

    private SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    //--------------------------------


    //생성시 Config class를 상속받은 Factory로 넘겨줘야해서 lombok을 사용하지 않고 다음과 같이 처리
    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    //--------------------------------

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            //header에 HttpHeaders.AUTHORIZATION 값이 존재하는지 확인
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
//                System.out.println("🚨 no authorization header"+HttpStatus.UNAUTHORIZED);
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");
            System.out.println("🚨 JWT : "+jwt);

            if(!isJwtValid(jwt)){
//                System.out.println("🚨 JWT Token is not valid : "+HttpStatus.UNAUTHORIZED);
                return onError(exchange, "JWT Token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }

    //token이 유효한지 확인
    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        Object subject = null;
        System.out.println("🚨 secretKey : "+getSigningKey());

        try {
            subject = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
            System.out.println("🚨 JWT 인증 성공! : "+subject);
        }catch (Exception e){
            returnValue = false;
        }

        if(subject == null || subject.equals("")){
            System.out.println("🚨 subject : "+subject);
            returnValue = false;
        }
        return returnValue;
    }

    //에러 발생시 에러 값을 response
    //Mono, Flux -> Spring WebFlux 개념 / 데이터 단위 단일=Mono, 복수=Flux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        System.out.println("🚨 Error : "+err);
        log.error(err);
        return response.setComplete();  //Mono 데이터 return
    }
}