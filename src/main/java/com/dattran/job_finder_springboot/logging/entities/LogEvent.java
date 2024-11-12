package com.dattran.job_finder_springboot.logging.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "log_event")
public class LogEvent {
    String userId;
    String ip;
    Boolean isSystem = false;
    LogAction action;
    String objectName;
    Object preValue;
    Object value;
    LocalDateTime createdAt;
}
