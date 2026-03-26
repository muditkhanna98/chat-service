package com.mudit.chatservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    public static final String REQUEST_ATTRIBUTE_CURRENT_USER = "currentUser";
    public static final String HEADER_GATEWAY_SECRET = "X-Gateway-Secret";
    public static final String HEADER_USER_SUB = "X-User-Sub";
    public static final String HEADER_USER_EMAIL = "X-User-Email";
    public static final String HEADER_USER_NAME = "X-User-Name";

    private static final List<String> PUBLIC_PATHS = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/health"
    );

    private final InternalAuthProperties internalAuthProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public GatewayAuthenticationFilter(InternalAuthProperties internalAuthProperties) {
        this.internalAuthProperties = internalAuthProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String path = request.getServletPath();
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String gatewaySecret = request.getHeader(HEADER_GATEWAY_SECRET);
        if (!StringUtils.hasText(gatewaySecret) || !gatewaySecret.equals(internalAuthProperties.gatewaySecret())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid gateway credentials");
            return;
        }

        String sub = request.getHeader(HEADER_USER_SUB);
        if (!StringUtils.hasText(sub)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing user identity");
            return;
        }

        CurrentUser currentUser = new CurrentUser(
                sub,
                request.getHeader(HEADER_USER_EMAIL),
                request.getHeader(HEADER_USER_NAME)
        );
        request.setAttribute(REQUEST_ATTRIBUTE_CURRENT_USER, currentUser);

        filterChain.doFilter(request, response);
    }
}
