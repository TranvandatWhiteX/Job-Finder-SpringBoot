package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.dtos.ApplyJobDto;
import com.dattran.job_finder_springboot.domain.entities.JobPost;
import com.dattran.job_finder_springboot.domain.entities.JobPostActivity;
import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.repositories.JobPostActivityRepository;
import com.dattran.job_finder_springboot.domain.repositories.JobPostRepository;
import com.dattran.job_finder_springboot.domain.repositories.UserRepository;
import com.dattran.job_finder_springboot.domain.utils.FnCommon;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplyJobService {
    JobPostActivityRepository jobPostActivityRepository;
    JobPostRepository jobPostRepository;
    UserRepository userRepository;

    public JobPostActivity applyJob(ApplyJobDto applyJobDto) {
        JobPost jobPost =
            jobPostRepository
                    .findById(applyJobDto.getJobId())
                    .orElseThrow(() -> new AppException(ResponseStatus.JOB_NOT_FOUND));
        User user = userRepository
                .findById(applyJobDto.getUserId())
                .orElseThrow(() -> new AppException(ResponseStatus.USER_NOT_FOUND));
        JobPostActivity jobPostActivity = FnCommon.copyNonNullProperties(JobPostActivity.class, applyJobDto);
        assert jobPostActivity != null;
        jobPostActivity.setApplyDate(LocalDateTime.now());
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<JobPostActivity> getAllJobPostActivities(String userId) {
        return jobPostActivityRepository.findByUserId(userId);
    }
}
