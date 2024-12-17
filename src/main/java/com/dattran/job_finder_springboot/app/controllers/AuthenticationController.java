package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.LoginDto;
import com.dattran.job_finder_springboot.app.responses.LoginResponse;
import com.dattran.job_finder_springboot.domain.services.AuthService;
import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginDto loginDto,
                                                 HttpServletRequest httpServletRequest) {
        LoginResponse response = authService.login(loginDto);
        return ApiResponse.<LoginResponse>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(response)
                .status(HttpStatus.OK)
                .message("Login Successfully!")
                .build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<Void> logout(HttpServletRequest httpServletRequest) {
        authService.logout();
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .message("Logout Successfully!")
                .build();
    }

    @PostMapping("/refresh-token")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody String refreshToken, HttpServletRequest httpServletRequest) {
        LoginResponse response = authService.refreshToken(refreshToken);
        return ApiResponse.<LoginResponse>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(response)
                .status(HttpStatus.OK)
                .message("Refresh Token Successfully!")
                .build();
    }
}
