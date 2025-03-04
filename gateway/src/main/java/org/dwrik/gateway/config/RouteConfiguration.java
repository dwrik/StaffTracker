package org.dwrik.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator buildRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("department-service", r -> r.path("/departments/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://department"))
                .route("employee-service", r -> r.path("/employees/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://employee"))
                .route("eureka-dashboard", r -> r.path("/eureka")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://eureka"))
                .route("eureka", r -> r.path("/eureka/**")
                        .uri("lb://eureka"))
                .build();
    }

}
