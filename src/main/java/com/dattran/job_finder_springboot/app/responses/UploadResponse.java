package com.dattran.job_finder_springboot.app.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponse {
    private String uploadUrl;
    private String imageUrl;
    private String imagePath;
}
