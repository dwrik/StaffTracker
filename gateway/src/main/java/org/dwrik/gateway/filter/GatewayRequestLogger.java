package org.dwrik.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class GatewayRequestLogger implements GlobalFilter {

    @Order(-1)
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            var request = exchange.getRequest();
            var response = exchange.getResponse();

            var method = request.getMethod();
            var path = request.getPath();
            var status = Objects.requireNonNull(response.getStatusCode()).value();
            var userAgent = request.getHeaders().get("User-Agent");

            var params = request.getURI().getQuery();
            params = params != null ? "?" + params : "";

            log.info("{} {} {} {} {}",
                    fixedWidth(method, 6),
                    fixedWidth(path + params, 30),
                    fixedWidth(status, 3),
                    fixedWidth(duration + "ms", 6),
                    userAgent
            );
        }));
    }

    private static String fixedWidth(Object object, int width) {
        return String.format("%-" + width + "s", object);
    }

}
