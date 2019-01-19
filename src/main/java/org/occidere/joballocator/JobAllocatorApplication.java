package org.occidere.joballocator;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "org.occidere.joballocator", exclude = DataSourceAutoConfiguration.class)
public class JobAllocatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobAllocatorApplication.class, args).close();
	}

}

