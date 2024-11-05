package com.dattran.job_finder_springboot.domain.exceptions;

import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AppException.class})
    ApiResponse<Void> handlingAppException(AppException exception, HttpServletRequest httpServletRequest) {
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
    }
}
