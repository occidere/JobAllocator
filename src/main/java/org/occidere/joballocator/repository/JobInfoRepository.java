package org.occidere.joballocator.repository;

import org.occidere.joballocator.domain.JobInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author occidere
 * @since 2019-01-19
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public interface JobInfoRepository extends MongoRepository<JobInfo, String> {
	@Query(value = "{'time': {$gte: ?0, $lte: ?1}, 'status': ?2}")
	List<JobInfo> findJobInfosByOnSchedule(String startTime, String endTime, String status);


}
