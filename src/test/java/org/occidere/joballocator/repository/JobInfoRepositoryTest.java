package org.occidere.joballocator.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.occidere.joballocator.domain.JobInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author occidere
 * @since 2019-01-19
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobInfoRepositoryTest {
	@Autowired
	private JobInfoRepository jobInfoRepository;

	@Test
	public void fetchScheduleBetweenTest() {
		List<JobInfo> jobInfos = jobInfoRepository.findJobInfosByOnSchedule("000000", "031000", "on");
		for (JobInfo jobInfo : jobInfos) {
			System.out.println(jobInfo);
		}
		System.out.println("-------------------");
		System.out.println(jobInfoRepository.count());
	}
}
