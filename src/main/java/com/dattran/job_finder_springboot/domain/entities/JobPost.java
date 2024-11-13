package com.dattran.job_finder_springboot.domain.entities;

import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_job_posts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "expired_date")
    LocalDate expiredDate;

    @Column(name = "job_title")
    String jobTitle;

    Boolean isActive;

    @Column(name = "job_requirement", columnDefinition = "TEXT", nullable = false)
    String jobRequirement;

    @Column(name = "job_description", columnDefinition = "TEXT", nullable = false)
    String jobDescription;

    @Column(columnDefinition = "TEXT NOT NULL")
    String responsibilities;

    @Column(name = "logo_url")
    String logoUrl;

    String location;

    @Column(name = "full_location")
    String fullLocation;

    @Column(name = "job_level")
    JobLevel jobLevel;

    @Column(name = "job_type")
    JobType jobType;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;
}
