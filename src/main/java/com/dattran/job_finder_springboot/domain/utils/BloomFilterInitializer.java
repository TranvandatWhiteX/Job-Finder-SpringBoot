package com.dattran.job_finder_springboot.domain.utils;

import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.bloomfilter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
@Data
public class BloomFilterInitializer {
    private final UserRepository userRepository;
    private BloomFilter bloomFilter;
    private final Shape shape;
    int PAGE_SIZE = 100;

    @Autowired
    public BloomFilterInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.shape = Shape.fromNP(100000000, 0.001);
    }

    @PostConstruct
    public void init() {
        this.bloomFilter = new SimpleBloomFilter(shape);
        initializeBloomFilter();
    }

    public void initializeBloomFilter() {
        long totalRecords = userRepository.count();
        if (totalRecords > 0) {
            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
            CountDownLatch latch = new CountDownLatch(totalPages);
            for (int i = 0; i < totalPages; i++) {
                log.info("Load to Bloom Filter, Page: "+i);
                loadPageIntoBloomFilter(i, latch);
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Async
    public void loadPageIntoBloomFilter(int pageNumber, CountDownLatch latch) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            Page<User> usersPage = userRepository.findAll(pageable);
            List<User> users = usersPage.getContent();
            for (User user : users) {
                addEmailToBloomFilter(user.getEmail());
            }
            log.info("Processed page: " + pageNumber);
        } finally {
            latch.countDown();
        }
    }

    public void addEmailToBloomFilter(String email) {
        Hasher hasher = new EnhancedDoubleHasher(email.getBytes(StandardCharsets.UTF_8));
        bloomFilter.merge(hasher);
    }
}
