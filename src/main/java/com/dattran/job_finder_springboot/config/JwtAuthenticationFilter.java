package com.dattran.job_finder_springboot.config;

import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.services.JwtService;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtService jwtService;
    UserDetailsService userDetailsService;
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            if (isBypass(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            if (!jwtService.verifyToken(token, false)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
            JWTClaimsSet claims = jwtService.getAllClaimsFromToken(token);
            String email = (String) claims.getClaim("email");
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isBypass(HttpServletRequest request) {
        Map<String, Set<String>> bypassTokens = new HashMap<>();
        bypassTokens.put("/auth/login", Set.of("POST"));
        bypassTokens.put("/jobs/search", Set.of("GET"));
//        bypassTokens.put("/jobs/download-template", Set.of("GET"));
//        bypassTokens.put("/jobs/import", Set.of("POST"));
        bypassTokens.put("/users", Set.of("POST"));
        bypassTokens.put("/users/verify", Set.of("POST"));
        bypassTokens.put("/users/forgot-password", Set.of("POST"));
        bypassTokens.put("/users/verify-pass", Set.of("POST"));
        bypassTokens.put("/api-docs", Set.of("GET"));
        bypassTokens.put("/v3/api-docs/**", Set.of("GET"));
        bypassTokens.put("/swagger-resources", Set.of("GET"));
        bypassTokens.put("/swagger-resources/**", Set.of("GET"));
        bypassTokens.put("/configuration/ui", Set.of("GET"));
        bypassTokens.put("/configuration/security", Set.of("GET"));
        bypassTokens.put("/swagger-ui/**", Set.of("GET"));
        bypassTokens.put("/swagger-ui.html", Set.of("GET"));
        bypassTokens.put("/webjars/swagger-ui/**", Set.of("GET"));
        bypassTokens.put("/swagger-ui/index.html", Set.of("GET"));
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        for (Map.Entry<String, Set<String>> entry : bypassTokens.entrySet()) {
            String pathPattern = entry.getKey();
            Set<String> methods = entry.getValue();
            if (pathMatcher.match(pathPattern, requestPath) && methods.contains(requestMethod)) {
                return true;
            }
        }
        return false;
    }
}
