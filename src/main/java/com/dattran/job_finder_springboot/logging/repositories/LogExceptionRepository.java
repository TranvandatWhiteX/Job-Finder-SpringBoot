package com.dattran.job_finder_springboot.logging.repositories;

import com.dattran.job_finder_springboot.logging.entities.LogException;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogExceptionRepository extends MongoRepository<LogException, String> {}
