package com.dattran.job_finder_springboot.domain.runners;

import com.dattran.job_finder_springboot.domain.entities.JobSkill;
import com.dattran.job_finder_springboot.domain.repositories.JobSkillRepository;
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
public class JobSkillRunner implements CommandLineRunner {
  JobSkillRepository jobSkillRepository;

  @Override
  public void run(String... args) throws Exception {
    if (jobSkillRepository.count() == 0) {
      List<JobSkill> jobSkills =
          List.of(
              JobSkill.builder()
                  .name("Java")
                  .code(2014L)
                  .description(
                      "Java là một ngôn ngữ lập trình hướng đối tượng, dựa trên lớp được thiết kế để có càng ít phụ thuộc thực thi càng tốt.")
                  .build());
      jobSkillRepository.saveAll(jobSkills);
    }
  }
}
