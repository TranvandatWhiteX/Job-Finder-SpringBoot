package com.dattran.job_finder_springboot.domain.entities.mongo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "notifications")
public class Notification {
    String senderId;

    String senderName;

    String receiverId;

    String message;

    Boolean isRead;

    LocalDateTime createdAt;

    Boolean isDeleted;
}
