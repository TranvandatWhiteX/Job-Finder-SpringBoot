package com.dattran.job_finder_springboot.domain.entities.mongo;

import com.dattran.job_finder_springboot.domain.enums.UploadType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "storages")
public class Storage {
    String url;

    String userId;

    String bucket;

    UploadType type;

    State state;

    LocalDateTime createdAt;

    public enum State {
        PUBLIC,
        DELETED,
        BLOCK,
    }
}
