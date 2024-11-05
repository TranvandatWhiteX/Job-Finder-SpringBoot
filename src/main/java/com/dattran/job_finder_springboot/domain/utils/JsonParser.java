package com.dattran.job_finder_springboot.domain.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {
  private static ObjectMapper mObjectMapper;

  private static ObjectMapper getMapper() {
    if (mObjectMapper == null) {
      mObjectMapper = new ObjectMapper();
      mObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    return mObjectMapper;
  }

  public static Map<String, Object> objectToMap(Object object) {
    try {
      return getMapper().convertValue(object, new TypeReference<>() {});
    } catch (Exception var2) {
      return new HashMap<>();
    }
  }
}
