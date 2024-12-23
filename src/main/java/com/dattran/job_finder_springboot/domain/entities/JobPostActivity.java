package com.dattran.job_finder_springboot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_job_post_activities")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostActivity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "apply_date")
    LocalDateTime applyDate;

    String userId;

    String jobId;

    String fullName;

    String description;

    String resumeLink;
}
