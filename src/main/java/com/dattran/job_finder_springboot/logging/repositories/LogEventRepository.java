package com.dattran.job_finder_springboot.logging.repositories;

import com.dattran.job_finder_springboot.logging.entities.LogEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogEventRepository extends MongoRepository<LogEvent, String> {}
