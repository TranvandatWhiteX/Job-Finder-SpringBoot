package com.dattran.job_finder_springboot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_favorite_jobs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteJob extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "user_id")
    String userId;

    @Column(name = "job_id")
    String jobId;
}
