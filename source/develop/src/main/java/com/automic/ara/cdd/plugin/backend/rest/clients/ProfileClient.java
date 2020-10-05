package com.automic.ara.cdd.plugin.backend.rest.clients;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.responses.ProfileResponse;

public class ProfileClient extends BaseClient {
	private String APP_PROFILE_LIST = "/api/data/v1/applications/{applicationId}/profiles?max_results={max_result}";

	public ProfileClient(Connection connection) {
		super(connection);
	}

	public ProfileResponse getProfiles(long applicationId) {
		WebTarget target = createClient().target(getAbsoluteUrl(APP_PROFILE_LIST))
				.resolveTemplate("applicationId", applicationId).resolveTemplate("max_result", maxResult);
		
		Response response = get(target, null);
		return parseResponse(ProfileResponse.class, response);
	}
}
