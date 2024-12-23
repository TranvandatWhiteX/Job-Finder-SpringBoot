package com.dattran.job_finder_springboot.domain.repositories;

import com.dattran.job_finder_springboot.domain.entities.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, String> {
    List<JobPostActivity> findByUserId(String userId);

    @Query(value = """
        WITH months AS (
            SELECT generate_series(
                date_trunc('year', to_date(:year, 'YYYY')), 
                date_trunc('year', to_date(:year, 'YYYY')) + interval '11 months',
                interval '1 month'
            ) AS month
        )
        SELECT 
            to_char(m.month, 'YYYY-MM') AS month, 
            COALESCE(COUNT(a.id), 0) AS total_applications
        FROM 
            months m
        LEFT JOIN 
            tbl_job_post_activities a 
        ON 
            date_trunc('month', a.apply_date) = m.month
        GROUP BY 
            m.month
        ORDER BY 
            m.month;
    """, nativeQuery = true)
    List<Object[]> getApplicationsCountByYear(@Param("year") String year);
}
