package com.automic.ara.cdd.plugin.backend.rest.clients;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.responses.ExecutionErtResponse;
import com.automic.ara.cdd.plugin.backend.rest.responses.ExecutionResponse;

public class ExecutionClient extends BaseClient{
	private String APP_EXECUTION_LIST = "/api/data/v1/executions/{id}";
	private String EXECUTION_ERT_INFO = "/api/data/v1/executions/{id}/estimated_runtime";
	
	public ExecutionClient(Connection connection){
		super(connection);
	}
	
	public ExecutionResponse getExecution(long id) {
		WebTarget target = createClient()
				.target(getAbsoluteUrl(APP_EXECUTION_LIST))
				.resolveTemplate("id", id);
		Response response = get(target, null);
		return parseResponse(ExecutionResponse.class, response);
	}

	public ExecutionErtResponse getExecutionErt(long id) {
		WebTarget target = createClient()
				.target(getAbsoluteUrl(EXECUTION_ERT_INFO))
				.resolveTemplate("id", id);
		Response response = get(target, null);
		return parseResponse(ExecutionErtResponse.class, response);
	}
}
