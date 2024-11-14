package com.dattran.job_finder_springboot.domain.runners;

import com.dattran.job_finder_springboot.domain.entities.BusinessStream;
import com.dattran.job_finder_springboot.domain.enums.BusinessType;
import com.dattran.job_finder_springboot.domain.repositories.BusinessStreamRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessStreamRunner implements CommandLineRunner {
  BusinessStreamRepository businessStreamRepository;

  @Override
  public void run(String... args) {
    if (businessStreamRepository.count() == 0) {
      List<BusinessStream> businessStreams =
          List.of(
              BusinessStream.builder()
                  .name(BusinessType.AUTOMOTIVE.name())
                  .code(1025L)
                  .description(
                      "Ngành công nghiệp ô tô thiết kế, phát triển, sản xuất, tiếp thị và bán tất cả các loại xe có động cơ.")
                  .build(),
              BusinessStream.builder()
                  .name(BusinessType.INFORMATION_TECHNOLOGY.name())
                  .code(1026L)
                  .description(
                      "Công nghệ thông tin là một nhánh ngành kỹ thuật sử dụng máy tính và phần mềm máy tính để chuyển đổi, lưu trữ, bảo vệ, xử lý, truyền tải và thu thập thông tin.")
                  .build(),
              BusinessStream.builder()
                  .name(BusinessType.WATCH_MAKING.name())
                  .description("Ngành công nghiệp chế tác đồng hồ")
                  .code(1027L)
                  .build());
      businessStreamRepository.saveAll(businessStreams);
    }
  }
}
