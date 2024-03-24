package com.edu.kt.gw.simple.common.config;

import com.edu.kt.gw.simple.common.filter.AuthorizationHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class RouteConfig {

    @Autowired
    private AuthorizationHeaderFilter authorizationHeaderFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder,AuthorizationHeaderFilter authorizationHeaderFilter) {

        return builder.routes()
                .route("command-and-query", r -> r
                        .path("/api/v1/**")
                        .uri("http://211.43.12.210:31511")) // 목적지 URI 설정
                .build();
                //--------------------------------------------------------------------------------
//                .route("command-service", r -> r
//                        .path("/api/v1/**")
//                        .and().method(HttpMethod.POST)
////                        .header("APP_NAME","command")
//                        .filters(f -> f.filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config("message", true, true)))
//                        .addRequestHeader("appname", "command")
//                        )
////                        .filters(f -> f.addRequestHeader("appname", "command"))
//                        .uri("http://211.43.12.210:31671/api/v1"))
//                .route("query-service", r -> r
//                        .path("/api/v1/**")
//                        .and().method(HttpMethod.GET)
////                        .header("APP_NAME","query")
//                        .filters(f -> f.filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config("message", true, true)))
//                                .addRequestHeader("appname", "query")
//
//                        )
////                        .filters(f -> f.addRequestHeader("appname", "query"))
//                        .uri("http://211.43.12.210:30173/api/v1"))
//                .build();
                //--------------------------------------------------------------------------------
//                .route("command-service", r -> r.path("api/v1/**")
//                        .and().method(HttpMethod.POST)
//                        .filters(f -> f.addRequestHeader("command", "This is commmand service"))
//                        .uri("http://localhost:8081"))
//                .route("query-service", r -> r.path("api/v1/**")
//                        .and().method(HttpMethod.GET)
//                        .filters(f -> f.addRequestHeader("query", "This is query service"))
//                        .uri("http://localhost:8082"))
//                .build();
    }

}