package org.occidere.joballocator.configuration;

import org.occidere.joballocator.job.JobAllocationTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;

/**
 * @author occidere
 * @since 2019-01-19
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {
	@Override
	public void setDataSource(DataSource dataSource) {
		// Empty Datasource
	}

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job jobAllocationJob(Step jobAllocationStep) {
		return jobBuilderFactory.get("jobAllocationJob")
				.incrementer(new RunIdIncrementer())
				.start(jobAllocationStep)
				.build();
	}

	@Bean
	public Step jobAllocationStep() {
		return stepBuilderFactory.get("jobAllocationStep")
				.transactionManager(dummyTransactionManager())
				.tasklet(jobAllocationTasklet())
				.build();
	}

	@Bean
	public JobAllocationTasklet jobAllocationTasklet() {
		return new JobAllocationTasklet();
	}

	@Bean
	public ResourcelessTransactionManager dummyTransactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
}
