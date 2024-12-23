package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.ApplicationsByMonth;
import com.dattran.job_finder_springboot.domain.services.JobPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatisticsController {
    JobPostService jobPostService;

    @GetMapping
    public List<ApplicationsByMonth> getStatisticsByYear(
            @RequestParam String year) {
        return jobPostService.getApplicationsCountByYear(year);
    }
}
