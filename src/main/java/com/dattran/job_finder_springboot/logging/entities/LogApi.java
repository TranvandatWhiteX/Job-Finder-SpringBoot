package com.dattran.job_finder_springboot.logging.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "log_api")
public class LogApi {
    String url;
    HttpMethod method;
    Object auth;
    Object body;
    Object response;
    HttpStatus status;
    LocalDateTime createdAt;
}
