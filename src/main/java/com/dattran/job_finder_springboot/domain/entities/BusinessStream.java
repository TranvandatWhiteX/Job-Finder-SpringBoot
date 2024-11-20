package com.dattran.job_finder_springboot.domain.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "tbl_business_streams")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BusinessStream extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    String description;

    Long code;

    @JsonIgnore
    @ManyToMany(mappedBy = "businessStreams", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Company> companies;

    @JsonIgnore
    @OneToMany(mappedBy = "businessStream", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<JobPost> jobPosts;
}
