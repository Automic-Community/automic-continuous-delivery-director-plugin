package com.automic.ara.cdd.plugin.backend.rest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Execution {
	private long id;

	private String status;

	@JsonProperty("process_run_id")
	private String processRunId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcessRunId() {
		return processRunId;
	}

	public void setProcessRunId(String processRunId) {
		this.processRunId = processRunId;
	}
}
