package com.dattran.job_finder_springboot.domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Asset {
    String logo;

    String thumbnail;

    Map<String, String> otherAssets;
}
