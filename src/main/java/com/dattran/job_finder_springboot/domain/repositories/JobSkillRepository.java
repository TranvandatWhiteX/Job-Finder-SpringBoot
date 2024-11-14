package com.dattran.job_finder_springboot.domain.repositories;

import com.dattran.job_finder_springboot.domain.entities.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSkillRepository extends JpaRepository<JobSkill, String> {
    Optional<JobSkill> findByCode(Long code);
}
