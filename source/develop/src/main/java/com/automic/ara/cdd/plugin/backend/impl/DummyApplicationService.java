package com.automic.ara.cdd.plugin.backend.impl;

import java.util.ArrayList;
import java.util.List;

import com.automic.ara.cdd.plugin.backend.api.IApplicationService;
import com.automic.ara.cdd.plugin.backend.rest.models.Application;
import com.automic.ara.cdd.plugin.backend.rest.models.Environment;
import com.ca.rp.plugins.dto.model.ExternalEnvironment;

public class DummyApplicationService implements IApplicationService{
	public List<Application> getApplications(String filter) {
		List<Application> result = new ArrayList<>();
		result.add(new Application(1L, "application1", "Test application 1"));
		result.add(new Application(2L, "application2", "Test application 2"));
		return result;
	}

	@Override
	public List<Environment> getEnvironments(long applicationId) {
		List<Environment> result = new ArrayList<>();
		result.add(new Environment(applicationId * 10, "Environment1 for " + applicationId,
				"Sample Environment1 for "  + applicationId));
		result.add(new Environment(applicationId * 10 + 1, "Environment2 for " + applicationId,
				"Sample Environment2 for "  + applicationId));
		return result;
	}
}
