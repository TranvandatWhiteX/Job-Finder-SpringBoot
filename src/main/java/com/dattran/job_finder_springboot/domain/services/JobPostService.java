package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.dtos.JobPostDto;
import com.dattran.job_finder_springboot.app.dtos.JobSearchDto;
import com.dattran.job_finder_springboot.domain.entities.Asset;
import com.dattran.job_finder_springboot.domain.entities.BusinessStream;
import com.dattran.job_finder_springboot.domain.entities.Company;
import com.dattran.job_finder_springboot.domain.entities.JobPost;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.repositories.BusinessStreamRepository;
import com.dattran.job_finder_springboot.domain.repositories.CompanyRepository;
import com.dattran.job_finder_springboot.domain.repositories.JobPostRepository;
import com.dattran.job_finder_springboot.domain.utils.FnCommon;
import com.dattran.job_finder_springboot.domain.utils.HttpRequestUtil;
import com.dattran.job_finder_springboot.logging.LoggingService;
import com.dattran.job_finder_springboot.logging.entities.LogAction;
import com.dattran.job_finder_springboot.logging.entities.ObjectName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPostService {
  JobPostRepository jobPostRepository;
  LoggingService loggingService;
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
    Optional.ofNullable(company.getAsset())
            .map(Asset::getLogo)
            .ifPresent(jobPost::setLogoUrl);
    JobPost savedJobPost = jobPostRepository.save(jobPost);
    // Handle StackOverflowError
    for (BusinessStream business : savedJobPost.getCompany().getBusinessStreams()) {
      business.setCompanies(null);
    }
    savedJobPost.getCompany().setJobPosts(null);
    savedJobPost.getBusinessStream().setJobPosts(null);
    // Logging
    loggingService.writeLogEvent(
        savedJobPost.getId(),
        LogAction.CREATE,
        HttpRequestUtil.getClientIp(httpServletRequest),
        ObjectName.JOB.name(),
        null,
        savedJobPost);
    return savedJobPost;
  }

//  public Page<JobPostSearch> searchJob(JobSearchDto jobSearchDto, Pageable pageable) {
//    return null;
//  }

  public JobPost getById(String id) {
    return jobPostRepository.findById(id).orElseThrow(() -> new AppException(ResponseStatus.JOB_NOT_FOUND));
  }

  public void unActiveJob(String id) {
    JobPost jobPost = getById(id);
    jobPost.setIsActive(false);
    jobPostRepository.save(jobPost);
  }

  public Page<JobPost> getAllJobsByCompany(String companyId, Pageable pageable) {
    return jobPostRepository.findByCompanyId(companyId, pageable);
  }

  public JobPost updatePost(String id, @Valid JobPostDto jobPostDto) {
    JobPost jobPost = getById(id);
    BusinessStream businessStream =
            businessStreamRepository
                    .findByCode(jobPostDto.getBusinessCode())
                    .orElseThrow(() -> new AppException(ResponseStatus.BUSINESS_STREAM_NOT_FOUND));
    if (!Objects.equals(businessStream.getCode(), jobPost.getBusinessStream().getCode())) {
      jobPost.setBusinessStream(businessStream);
    }
    FnCommon.copyProperties(jobPostDto, jobPost);
    return jobPostRepository.save(jobPost);
  }
}
