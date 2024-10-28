package com.bank.gateway.configuration;

import com.bank.gateway.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpHeaders;

import java.util.List;

@RefreshScope
@Component
@Log4j2
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    public static class Config {
        private String requiredRole;

        public String getRequiredRole() {
            return requiredRole;
        }

        public void setRequiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("SecureCodeBank ")) {
                log.warn("Missing or invalid Authorization header.");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            String token = authHeader.substring(15);

            try {
                jwtUtil.isInvalid(token); // Token'ı kontrol et
            } catch (Exception e) {
                log.warn("Invalid token: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // Burada kimlik doğrulama ve rol kontrolü yapmalısınız
            List<String> roles = getUserRolesFromHeader(token);
            String requiredRole = config.getRequiredRole();

            if (roles == null || !roles.contains(requiredRole)) {
                log.warn("Unauthorized access attempt to the service.");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            log.info("Access granted for user with roles: {}", roles);
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-UUID", getUserUUIDFromHeader(token))
                    .build();

                // Yeni isteği kullanarak filter chain devam ediyor
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

            //return chain.filter(exchange);
        };
    }

    private List<String> getUserRolesFromHeader(String exchange) {
        return jwtUtil.extractRoles(exchange);
    }

    private String getUserUUIDFromHeader(String exchange) {
        return jwtUtil.extractUUID(exchange);
    }

    @Override
    public String name() {
        return "AuthenticationFilter";
    }
}
