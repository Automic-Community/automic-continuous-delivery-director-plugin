package com.automic.ara.cdd.plugin.backend.rest.clients;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.responses.ApplicationResponse;

public class ApplicationClient extends BaseClient {
	private String APPLICATION_LIST = "/api/data/v1/applications?max_results={max_result}";

	public ApplicationClient(Connection connection) {
		super(connection);
	}

	public ApplicationResponse getApplicationsWithFilter(String filter) {
		if (filter !=null && !filter.isEmpty() )
			filter = filter.trim() + "%";
		return getApplications(filter);
	}

	public ApplicationResponse getApplications(String name) {
		WebTarget target = createClient().target(getAbsoluteUrl(APPLICATION_LIST))
				.resolveTemplate("max_result", maxResult);
		target = addQueryString(target, "name", name);
		Response response = get(target, null);
		return parseResponse(ApplicationResponse.class, response);
	}
}
