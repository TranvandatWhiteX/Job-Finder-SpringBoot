package com.dattran.job_finder_springboot.logging.repositories;

import com.dattran.job_finder_springboot.logging.entities.LogApi;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogApiRepository extends MongoRepository<LogApi, String> {}
