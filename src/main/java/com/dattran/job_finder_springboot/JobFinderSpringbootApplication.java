package com.dattran.job_finder_springboot;

import com.dattran.job_finder_springboot.config.AwsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({AwsProperties.class})
@EnableScheduling
public class JobFinderSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobFinderSpringbootApplication.class, args);
	}

}
