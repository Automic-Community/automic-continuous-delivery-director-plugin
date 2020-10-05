package com.automic.ara.cdd.plugin.backend.impl;

import java.util.ArrayList;
import java.util.List;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.api.IWorkflowService;
import com.automic.ara.cdd.plugin.backend.rest.clients.WorkflowClient;
import com.automic.ara.cdd.plugin.backend.rest.models.Workflow;
import com.automic.ara.cdd.plugin.backend.rest.responses.WorkflowResponse;
import com.automic.ara.cdd.plugin.utils.Utils;

public class RestWorkflowService implements IWorkflowService {
	private WorkflowClient workflowClient;

	public RestWorkflowService(Connection connection) {
		this.workflowClient = new WorkflowClient(connection);
	}

	@Override
	public List<Workflow> getGeneralWorkflows(String filter) {
		WorkflowResponse workflowResponse = workflowClient.getWorkflows("", filter);
		
		List<Workflow> result = new ArrayList<>();

		List<Workflow> workflows = (List<Workflow>) Utils.unarchievedData(workflowResponse.getData());
		for (Workflow workflow : workflows) {
			if (workflow.getApplication() == null) {
				result.add(workflow);
			}
		}

		return result;
	}

	@Override
	public List<Workflow> getApplicationWorkflow(String applicationName, String filter) {
		if(applicationName == null || applicationName.equals("")) {
			return new ArrayList<>();
		}
		
		return (List<Workflow>) Utils.unarchievedData(workflowClient.getWorkflows(applicationName, filter).getData());
	}

}