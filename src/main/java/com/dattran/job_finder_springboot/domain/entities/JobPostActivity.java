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

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    JobPost jobPost;
}
