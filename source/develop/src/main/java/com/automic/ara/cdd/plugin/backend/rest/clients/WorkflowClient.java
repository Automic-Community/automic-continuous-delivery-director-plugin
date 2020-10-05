package com.automic.ara.cdd.plugin.backend.rest.clients;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.ModelConvert;
import com.automic.ara.cdd.plugin.backend.rest.requests.ExecuteWorkflowRequest;
import com.automic.ara.cdd.plugin.backend.rest.responses.ExecutionResponse;
import com.automic.ara.cdd.plugin.backend.rest.responses.WorkflowResponse;

public class WorkflowClient extends BaseClient {
	private String EXECUTE = "/api/data/v1/executions";
	private String WORKFLOW_LIST = "/api/data/v1/workflows?max_results={max_result}";

	public WorkflowClient(Connection connection) {
		super(connection);
	}

	public ExecutionResponse execute(ExecuteWorkflowRequest executeWorkflowRequest) {
		WebTarget target = createClient().target(getAbsoluteUrl(EXECUTE));
		Response response = post(target, ModelConvert.writeString(executeWorkflowRequest));
		return parseResponse(ExecutionResponse.class, response);
	}

	public WorkflowResponse getWorkflows(String applicationName, String filterName) {
		return getWorkflows(applicationName, null, filterName);
	}

	public WorkflowResponse getWorkflows(String applicationName, String workflowName, String filterName) {
		WebTarget target = createClient().target(getAbsoluteUrl(WORKFLOW_LIST)).resolveTemplate("max_result", maxResult);
		
		if (applicationName != null && !applicationName.equals("")) {
			target = target.queryParam("application.name", applicationName);
		}
		else  {
			target = target.queryParam("base_type", "General");
		}

		if (workflowName != null && !workflowName.equals("")) {
			target = target.queryParam("name", workflowName);
		}
		else if (filterName != null && !filterName.isEmpty()) {
			filterName = filterName.trim() + "%";
			target = target.queryParam("name", filterName);
		}
		Response response = get(target, null);
		return parseResponse(WorkflowResponse.class, response);
	}
}
