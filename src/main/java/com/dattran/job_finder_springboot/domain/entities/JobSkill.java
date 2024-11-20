package com.dattran.job_finder_springboot.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_job_skills")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    String description;

    Long code;

    @JsonIgnore
    @ManyToMany(targetEntity = JobPost.class,
            fetch = FetchType.LAZY,
            mappedBy = "jobSkills",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    List<JobPost> jobPosts;
}
