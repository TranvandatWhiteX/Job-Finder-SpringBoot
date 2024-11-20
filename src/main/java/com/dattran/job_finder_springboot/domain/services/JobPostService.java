package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.dtos.JobPostDto;
import com.dattran.job_finder_springboot.domain.entities.BusinessStream;
import com.dattran.job_finder_springboot.domain.entities.Company;
import com.dattran.job_finder_springboot.domain.entities.JobPost;
import com.dattran.job_finder_springboot.domain.entities.JobSkill;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.repositories.BusinessStreamRepository;
import com.dattran.job_finder_springboot.domain.repositories.CompanyRepository;
import com.dattran.job_finder_springboot.domain.repositories.JobPostRepository;
import com.dattran.job_finder_springboot.domain.repositories.JobSkillRepository;
import com.dattran.job_finder_springboot.domain.utils.FnCommon;
import com.dattran.job_finder_springboot.domain.utils.HttpRequestUtil;
import com.dattran.job_finder_springboot.logging.LoggingService;
import com.dattran.job_finder_springboot.logging.entities.LogAction;
import com.dattran.job_finder_springboot.logging.entities.ObjectName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPostService {
  JobPostRepository jobPostRepository;
  LoggingService loggingService;
  JobSkillRepository jobSkillRepository;
  CompanyRepository companyRepository;
  BusinessStreamRepository businessStreamRepository;

  public JobPost postJob(JobPostDto jobPostDto, HttpServletRequest httpServletRequest) {
    Company company =
        companyRepository
            .findById(jobPostDto.getCompanyId())
            .orElseThrow(() -> new AppException(ResponseStatus.COMPANY_NOT_FOUND));
    BusinessStream businessStream =
        businessStreamRepository
            .findByCode(jobPostDto.getBusinessCode())
            .orElseThrow(() -> new AppException(ResponseStatus.BUSINESS_STREAM_NOT_FOUND));
    JobPost jobPost = FnCommon.copyNonNullProperties(JobPost.class, jobPostDto);
    assert jobPost != null;
    jobPost.setCompany(company);
    jobPost.setBusinessStream(businessStream);
    jobPost.setIsActive(true);
    StringBuilder sb = new StringBuilder();
    sb.append(jobPostDto.getAddress().getDetail())
        .append(", ")
        .append(jobPostDto.getAddress().getWard())
        .append(", ")
        .append(jobPostDto.getAddress().getDistrict())
        .append(", ")
        .append(jobPostDto.getAddress().getProvince())
        .append(".");
    jobPost.setLocation(sb.toString());
    // Todo: Add logo from company logo
    List<JobSkill> jobSkills = new ArrayList<>();
    for (Long skillCode : jobPostDto.getSkillCodes()) {
      JobSkill jobSkill =
          jobSkillRepository
              .findByCode(skillCode)
              .orElseThrow(() -> new AppException(ResponseStatus.SKILL_NOT_FOUND));
      jobSkills.add(jobSkill);
    }
    jobPost.setJobSkills(jobSkills);
    JobPost savedJobPost = jobPostRepository.save(jobPost);
    // Handle StackOverflowError
    for (BusinessStream business : savedJobPost.getCompany().getBusinessStreams()) {
      business.setCompanies(null);
    }
    savedJobPost.getCompany().setJobPosts(null);
    for (JobSkill jobSkill : savedJobPost.getJobSkills()) {
      jobSkill.setJobPosts(null);
    }
    savedJobPost.getBusinessStream().setJobPosts(null);
    // Logging
    // Todo: Fix stackoverflow
    loggingService.writeLogEvent(
        savedJobPost.getId(),
        LogAction.CREATE,
        HttpRequestUtil.getClientIp(httpServletRequest),
        ObjectName.JOB.name(),
        null,
        savedJobPost);
    return savedJobPost;
  }
}
