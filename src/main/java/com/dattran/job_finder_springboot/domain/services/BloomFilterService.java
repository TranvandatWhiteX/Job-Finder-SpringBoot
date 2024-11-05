package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.domain.utils.BloomFilterInitializer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.bloomfilter.EnhancedDoubleHasher;
import org.apache.commons.collections4.bloomfilter.Hasher;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BloomFilterService {
    BloomFilterInitializer bloomFilterInitializer;

    public boolean isEmailExisted(String email) {
        Hasher hasher =
                new EnhancedDoubleHasher(email.getBytes(StandardCharsets.UTF_8));
        return bloomFilterInitializer.getBloomFilter().contains(hasher);
    }

    public void addEmailToBloomFilter(String email) {
        Hasher hasher = new EnhancedDoubleHasher(email.getBytes(StandardCharsets.UTF_8));
        bloomFilterInitializer.getBloomFilter().merge(hasher);
        log.info("Email added to bloom filter: " + email);
    }
}
