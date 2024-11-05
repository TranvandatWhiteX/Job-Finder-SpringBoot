package com.dattran.job_finder_springboot.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "tbl_assets")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Asset extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String path;

    @Column(name = "is_deleted")
    Boolean isDeleted;

    Long size;

    String type;

    @Column(name = "created_by")
    String createdBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}

