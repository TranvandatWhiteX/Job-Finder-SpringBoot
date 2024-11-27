package com.dattran.job_finder_springboot.domain.repositories.elasticsearch;

import com.dattran.job_finder_springboot.domain.entities.elasticsearch.JobPostSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobPostElasticsearchRepository extends ElasticsearchRepository<JobPostSearch, String> {}
