package com.dattran.job_finder_springboot.domain.jobs;

import com.dattran.job_finder_springboot.config.AwsProperties;
import com.dattran.job_finder_springboot.domain.entities.mongo.Storage;
import com.dattran.job_finder_springboot.domain.repositories.mongo.StorageRepository;
import com.dattran.job_finder_springboot.domain.services.StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class DeletedFileS3Job {
  StorageService storageService;
  AwsProperties awsProperties;
  StorageRepository storageRepository;

  // Run on 12:00 PM
  @Scheduled(cron = "0 0 12 * * ?", zone = "Asia/Ho_Chi_Minh")
  public void runDailyTask() {
      int pageSize = 1000;
      int pageNumber = 0;
      Page<Storage> page;
      List<CompletableFuture<Void>> futures = new ArrayList<>();
      do {
          page = storageRepository.findByIsDeleted(true, PageRequest.of(pageNumber++, pageSize));
          List<Storage> storages = page.getContent();
          CompletableFuture<Void> future = processChunkAsync(storages);
          futures.add(future);
      } while (page.hasNext());
      // Đợi tất cả các tác vụ không đồng bộ hoàn thành
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }

  @Async
  public CompletableFuture<Void> processChunkAsync(List<Storage> storages) {
    List<ObjectIdentifier> keys =
        storages.stream()
            .map(storage -> ObjectIdentifier.builder().key(storage.getFileName()).build())
            .collect(Collectors.toList());
    storageService.deleteMultipleObjectFromBucket(awsProperties.getS3BucketName(), keys);
    return CompletableFuture.completedFuture(null);
  }

  // Run every 5s
  @Scheduled(fixedRate = 5000)
  public void runTaskEvery5Seconds() {
//    log.info("Schedule run task every 5 seconds");
  }

  @Scheduled(fixedDelay = 5000)
  public void runTaskAfter5SecondsDelay() {
//    System.out.println("Chạy sau mỗi 5 giây kể từ khi tác vụ trước kết thúc");
  }
}
