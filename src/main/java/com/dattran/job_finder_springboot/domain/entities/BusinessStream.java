package com.dattran.job_finder_springboot.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @ManyToMany(mappedBy = "businessStreams")
    Set<Company> companies;
}
