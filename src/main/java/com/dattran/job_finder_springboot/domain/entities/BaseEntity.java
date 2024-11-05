package com.dattran.job_finder_springboot.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {
  @CreationTimestamp
  @Column(name = "created_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime updatedAt;
}
