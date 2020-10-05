package com.automic.ara.cdd.plugin.backend.impl;

import java.util.ArrayList;
import java.util.List;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.api.IApplicationService;
import com.automic.ara.cdd.plugin.backend.rest.clients.ApplicationClient;
import com.automic.ara.cdd.plugin.backend.rest.clients.ProfileClient;
import com.automic.ara.cdd.plugin.backend.rest.models.Application;
import com.automic.ara.cdd.plugin.backend.rest.models.Environment;
import com.automic.ara.cdd.plugin.backend.rest.models.Profile;
import com.automic.ara.cdd.plugin.utils.Utils;

public class RestApplicationService implements IApplicationService {
	
	private ApplicationClient applicationClient;
	private ProfileClient profileClient;

	public RestApplicationService(Connection connection) {
		this.applicationClient = new ApplicationClient(connection);
		this.profileClient = new ProfileClient(connection);
	}

	@Override
	public List<Application> getApplications(String filter) {
		return (List<Application>) Utils.unarchievedData(this.applicationClient.getApplicationsWithFilter(filter).getData());
	}

	@Override
	public List<Environment> getEnvironments(long applicationId) {
		List<Profile> profiles = (List<Profile>) Utils.unarchievedData(this.profileClient.getProfiles(applicationId).getData());
		List<Environment> result = new ArrayList<>();
		for (Profile profile : profiles) {
			result.add(profile.getEnvironment());
		}
		
		return result;
	}

}
