package com.dattran.job_finder_springboot.domain.repositories;

import com.dattran.job_finder_springboot.domain.entities.JobPost;
import com.dattran.job_finder_springboot.domain.enums.BusinessType;
import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, String> {
  Page<JobPost> findByCompanyId(String companyId, Pageable pageable);

  @Query(
      value =
          """
    SELECT *
    FROM tbl_job_posts
    WHERE (:provinceCode IS NULL OR address->>'provinceCode' = :provinceCode)
      OR (:jobTitle IS NULL OR job_title ILIKE %:jobTitle%)
      OR (:experience IS NULL OR experience <= :experience)
      OR (:minSalary IS NULL OR (salary->>'minSalary')::INT >= :minSalary)
      OR (:maxSalary IS NULL OR (salary->>'maxSalary')::INT <= :maxSalary)
""",
      nativeQuery = true)
  List<JobPost> searchJobs(
      @Param("provinceCode") String provinceCode,
      @Param("jobTitle") String jobTitle,
      @Param("experience") long experience,
      @Param("minSalary") long minSalary,
      @Param("maxSalary") long maxSalary);
}

