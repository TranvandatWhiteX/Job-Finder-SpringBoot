package com.dattran.job_finder_springboot.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyJobDto {
    String jobId;

    String userId;

    String fullName;

    String description;

    String resumeLink;
}
