package com.automic.ara.cdd.plugin.api;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automic.ara.cdd.plugin.api.converters.DtoConverter;
import com.automic.ara.cdd.plugin.backend.api.IApplicationService;
import com.automic.ara.cdd.plugin.backend.rest.ModelConvert;
import com.automic.ara.cdd.plugin.backend.rest.models.Application;
import com.automic.ara.cdd.plugin.backend.rest.models.Environment;
import com.automic.ara.cdd.plugin.utils.Utils;
import com.ca.rp.plugins.dto.model.ExternalApplication;
import com.ca.rp.plugins.dto.model.ExternalEnvironment;

public class ApplicationImportHandler {
	protected static final Logger logger = LoggerFactory.getLogger(ApplicationImportHandler.class);
	private IApplicationService service;

	public ApplicationImportHandler(IApplicationService applicationService) {
		this.service = applicationService;
	}

	public List<ExternalApplication> getListOfExternalApplications() {
		List<ExternalApplication> externalApplications = new ArrayList<ExternalApplication>();
		for (Application app : service.getApplications(null)) {
			ExternalApplication externalApplication = DtoConverter.toExternalApplication(app);
			injectEnvironment(externalApplication);
			externalApplications.add(externalApplication);
		}

		logger.info("result external applications:");
		logger.info(ModelConvert.writeString(externalApplications));

		return externalApplications;
	}

	private void injectEnvironment(ExternalApplication externalApplication) {
		logger.info("Get environments of application:" + ModelConvert.writeString(externalApplication));
		List<ExternalEnvironment> externalEnvironments = new ArrayList<ExternalEnvironment>();

        // Removing duplicate environments
		List<Environment> environments = service.getEnvironments(Long.parseLong(externalApplication.getId()));
		Map<String, Environment> map = new LinkedHashMap<>();
		for (Environment env : environments) {
		  map.put(env.getName(), env);
		}
		environments.clear();
		environments.addAll(map.values());

		for (Environment env : environments) {
			ExternalEnvironment extEnv = DtoConverter.toExternalEnvironment(env);
			extEnv.setId(calculateExternalEnvId(externalApplication, env));
			logger.info("Environment:" + ModelConvert.writeString(extEnv));
			externalEnvironments.add(extEnv);
		}
		externalApplication.setEnvironments(externalEnvironments);
	}
	
	/*
	 * generate unique hexadecimal id for external environment id.
	 * This method calculate external environment id again based on correlation between application and environment
	 */
	private String calculateExternalEnvId(ExternalApplication externalApplication, Environment env) {
		String appId = Utils.decimalToHex(Long.parseLong(externalApplication.getId()));
		String envId = Utils.decimalToHex(env.getId());
		return new StringBuilder(appId).append(envId).toString();
	}	

}
