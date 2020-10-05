package com.automic.ara.cdd.plugin.backend.rest.clients;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.responses.InstallationResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class InstallationClient extends BaseClient{
	private String INSTALLATION_LIST = "/api/data/v1/installations?execution.ids={execution_id}&max_results={max_result}&include_deactivated=true";

	public InstallationClient(Connection connection){
		super(connection);
	}
	
	public InstallationResponse getInstallations(long executionId) {
		WebTarget target = createClient()
				.target(getAbsoluteUrl(INSTALLATION_LIST))
				.resolveTemplate("execution_id", executionId)
				.resolveTemplate("max_result", maxResult);
		Response response = get(target, null);
		return parseResponse(InstallationResponse.class, response);
	}
}
