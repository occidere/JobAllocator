package org.occidere.joballocator.job;

import org.occidere.joballocator.common.JobStatus;
import org.occidere.joballocator.domain.JobInfo;
import org.occidere.joballocator.repository.JobInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author occidere
 * @since 2019-01-19
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class JobAllocationTasklet implements Tasklet, StepExecutionListener {
	private Logger logger = LoggerFactory.getLogger(JobAllocationTasklet.class);

	private int minuteRange = 10; // TODO JobParameters 로 변경 필요

	@Value("${job.allocator.execute.thread.count}")
	private int jobAllocatorExecuteThreadCount;

	@Autowired
	private WebClient webClient;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JobInfoRepository jobInfoRepository;

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LocalDateTime curTime = LocalDateTime.now();

		logger.info("현재 시간: {}", curTime);
		String from = curTime.minusMinutes(minuteRange).toLocalTime().format(DateTimeFormatter.ofPattern("HHmm")) + "00";
		String to = curTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmm")) + "59";

		logger.info("잡 할당 체크 시간범위: {} ~ {}", from, to);

		List<JobInfo> jobInfos = jobInfoRepository.findJobInfosByOnSchedule(from, to, JobStatus.SERVICE_ON.getFieldName());
		logger.info("스케줄링 된 잡 개수: {}", jobInfos.size());

		logger.info("스레드 개수: {}", jobAllocatorExecuteThreadCount);

		new ForkJoinPool(jobAllocatorExecuteThreadCount).submit(() ->
				jobInfos.parallelStream()
						.forEach(jobInfo -> {
							String jobCallUrl = jobInfo.getJobCallUrl();
							logger.info("호출 Job 정보: {}", jobInfo.toString());
//							webClient.get().uri(jobCallUrl).exchange().block();
							restTemplate.execute(jobCallUrl, HttpMethod.GET, null, null);
						})).get();

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
