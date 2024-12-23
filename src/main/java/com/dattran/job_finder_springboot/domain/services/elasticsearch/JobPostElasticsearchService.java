//package com.dattran.job_finder_springboot.domain.services.elasticsearch;
//
//import com.dattran.job_finder_springboot.app.dtos.JobSearchDto;
//import com.dattran.job_finder_springboot.domain.entities.JobPost;
//import com.dattran.job_finder_springboot.domain.entities.elasticsearch.JobPostSearch;
//import com.dattran.job_finder_springboot.domain.repositories.JobPostRepository;
//import com.dattran.job_finder_springboot.domain.repositories.elasticsearch.JobPostElasticsearchRepository;
//import com.dattran.job_finder_springboot.domain.utils.FnCommon;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class JobPostElasticsearchService {
//    JobPostElasticsearchRepository postElasticsearchRepository;
//    JobPostRepository jobPostRepository;
//    RestHighLevelClient client;
//
//    public void syncDataToElasticsearch() {
//        int pageSize = 1000;
//        int pageNumber = 0;
//        Page<JobPost> page;
//        List<CompletableFuture<Void>> futures = new ArrayList<>();
//        do {
//           page = jobPostRepository.findAll(PageRequest.of(pageNumber, pageSize));
//           List<JobPost> jobPosts = page.getContent();
//            CompletableFuture<Void> future = processChunkAsync(jobPosts);
//            futures.add(future);
//        } while (page.hasNext());
//        // Đợi tất cả các tác vụ không đồng bộ hoàn thành
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//    }
//
//    @Async
//    public CompletableFuture<Void> processChunkAsync(List<JobPost> jobPosts) {
//        List<JobPostSearch> jobPostSearches =
//                jobPosts.stream().map(this::convertToJobPostSearch).toList();
//        try {
//            postElasticsearchRepository.saveAll(jobPostSearches);
//        } catch (Exception e) {
//            log.error("Error while synchronizing products to Elasticsearch: {}", e.getMessage());
//        }
//        return CompletableFuture.completedFuture(null);
//    }
//
//    private JobPostSearch convertToJobPostSearch(JobPost jobPost) {
//        return FnCommon.copyNonNullProperties(JobPostSearch.class, jobPost);
//    }
//
//    public Page<JobPostSearch> searchJob(JobSearchDto jobSearchDto, Pageable pageable) {
//        return null;
//    }
//}
