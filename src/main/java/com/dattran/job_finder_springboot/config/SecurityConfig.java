package com.dattran.job_finder_springboot.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    JwtAuthenticationFilter jwtAuthenticationFilter;

    String[] PUBLIC_ENDPOINTS_POST = {
            "/users",
            "/users/verify",
            "/users/forgot-password",
            "/users/verify-pass",
            "/auth/login"
    };

    String[] PUBLIC_ENDPOINTS_GET = {
//            Swagger
            "/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/swagger-ui/**",
            "/swagger-ui/index.html",
            "/jobs/search"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()
                        .anyRequest().authenticated());
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);
        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
