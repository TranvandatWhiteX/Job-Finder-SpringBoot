package com.dattran.job_finder_springboot.domain.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
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

  public static Map<String, Object> objectToMap(Object obj) {
    Map<String, Object> map = new HashMap<>();
    if (obj == null) return map;
    try {
      Field[] fields = obj.getClass().getDeclaredFields();
      for (Field field : fields) {
        if (field.isAnnotationPresent(JsonIgnore.class)) {
          continue;
        }
        field.setAccessible(true);
        if (isValidType(field)) {
          map.put(field.getName(), field.get(obj));
        } else {
          // Todo: Handle if field has object type.
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Error while accessing fields of object", e);
    }
    return map;
  }

  private static boolean isValidType(Field field) {
    Class<?> fieldType = field.getType();
    return fieldType.isPrimitive() || fieldType == String.class || fieldType.isEnum();
  }
}
