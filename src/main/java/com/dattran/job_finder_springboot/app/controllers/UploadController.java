package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.responses.UploadResponse;
import com.dattran.job_finder_springboot.domain.enums.UploadType;
import com.dattran.job_finder_springboot.domain.services.StorageService;
import com.dattran.job_finder_springboot.domain.utils.JFHeader;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/s3")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UploadController {
    StorageService storageService;

    @GetMapping("/generate-upload-url")
    public UploadResponse generateUploadUrl(
            @RequestHeader(name = JFHeader.headerUserId) String userId,
            @RequestParam @Valid UploadType type,
            @RequestParam @Valid String fileName
    ) {
        return storageService.generateUrl(type, fileName, userId);
    }
}
