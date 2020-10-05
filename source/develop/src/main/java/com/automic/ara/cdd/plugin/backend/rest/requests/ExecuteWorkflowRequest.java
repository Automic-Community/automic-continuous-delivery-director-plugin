package com.automic.ara.cdd.plugin.backend.rest.requests;

import com.automic.ara.cdd.plugin.backend.rest.models.Prompt;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExecuteWorkflowRequest {
	private Object workflow;

	private String application;

	@JsonProperty("package")
	private String pck;

	@JsonProperty("deployment_profile")
	private String deploymentProfile;

	private String queue;

	@JsonProperty("needs_manual_start")
	private boolean needsManualStart;

	@JsonProperty("manual_confirmer")
	private String manualConfirmer;

	@JsonProperty("planned_from")
	private String plannedFrom;

	@JsonProperty("install_mode")
	private String installMode;

	@JsonProperty("overrides")
	private Prompt overrides;

	public Object getWorkflow() {
		return workflow == null ? null : workflow;
	}

	public void setWorkflow(Object workflow) {
		if(workflow instanceof Long) {
			ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
			objectNode.put("id", (long)workflow);
			this.workflow = objectNode;
		}
		else {
			this.workflow = workflow;
		}
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getPck() {
		return pck;
	}

	public void setPck(String pck) {
		this.pck = pck;
	}

	public String getDeploymentProfile() {
		return deploymentProfile;
	}

	public void setDeploymentProfile(String deploymentProfile) {
		this.deploymentProfile = deploymentProfile;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public boolean getNeedsManualStart() {
		return needsManualStart;
	}

	public void setNeedsManualStart(boolean needsManualStart) {
		this.needsManualStart = needsManualStart;
	}

	public String getManualConfirmer() {
		return manualConfirmer;
	}

	public void setManualConfirmer(String manualConfirmer) {
		this.manualConfirmer = manualConfirmer;
	}

	public String getPlannedFrom() {
		return plannedFrom;
	}

	public void setPlannedFrom(String plannedFrom) {
		this.plannedFrom = plannedFrom;
	}

	public String getInstallMode() {
		return installMode;
	}

	public void setInstallMode(String installMode) {
		this.installMode = installMode;
	}

	public Prompt getOverrides() {
		return overrides;
	}

	public void setOverrides(Prompt overrides) {
		this.overrides = overrides;
	}

}
