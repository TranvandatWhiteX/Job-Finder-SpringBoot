//package com.dattran.job_finder_springboot.domain.entities.elasticsearch;
//
//import com.dattran.job_finder_springboot.domain.entities.Address;
//import com.dattran.job_finder_springboot.domain.entities.BusinessStream;
//import com.dattran.job_finder_springboot.domain.entities.Salary;
//import com.dattran.job_finder_springboot.domain.enums.JobLevel;
//import com.dattran.job_finder_springboot.domain.enums.JobType;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.List;
//
//@Document(indexName = "job-search")
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class JobPostSearch implements Serializable {
//    @Id
//    String id;
//
//    @Field(type = FieldType.Date)
//    LocalDate expiredDate;
//
//    @Field(type = FieldType.Text)
//    String jobTitle;
//
//    @Field(type = FieldType.Boolean)
//    Boolean isActive;
//
//    @Field(type = FieldType.Text)
//    String jobRequirement;
//
//    @Field(type = FieldType.Text)
//    String jobDescription;
//
//    @Field(type = FieldType.Text)
//    String benefit;
//
//    @Field(type = FieldType.Text)
//    String location;
//
//    @Field(type = FieldType.Nested)
//    List<JobLevel> jobLevels;
//
//    @Field(type = FieldType.Nested)
//    List<JobType> jobTypes;
//
//    @Field(type = FieldType.Nested)
//    Salary salary;
//
//    @Field(type = FieldType.Double)
//    Long experience;
//
//    @Field(type = FieldType.Nested)
//    Address address;
//
//    @Field(type = FieldType.Nested)
//    BusinessStream businessStream;
//
//    @Field(type = FieldType.Text)
//    String skills;
//}
