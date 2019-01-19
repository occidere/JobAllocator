package org.occidere.joballocator.domain;

import lombok.Data;
import org.occidere.joballocator.common.JobStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author occidere
 * @since 2019-01-19
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Data
@Document(collection = "jobinfo")
public class JobInfo {
	@Id
	private String id;

	private String type;
	private String name;
	private String time; // HHmmss
	private String status;
	public void setStatus(String status) {
		this.status = JobStatus.getStatus(status);
	}
	private String jobCallUrl;

//	@Override
//	public String toString() {
//		return String.format(
//				"Id: %s, Type: %s, Name: %s, Time: %s, status: %s, JobCallUrl: %s",
//				id, type, name, time, status, jobCallUrl
//		);
//	}
}
