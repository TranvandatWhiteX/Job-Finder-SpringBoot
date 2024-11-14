package com.dattran.job_finder_springboot.domain.entities;

import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;

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

    @Column(columnDefinition = "TEXT")
    String benefit;

    @Column(name = "logo_url")
    String logoUrl;

    String location;

    @Type(JsonBinaryType.class)
    @Column(name = "job_levels", columnDefinition = "jsonb")
    List<JobLevel> jobLevels;

    @Type(JsonBinaryType.class)
    @Column(name = "job_types", columnDefinition = "jsonb")
    List<JobType> jobTypes;

    @ManyToMany(targetEntity = JobSkill.class, fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "tbl_rel_job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    List<JobSkill> jobSkills;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Address address;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "company_id")
    Company company;
}
