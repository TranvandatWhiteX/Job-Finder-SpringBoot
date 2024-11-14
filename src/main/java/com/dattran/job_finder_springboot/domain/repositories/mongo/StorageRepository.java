package com.dattran.job_finder_springboot.domain.repositories.mongo;

import com.dattran.job_finder_springboot.domain.entities.mongo.Storage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageRepository extends MongoRepository<Storage, String> {}
