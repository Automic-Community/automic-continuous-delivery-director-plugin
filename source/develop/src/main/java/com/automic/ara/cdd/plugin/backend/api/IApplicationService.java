package com.automic.ara.cdd.plugin.backend.api;

import java.util.List;

import com.automic.ara.cdd.plugin.backend.rest.models.Application;
import com.automic.ara.cdd.plugin.backend.rest.models.Environment;

public interface IApplicationService {
	List<Application> getApplications(String filter);

	List<Environment> getEnvironments(long id);
}
