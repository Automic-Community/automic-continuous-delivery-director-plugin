package com.automic.ara.cdd.plugin.backend.rest.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionResponse extends BaseRestResponse {
	public static final String STATUS_FINISHED = "Finished";
	public static final String STATUS_ACTIVE = "Active";
	public static final String STATUS_FAILED = "Failed";
	private long id;
	
	private String status;
	
	@JsonProperty("process_run_id")
	private String processRunId;
	
	@JsonProperty("monitor_url")
	private String monitorUrl;
	
	@JsonProperty("installation_url")
	private String installationUrl;

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
	
	public String getMonitorUrl() {
		return monitorUrl;
	}
	
	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}
	
	public String getInstallationUrl() {
		return installationUrl;
	}
	
	public void setInstallationUrl(String installationUrl) {
		this.installationUrl = installationUrl;
	}
	
	
}
