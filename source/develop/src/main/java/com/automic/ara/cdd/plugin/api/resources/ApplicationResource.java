package com.automic.ara.cdd.plugin.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automic.ara.cdd.plugin.api.ApplicationImportHandler;
import com.automic.ara.cdd.plugin.backend.api.AraConnectionFactory;
import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.api.IApplicationService;
import com.automic.ara.cdd.plugin.backend.impl.RestApplicationService;
import com.ca.rp.plugins.dto.model.ExternalApplicationSourceInput;
import com.ca.rp.plugins.dto.model.ExternalApplicationsResponse;

@Path("applications")
public class ApplicationResource {
	protected static final Logger logger = LoggerFactory.getLogger(ApplicationResource.class);
	
	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ExternalApplicationsResponse execute(ExternalApplicationSourceInput externalApplicationSourceInput)
			throws Exception {
		try {
			logger.info("Sync external applications started");
			ApplicationImportHandler handler = new ApplicationImportHandler(
					GetApplicationService(externalApplicationSourceInput));
			ExternalApplicationsResponse response = new ExternalApplicationsResponse();
			response.setApplications(handler.getListOfExternalApplications());
			logger.info("Sync external applications ended");
			return response;
		}
		catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	private IApplicationService GetApplicationService(ExternalApplicationSourceInput input) {
		Connection connection = AraConnectionFactory.withBasicAuth(input.getEndPointProperties());
		return new RestApplicationService(connection);		
	}
}