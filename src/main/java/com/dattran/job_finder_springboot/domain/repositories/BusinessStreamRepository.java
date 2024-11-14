package com.dattran.job_finder_springboot.domain.repositories;

import com.dattran.job_finder_springboot.domain.entities.BusinessStream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessStreamRepository extends JpaRepository<BusinessStream, String> {
    Optional<BusinessStream> findByCode(Long code);
}
