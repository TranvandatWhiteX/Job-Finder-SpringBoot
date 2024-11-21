package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.JobPostDto;
import com.dattran.job_finder_springboot.domain.entities.JobPost;
import com.dattran.job_finder_springboot.domain.services.JobPostService;
import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/jobs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JobPostController {
    JobPostService jobPostService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<JobPost> postJob(@RequestBody @Valid JobPostDto jobPostDto, HttpServletRequest httpServletRequest) {
        JobPost jobPost = jobPostService.postJob(jobPostDto, httpServletRequest);
        return ApiResponse.<JobPost>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(jobPost)
                .status(HttpStatus.CREATED)
                .message("Job Created Successfully!")
                .build();
    }

    // Todo: Filter Job

    // Todo: Update Job

    // Todo: Delete Job

    // Todo: Get By Id
}
