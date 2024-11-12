package com.dattran.job_finder_springboot.logging.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "log_exception")
public class LogException {
    Integer status;
    String message;
    String messageCode;
    String description;
    String path;
    Object params;
    Object body;
    Object headers;
    LocalDateTime createdAt;
}
