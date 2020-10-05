package com.automic.ara.cdd.plugin.api.resources;

import com.automic.ara.cdd.plugin.api.ExecuteWorkflowHandler;
import com.automic.ara.cdd.plugin.api.converters.DtoConverter;
import com.automic.ara.cdd.plugin.backend.api.AraConnectionFactory;
import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.api.IPromptService;
import com.automic.ara.cdd.plugin.backend.api.IWorkflowService;
import com.automic.ara.cdd.plugin.backend.impl.RestPromptService;
import com.automic.ara.cdd.plugin.backend.impl.RestWorkflowService;
import com.automic.ara.cdd.plugin.backend.rest.clients.ApplicationClient;
import com.automic.ara.cdd.plugin.backend.rest.clients.PackageClient;
import com.automic.ara.cdd.plugin.backend.rest.clients.ProfileClient;
import com.automic.ara.cdd.plugin.backend.rest.models.Application;
import com.automic.ara.cdd.plugin.backend.rest.models.AraPackage;
import com.automic.ara.cdd.plugin.backend.rest.models.Profile;
import com.automic.ara.cdd.plugin.backend.rest.responses.ApplicationResponse;
import com.automic.ara.cdd.plugin.exceptions.AraPluginBadRequestException;
import com.automic.ara.cdd.plugin.utils.Utils;
import com.ca.rp.plugins.dto.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("workflows")
public class WorkflowResource {
	private static final Logger logger = LoggerFactory.getLogger(WorkflowResource.class);
	private static final String FILTER = "filter";
	private static final String CONTEXTKEY_APPLICATION = "application";
	private static final String CONTEXTKEY_COMPENSATION_APPLICATION = "compensation_application";
	private static final String CONTEXTKEY_WORKFLOW = "workflow";
	private static final String CONTEXTKEY_PACKAGE = "package";

	@POST
	@Path("/execute")
	@Produces(MediaType.APPLICATION_JSON)
	public ExternalTaskResult execute(@QueryParam(TaskConstants.ACTION_PARAM) String action,
			ExternalTaskInputs taskInputs) {
		try {
			return new ExecuteWorkflowHandler(taskInputs).execute(action);
		}
		catch (AraPluginBadRequestException ex){
			logger.error(ex.getMessage(), ex);
			return ExternalTaskResult.createResponseForFailure("Failed to execute action.", ex.getMessage());
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@POST
	@Path("/general-workflows")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DynamicValuesOutput getGeneralWorkflows(DynamicValuesInput dynamicValuesInput,
			@DefaultValue("") @QueryParam(FILTER) String filter) throws Exception {
		try {
			logger.info("Get general workflows. Filter: " + filter);
			IWorkflowService service = getWorkflowService(dynamicValuesInput);
			return DtoConverter.toDynamicValuesOutput(service.getGeneralWorkflows(filter), filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@POST
	@Path("/dynamic-property")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DynamicValuesOutput getDynamicProperties(DynamicValuesInput dynamicValuesInput,
													@DefaultValue("") @QueryParam(FILTER) String filter) throws Exception {
		return getPrompts(dynamicValuesInput,filter,"");
	}

	private DynamicValuesOutput getPrompts(DynamicValuesInput dynamicValuesInput, String filter, String prefix) throws Exception {
		try {
			logger.info("Get full name of prompt dynamic property : " + filter);
			String workflowName = getPluginContextKey(dynamicValuesInput, prefix+CONTEXTKEY_WORKFLOW );
			if (Utils.isNullOrEmpty(workflowName))
				return emptyDynamicValuesOutput();
			String applicationName = getPluginContextKey(dynamicValuesInput, prefix+ CONTEXTKEY_APPLICATION );
			String packageName = getPluginContextKey(dynamicValuesInput, prefix+CONTEXTKEY_PACKAGE);
			IPromptService service = getPromptService(dynamicValuesInput);
			return DtoConverter.convertToDynamicValuesOutput(service.getPrompts(applicationName,workflowName,packageName),filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@POST
	@Path("/compensation-dynamic-property")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DynamicValuesOutput getCompensationDynamicProperties(DynamicValuesInput dynamicValuesInput,
												   @DefaultValue("") @QueryParam(FILTER) String filter) throws Exception {
		return getPrompts(dynamicValuesInput,filter,"compensation_");
	}

	@POST
	@Path("/applications")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DynamicValuesOutput getApplications(DynamicValuesInput dynamicValuesInput,
			@DefaultValue("") @QueryParam(FILTER) String filter) throws Exception {
		try {
			logger.info("Get applications. Filter: " + filter);
			ApplicationClient client = new ApplicationClient(getConnection(dynamicValuesInput));
			return DtoConverter.toDynamicValuesOutput((List<Application>)Utils.unarchievedData(client.getApplicationsWithFilter(filter).getData()), filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@POST
	@Path("/application-workflows")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DynamicValuesOutput getApplicationWorkflows(DynamicValuesInput dynamicValuesInput,
			@DefaultValue("") @QueryParam(FILTER) String filter) throws Exception {
		try {
			String applicationName = getPluginContextApplication(dynamicValuesInput);
			
			logger.info("Get application wokflows. application name: " + applicationName + "; filter: " + filter);
			
			if (Utils.isNullOrEmpty(applicationName)) {
				return emptyDynamicValuesOutput();
			}

			IWorkflowService service = getWorkflowService(dynamicValuesInput);
			return DtoConverter.toDynamicValuesOutput(service.getApplicationWorkflow(applicationName, filter), filter);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@POST
	@Path("/profiles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DynamicValuesOutput getApplicationProfiles(DynamicValuesInput dynamicValuesInput,
			@DefaultValue("") @QueryParam(FILTER) String filter) throws Exception {

		try {
			DynamicValuesOutput result = emptyDynamicValuesOutput();

			String applicationName = getPluginContextApplication(dynamicValuesInput);
			
			logger.info("Get application profiles. application name: " + applicationName+ "; filter: " + filter);
			
			if (Utils.isNullOrEmpty(applicationName)) {
				return result;
			}

			ApplicationClient applicationClient = new ApplicationClient(getConnection(dynamicValuesInput));
			ApplicationResponse applicationResponse = applicationClient.getApplications(applicationName);
			if (applicationResponse.hasError() || applicationResponse.getData().isEmpty()) {
				return result;
			}

			List<Application> appList = (List<Application>) Utils.unarchievedData(applicationResponse.getData());

			if (appList.isEmpty()) {
				return result;
			}

			long applicationId = appList.get(0).getId();
			ProfileClient profileClient = new ProfileClient(getConnection(dynamicValuesInput));

			List<Profile> activeProfiles =  (List<Profile>) Utils.unarchievedData(profileClient.getProfiles(applicationId).getData());
			
			return DtoConverter.toDynamicValuesOutput(activeProfiles, filter);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@POST
	@Path("/packages")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DynamicValuesOutput getApplicationPackages(DynamicValuesInput dynamicValuesInput,
			@DefaultValue("") @QueryParam(FILTER) String filter) throws Exception {
		try {
			String applicationName = getPluginContextApplication(dynamicValuesInput);
			
			logger.info("Get application packages. application name: " + applicationName+ "; filter: " + filter);
			
			if (Utils.isNullOrEmpty(applicationName)) {
				return emptyDynamicValuesOutput();
			}

			PackageClient client = new PackageClient(getConnection(dynamicValuesInput));
			List<AraPackage> packages = (List<AraPackage>) Utils.unarchievedData(client.getPackages(applicationName).getData());
			
			return DtoConverter.toDynamicValuesOutput(packages, filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	private String getPluginContextApplication(DynamicValuesInput input) {
		String appName = getPluginContextKey(input, CONTEXTKEY_APPLICATION);
		if(appName == null)
			appName = getPluginContextKey(input, CONTEXTKEY_COMPENSATION_APPLICATION);
		return appName;
	}

	private String getPluginContextKey(DynamicValuesInput input, String key) {
		SerializablePair pair = input.getPluginServiceContext().get(key);
		if (pair == null)
			return null;
		return pair.getValue().toString();
	}

	private IWorkflowService getWorkflowService(DynamicValuesInput input) {
		return new RestWorkflowService(getConnection(input));
	}

	private IPromptService getPromptService(DynamicValuesInput input) {
		return new RestPromptService(getConnection(input));
	}

	private Connection getConnection(DynamicValuesInput input) {
		return AraConnectionFactory.withBasicAuth(input.getEndPointProperties());
	}

	private DynamicValuesOutput emptyDynamicValuesOutput() {
		DynamicValuesOutput result = new DynamicValuesOutput();
		result.setValues(new ArrayList<SerializablePair>());

		return result;
	}
}
