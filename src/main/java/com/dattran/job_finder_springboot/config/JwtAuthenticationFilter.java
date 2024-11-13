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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtService jwtService;
    UserDetailsService userDetailsService;

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

    private boolean isBypass(@NonNull HttpServletRequest request) {
        Map<String, Set<String>> bypassTokens = Map.of(
                "/auth/login", Set.of("POST"),
                "/users", Set.of("POST"),
                "/users/verify", Set.of("POST"),
                "/users/forgot-password", Set.of("POST"),
                "/users/verify-pass", Set.of("POST")
        );
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        return bypassTokens.containsKey(requestPath) && bypassTokens.get(requestPath).contains(requestMethod);
    }
}
