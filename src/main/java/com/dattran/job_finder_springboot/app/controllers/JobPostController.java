package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.JobPostDto;
import com.dattran.job_finder_springboot.app.dtos.JobSearchDto;
import com.dattran.job_finder_springboot.domain.entities.JobPost;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.services.ExcelService;
import com.dattran.job_finder_springboot.domain.services.JobPostService;
import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/jobs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JobPostController {
  JobPostService jobPostService;
  ExcelService excelService;

  @PostMapping
//  @PreAuthorize("hasRole('RECRUITER')")
  public ApiResponse<JobPost> postJob(
      @RequestBody @Valid JobPostDto jobPostDto, HttpServletRequest httpServletRequest) {
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

  @GetMapping("/search")
  public ApiResponse<List<JobPost>> searchJob(
      @RequestBody @Valid JobSearchDto jobSearchDto,
      HttpServletRequest httpServletRequest) {
    List<JobPost> jobPosts = jobPostService.searchJob(jobSearchDto);
    return ApiResponse.<List<JobPost>>builder()
        .timestamp(LocalDateTime.now().toString())
        .path(httpServletRequest.getRequestURI())
        .requestMethod(httpServletRequest.getMethod())
        .result(jobPosts)
        .status(HttpStatus.OK)
        .message("Search Jobs Successfully!")
        .build();
  }

  @PutMapping("/{id}")
//  @PreAuthorize("hasRole('RECRUITER')")
  public ApiResponse<JobPost> updateJob(
      @PathVariable("id") String id,
      @RequestBody @Valid JobPostDto jobPostDto,
      HttpServletRequest httpServletRequest) {
    JobPost jobPost = jobPostService.updatePost(id, jobPostDto);
    return ApiResponse.<JobPost>builder()
        .timestamp(LocalDateTime.now().toString())
        .path(httpServletRequest.getRequestURI())
        .requestMethod(httpServletRequest.getMethod())
        .result(jobPost)
        .status(HttpStatus.OK)
        .message("Job Updated Successfully!")
        .build();
  }

  @DeleteMapping("/{id}")
//  @PreAuthorize("hasRole('RECRUITER')")
  public ApiResponse<Void> unActiveJob(
      @PathVariable String id, HttpServletRequest httpServletRequest) {
    jobPostService.unActiveJob(id);
    return ApiResponse.<Void>builder()
        .timestamp(LocalDateTime.now().toString())
        .path(httpServletRequest.getRequestURI())
        .requestMethod(httpServletRequest.getMethod())
        .status(HttpStatus.OK)
        .message("Un-Active Job Successfully!")
        .build();
  }

  @GetMapping("/{id}")
  public ApiResponse<JobPost> getJob(
      @PathVariable String id, HttpServletRequest httpServletRequest) {
    JobPost job = jobPostService.getById(id);
    return ApiResponse.<JobPost>builder()
        .timestamp(LocalDateTime.now().toString())
        .path(httpServletRequest.getRequestURI())
        .requestMethod(httpServletRequest.getMethod())
        .result(job)
        .status(HttpStatus.OK)
        .message("Get Job Successfully!")
        .build();
  }

  @GetMapping("/company/{companyId}")
  public ApiResponse<Page<JobPost>> getAllJobs(
      @PathVariable String companyId, Pageable pageable, HttpServletRequest httpServletRequest) {
    Page<JobPost> jobs = jobPostService.getAllJobsByCompany(companyId, pageable);
    return ApiResponse.<Page<JobPost>>builder()
        .timestamp(LocalDateTime.now().toString())
        .path(httpServletRequest.getRequestURI())
        .requestMethod(httpServletRequest.getMethod())
        .result(jobs)
        .status(HttpStatus.OK)
        .message("Get Job Successfully!")
        .build();
  }

  @GetMapping("/download-template")
  public ResponseEntity<InputStreamResource> downloadTemplate() {
    String fileName = "post_template.xlsx";
    ClassPathResource resource = new ClassPathResource("static/" + fileName);
    excelService.exportExcel("static/post_template.xlsx");
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(resource.getFile())) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = fis.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }
      ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition","attachment; filename=template.xlsx");
      return ResponseEntity.ok()
          .headers(headers)
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(new InputStreamResource(in));
    } catch (IOException e) {
      throw new AppException(ResponseStatus.DOWNLOAD_TEMPLATE_ERROR);
    }
  }

  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<Void> importExcel(@RequestPart("file") MultipartFile file,
                                       @RequestHeader String userId,
                                       HttpServletRequest httpServletRequest) {
    if (file == null || file.isEmpty()) {
      throw new AppException(ResponseStatus.FILE_NOT_FOUND);
    }
    String contentType = file.getContentType();
    if (!isValidExcelContentType(contentType)) {
      throw new AppException(ResponseStatus.INVALID_FILE);
    }
    long maxFileSize = 20 * 1024 * 1024;
    if (file.getSize() > maxFileSize) {
      throw new AppException(ResponseStatus.FILE_SIZE_EXCEEDED);
    }
    excelService.importExcel(file, userId, httpServletRequest);
    return ApiResponse.<Void>builder()
            .timestamp(LocalDateTime.now().toString())
            .path(httpServletRequest.getRequestURI())
            .requestMethod(httpServletRequest.getMethod())
            .status(HttpStatus.OK)
            .message("Import Excel Successfully!")
            .build();
  }

  private boolean isValidExcelContentType(String contentType) {
    return contentType != null
            && (contentType.equals("application/vnd.ms-excel")
            || contentType.equals(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
  }
}
