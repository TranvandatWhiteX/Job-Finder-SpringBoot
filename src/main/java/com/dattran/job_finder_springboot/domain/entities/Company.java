package com.dattran.job_finder_springboot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tbl_companies")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    @Column(name = "tax_identification_number")
    String taxIdentificationNumber;

    @Column(columnDefinition = "TEXT", nullable = false)
    String description;

    @Column(name = "website_url")
    String websiteUrl;

    @Column(name = "founded_date")
    LocalDate foundedDate;

    @Column(name = "number_of_employees")
    Long numberOfEmployees;

    @Column(name = "head_quarters")
    String headQuarters;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_rel_company_business",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "business_id"))
    Set<BusinessStream> businessStreams;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    Set<Asset> assets;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    Set<JobPost> jobPosts;
}
