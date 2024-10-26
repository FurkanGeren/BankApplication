package com.bank.gateway.configuration;

import com.bank.gateway.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
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
            // Burada kimlik doğrulama ve rol kontrolü yapmalısınız
            List<String> roles = getUserRolesFromHeader(exchange); // Kullanıcının rollerini alın
            String requiredRole = config.getRequiredRole();

            if (roles == null || !roles.contains(requiredRole)) {
                log.warn("Unauthorized access attempt to the service.");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            log.info("Access granted for user with roles: {}", roles);
            return chain.filter(exchange);
        };
    }

    private List<String> getUserRolesFromHeader(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return jwtUtil.extractRoles(authHeader);
    }

    @Override
    public String name() {
        return "AuthenticationFilter";
    }
}
