package com.automic.ara.cdd.plugin.api.resources;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automic.ara.cdd.plugin.backend.api.AraConnectionFactory;
import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.clients.ApplicationClient;
import com.automic.ara.cdd.plugin.backend.rest.responses.ApplicationResponse;
import com.ca.rp.plugins.dto.model.ConnectivityInput;
import com.ca.rp.plugins.dto.model.ConnectivityResult;

@Path("connectivity")
public class ConnectivityTestResource {
	protected static final Logger logger = LoggerFactory.getLogger(ConnectivityTestResource.class);
	@POST
	@Path("connectivity-test")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ConnectivityResult connect(ConnectivityInput connectivityInput) {
		ConnectivityResult connectivityResult = new ConnectivityResult(true, null);
		logger.info("connectivity-test started");
		ApplicationClient applicationClient = createApplicationClient(connectivityInput);
		try {
			ApplicationResponse response = applicationClient.getApplicationsWithFilter(null);
			
			if (response.hasError()) {
				connectivityResult.setSuccess(false);
				String errorMessage = buildErrorMessage(response);
				connectivityResult.setErrorMessage(errorMessage);
				logger.info("connectivity-test failed: " + errorMessage);
			}
			else {
				connectivityResult.setSuccess(true);
				connectivityResult.setErrorMessage(null);
				logger.info("connectivity-test success");
			}
		}
		catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			connectivityResult.setSuccess(false);
			connectivityResult.setErrorMessage(ex.getMessage());
		}
		
		return connectivityResult;
	}

	private String buildErrorMessage(ApplicationResponse response) {
		return "Status code: " + response.getStatusCode() + 
				"; Status text: " + response.getStatusText() + 
				"; Error message: " + response.buildErrorMessage();
	}

	private ApplicationClient createApplicationClient(ConnectivityInput connectivityInput) {
		Map<String, String> endpointProperties = connectivityInput.getEndPointProperties();
		Connection connection = AraConnectionFactory.withBasicAuth(endpointProperties);
		ApplicationClient applicationClient = new ApplicationClient(connection);
		applicationClient.setMaxResult(1);
		return applicationClient;
	}
}
