package com.automic.ara.cdd.plugin.backend.rest.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecutionErtResponse extends BaseRestResponse {

	@JsonProperty("start_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private Date startTime;

	@JsonProperty("estimated_end_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private Date estimatedEndTime;

	public Date getStartTime() {
		return startTime;
	}

	public String getStartTimeInString(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(startTime);
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEstimatedEndTime() {
		return estimatedEndTime;
	}

	public String getEstimatedEndTimeInString(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(estimatedEndTime);
	}

	public void setEstimatedEndTime(Date estimatedEndTime) {
		this.estimatedEndTime = estimatedEndTime;
	}
}
