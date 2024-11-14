package com.dattran.job_finder_springboot.domain.exceptions;

import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import com.dattran.job_finder_springboot.logging.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {
    LoggingService loggingService;

    @ExceptionHandler({Exception.class})
    ResponseEntity<ApiResponse<Void>> handlingException(Exception exception, HttpServletRequest httpServletRequest) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        loggingService
                .writeLogException(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), "Server Error!", httpServletRequest);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler({AppException.class})
    ResponseEntity<ApiResponse<Void>> handlingException(AppException exception, HttpServletRequest httpServletRequest) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
