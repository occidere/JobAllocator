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
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author occidere
 * @since 2019-01-19
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class JobAllocationTasklet implements Tasklet, StepExecutionListener {
	private Logger logger = LoggerFactory.getLogger(JobAllocationTasklet.class);
	private int minuteRange = 10; // TODO JobParameters 로 변경 필요

	@Autowired
	private WebClient webClient;

	@Autowired
	private JobInfoRepository jobInfoRepository;

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LocalTime curTime = LocalTime.now();

		logger.info("현재 시간: {}", curTime);
		String from = curTime.minusMinutes(minuteRange).format(DateTimeFormatter.ofPattern("HHmm")) + "00";
		String to = curTime.format(DateTimeFormatter.ofPattern("HHmm")) + "59";

		logger.info("잡 할당 체크 시간범위: {} ~ {}", from, to);

		List<JobInfo> jobInfos = jobInfoRepository.findJobInfosByOnSchedule(from, to, JobStatus.SERVICE_ON.getFieldName());
		logger.info("스케줄링 된 잡 개수: {}", jobInfos.size());

		for (JobInfo jobInfo : jobInfos) {
			String jobCallUrl = jobInfo.getJobCallUrl();
			logger.info("호출: {}", jobCallUrl);
			webClient.get().uri(jobCallUrl);
		}


		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
