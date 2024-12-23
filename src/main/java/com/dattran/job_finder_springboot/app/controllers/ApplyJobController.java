package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.ApplyJobDto;
import com.dattran.job_finder_springboot.app.responses.LoginResponse;
import com.dattran.job_finder_springboot.domain.entities.JobPostActivity;
import com.dattran.job_finder_springboot.domain.services.ApplyJobService;
import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/apply")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ApplyJobController {
    ApplyJobService applyJobService;

    @PostMapping
    public ApiResponse<JobPostActivity> applyJob(@RequestBody ApplyJobDto applyJobDto, HttpServletRequest httpServletRequest) {
        JobPostActivity response = applyJobService.applyJob(applyJobDto);
        return ApiResponse.<JobPostActivity>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(response)
                .status(HttpStatus.OK)
                .message("Apply Successfully!")
                .build();
    }

    @GetMapping
    public ApiResponse<List<JobPostActivity>> getAll(@RequestBody String userId, HttpServletRequest httpServletRequest) {
        List<JobPostActivity> jobPostActivities = applyJobService.getAllJobPostActivities(userId);
        return ApiResponse.<List<JobPostActivity>>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(jobPostActivities)
                .status(HttpStatus.OK)
                .message("Apply Successfully!")
                .build();
    }
}
