package org.occidere.joballocator.common;

import lombok.Getter;

/**
 * @author occidere
 * @since 2019-01-19
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Getter
public enum JobStatus {
	SERVICE_ON("on"),
	SERVICE_OFF("off"),
	START("start"),
	RUNNING("running"),
	COMPLETE("complete"),
	EXIT_ABNORMALLY("exitAbnormally"),
	UNKNOWN("unknown"),
	IGNORE("ignore");

	public static String getStatus(String status) {
		for (JobStatus value : JobStatus.values()) {
			if (value.fieldName.equals(status)) {
				return status;
			}
		}
		return UNKNOWN.fieldName;
	}

	JobStatus(String fieldName) {
		this.fieldName = fieldName;
	}

	private String fieldName;
}
