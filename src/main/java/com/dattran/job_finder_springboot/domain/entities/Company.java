package com.dattran.job_finder_springboot.domain.entities;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;
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

    @Column(name = "international_name")
    String internationalName;

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

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    List<Address> addresses;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Asset asset;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_rel_company_business",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "business_id"))
    List<BusinessStream> businessStreams;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    Set<JobPost> jobPosts;
}
