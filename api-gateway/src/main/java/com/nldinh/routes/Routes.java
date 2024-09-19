package com.nldinh.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

@Configuration
public class Routes {

    @Value("${product.service.url}")
    private String productServiceUrl;
    @Value("${order.service.url}")
    private String orderServiceUrl;
//    @Value("${inventory.service.url}")
//    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){
        return GatewayRouterFunctions.route("product-service")
                .route(RequestPredicates.path("api/product"), HandlerFunctions.http(productServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute(){
        return GatewayRouterFunctions.route("order-service")
                .route(RequestPredicates.path("api/order"), HandlerFunctions.http(orderServiceUrl))
                .build();
    }
}
