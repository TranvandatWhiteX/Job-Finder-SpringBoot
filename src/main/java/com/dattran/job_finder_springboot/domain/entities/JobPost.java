package com.dattran.job_finder_springboot.domain.entities;

import com.dattran.job_finder_springboot.domain.enums.BusinessType;
import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_job_posts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Enumerated(EnumType.STRING)
    JobLevel jobLevel;

    @Enumerated(EnumType.STRING)
    JobType jobType;

    @Type(JsonBinaryType.class)
    @Column(name = "salary", columnDefinition = "jsonb")
    Salary salary;

    @Column(name = "number_requirement")
    Long numberRequirement;

    Long experience;

    @Column(columnDefinition = "TEXT", nullable = false)
    String skills;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "company_id")
    @JsonIgnore
    Company company;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "business_id")
            @JsonIgnore
    BusinessStream businessStream;
}
