package com.automic.ara.cdd.plugin.backend.api;

import java.util.List;

import com.automic.ara.cdd.plugin.backend.rest.models.Workflow;

public interface IWorkflowService {
	public List<Workflow> getGeneralWorkflows(String filter);
	
	public List<Workflow> getApplicationWorkflow(String applicationName, String filter);
}
