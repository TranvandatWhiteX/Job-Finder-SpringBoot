package com.dattran.job_finder_springboot.domain.repositories;

import com.dattran.job_finder_springboot.domain.entities.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, String> {
    List<JobPostActivity> findByUserId(String userId);
}
