package com.dattran.job_finder_springboot.domain.entities;

import com.dattran.job_finder_springboot.domain.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_job_application_status")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    ApplicationStatus status;
}
