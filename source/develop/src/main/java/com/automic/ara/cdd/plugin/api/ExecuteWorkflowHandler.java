package com.automic.ara.cdd.plugin.api;

import com.automic.ara.cdd.plugin.api.converters.DtoConverter;
import com.automic.ara.cdd.plugin.api.converters.TaskInputsConverter;
import com.automic.ara.cdd.plugin.backend.api.AraConnectionFactory;
import com.automic.ara.cdd.plugin.backend.api.AppConfig;
import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.clients.ExecutionClient;
import com.automic.ara.cdd.plugin.backend.rest.clients.InstallationClient;
import com.automic.ara.cdd.plugin.backend.rest.clients.WorkflowClient;
import com.automic.ara.cdd.plugin.backend.rest.models.BaseModel;
import com.automic.ara.cdd.plugin.backend.rest.models.Installation;
import com.automic.ara.cdd.plugin.backend.rest.models.Workflow;
import com.automic.ara.cdd.plugin.backend.rest.requests.ExecuteWorkflowRequest;
import com.automic.ara.cdd.plugin.backend.rest.responses.ExecutionErtResponse;
import com.automic.ara.cdd.plugin.backend.rest.responses.ExecutionResponse;
import com.automic.ara.cdd.plugin.backend.rest.responses.InstallationResponse;
import com.automic.ara.cdd.plugin.backend.rest.responses.WorkflowResponse;
import com.automic.ara.cdd.plugin.exceptions.AraPluginBackendException;
import com.automic.ara.cdd.plugin.utils.Constants;
import com.automic.ara.cdd.plugin.utils.Utils;
import com.ca.rp.plugins.dto.model.ExternalTaskInputs;
import com.ca.rp.plugins.dto.model.ExternalTaskResult;
import com.ca.rp.plugins.dto.model.PollableResult;
import com.ca.rp.plugins.dto.model.TaskConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExecuteWorkflowHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExecuteWorkflowHandler.class);

	private static final int SLEEP_TIME = 200;
	private static final String INSTALLATION_STATE_IN_PROGRESS = "InProgress";

	private ExternalTaskInputs taskInputs;
	private ExecuteWorkflowRequest mainExecuteWorkflowRequest;
	private ExecuteWorkflowRequest compensationExecuteWorkflowRequest;
	private Connection connection;
	private WorkflowClient workflowClient;
	private ExecutionClient executionClient;
	private InstallationClient installationClient;

	public ExecuteWorkflowHandler(ExternalTaskInputs taskInputs) {
		this.taskInputs = taskInputs;
    	this.mainExecuteWorkflowRequest = TaskInputsConverter.toMainExecuteWorkflowRequest(taskInputs);
    	this.compensationExecuteWorkflowRequest = TaskInputsConverter.toCompensationExecuteWorkflowRequest(taskInputs);
		this.connection = AraConnectionFactory.withBasicAuth(taskInputs.getEndPointProperties());
		this.workflowClient = new WorkflowClient(connection);
		this.executionClient = new ExecutionClient(connection);
		this.installationClient = new InstallationClient(connection);
	}

	public ExternalTaskResult execute(String action) {
		ExternalTaskResult externalTaskResult;
		if (TaskConstants.EXECUTE_ACTION.equalsIgnoreCase(action)) {
			externalTaskResult = tryExecuteOrGetTaskStatus();
		} else if (TaskConstants.STOP_ACTION.equalsIgnoreCase(action)) {
			externalTaskResult = stopTask();
		} else {
			throw new IllegalArgumentException(
					"Unexpected invalid action '" + action + "' while trying to execute task.");
		}
		
		return externalTaskResult;
	}

	private ExternalTaskResult tryExecuteOrGetTaskStatus() {
		ExternalTaskResult externalTaskResult;
		try {
			externalTaskResult = executeOrGetTaskStatus();
		} catch (Exception e) {
			logger.error("Failed to execute action. " + e.getMessage(), e);
			externalTaskResult = ExternalTaskResult.createResponseForFailure("Failed to execute action.", e.getMessage());
		}
		
		injectDeployedBuildNumber(externalTaskResult);
		
		return externalTaskResult;
	}

	private void injectDeployedBuildNumber(ExternalTaskResult externalTaskResult) {
		String pckName = mainExecuteWorkflowRequest.getPck();
		if (pckName != null && !"".equals(pckName)) {
			externalTaskResult.setDeployedBuildNumber(pckName);
		}
	}

	private ExternalTaskResult executeOrGetTaskStatus() {
		ExternalTaskResult mainTaskResult = executeOrGetTaskStatus(Constants.TASK_CONTEXT_KEY_EXECUTION_ID, mainExecuteWorkflowRequest);
		if(compensationExecuteWorkflowRequest != null && mainTaskResult.getExecutionStatus() == PollableResult.ExecutionStatus.FAILED){
			ExternalTaskResult compensationTaskResult = executeOrGetTaskStatus(Constants.TASK_CONTEXT_KEY_COMPENSATION_EXECUTION_ID, compensationExecuteWorkflowRequest);
        	compensationTaskResult.setDetailedInfo(DtoConverter.decorateDetailedStatusMessage(mainTaskResult.getDetailedInfo(), compensationTaskResult.getDetailedInfo(), compensationTaskResult.getExecutionStatus()));
			return updateOutputParams(compensationTaskResult, compensationExecuteWorkflowRequest, Constants.TASK_CONTEXT_KEY_COMPENSATION_EXECUTION_ID);
		}

		String compensationTaskMessage = null;
        if(mainTaskResult.getExecutionStatus() == PollableResult.ExecutionStatus.RUNNING && compensationExecuteWorkflowRequest != null)
			compensationTaskMessage = DtoConverter.getNotStartedStatusMessage();
		mainTaskResult.setDetailedInfo(DtoConverter.decorateDetailedStatusMessage(mainTaskResult.getDetailedInfo(), compensationTaskMessage, mainTaskResult.getExecutionStatus()));
		return updateOutputParams(mainTaskResult, mainExecuteWorkflowRequest, Constants.TASK_CONTEXT_KEY_EXECUTION_ID);
	}

	private boolean isStartApplicationWorkflowRequest(ExecuteWorkflowRequest request) {
		return request.getApplication() != null;
	}

	private ExternalTaskResult executeOrGetTaskStatus(String executionIdKey, ExecuteWorkflowRequest executeWorkflowRequest) {
		long executionId = DtoConverter.getExecutionId(taskInputs.getExecutionContext(), executionIdKey);
		boolean startExecute = executionId == 0;

		ExecutionResponse executionResponse;
		ExecutionErtResponse ertResponse = null;
		String failMessage;

		if (startExecute) {
			fixExecuteGeneralWorkflowExecutionBugByReplaceWorkflowNameWithId(executeWorkflowRequest);
			executionResponse = workflowClient.execute(executeWorkflowRequest);
			failMessage = "Failed to start workflow.";
		} else {
			ertResponse = executionClient.getExecutionErt(executionId);
			executionResponse = executionClient.getExecution(executionId);
			failMessage = "Failed to get execution status.";
		}

		return DtoConverter.convertToExternalTaskResult(executionResponse, ertResponse, taskInputs, failMessage, executionIdKey);
	}

	private ExternalTaskResult updateOutputParams(ExternalTaskResult taskResult, ExecuteWorkflowRequest request, String executionIdKey) {

		long executionId = DtoConverter.getExecutionId(taskInputs.getExecutionContext(), executionIdKey);
		if (executionId == 0) {
			return taskResult;
		}

		if (isStartApplicationWorkflowRequest(request) && isCompletedTask(taskResult)) {

			ExecutionResponse executionResponse = executionClient.getExecution(executionId);
			if (executionResponse.hasError()) {
				throw new AraPluginBackendException(String.format("Cannot get the Execution with id = %d. %s", executionId, executionResponse.buildErrorMessage()));
			}

			InstallationResponse installationResponse = getInstallations(executionResponse.getId());
			List<BaseModel> components = installationResponse.getData().stream().map(Installation::getComponent).distinct().collect(Collectors.toList());
			List<String> componentNames = components.stream().map(BaseModel::getName).collect(Collectors.toList());

			ObjectMapper mapper = new ObjectMapper();
			String jsonComponents;

			try {
				jsonComponents = mapper.writeValueAsString(componentNames);
			} catch (JsonProcessingException e) {
				throw new AraPluginBackendException("Failed to convert Component List to JSON string", e);
			}

			taskResult.setOutputParameter(Constants.TASK_OUTPUT_PARAMETER_COMPONENTS, jsonComponents);
			taskResult.setOutputParameter(Constants.TASK_OUTPUT_PARAMETER_INSTALLATIONS, executionResponse.getInstallationUrl());
		}

		return taskResult;
	}

	private boolean isCompletedTask(ExternalTaskResult taskResult) {
		return taskResult.getExecutionStatus() != PollableResult.ExecutionStatus.RUNNING;
	}

	private InstallationResponse getInstallations(long executionId) {
		boolean isCompleted = false;
		InstallationResponse installationResponse = null;
		LocalDateTime start = LocalDateTime.now();

		long timeoutMilliseconds = AppConfig.getInstallationTimeOut();
		while (!isCompleted && Duration.between(start, LocalDateTime.now()).toMillis() < timeoutMilliseconds) {
			installationResponse = installationClient.getInstallations(executionId);
			if(installationResponse.hasError()) {
				throw new AraPluginBackendException(String.format("Cannot get the Installation information of Execution id = %d. %s", executionId, installationResponse.buildErrorMessage()));
			}

			if(!isUpdatingInstallations(installationResponse)) {
				isCompleted = true;
			}

			try {
				TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				throw new AraPluginBackendException("Cannot get the Installation information.", e);
			}
		}

		return installationResponse;
	}

	private boolean isUpdatingInstallations(InstallationResponse installationResponse) {
		return installationResponse.getData().stream().anyMatch(x -> INSTALLATION_STATE_IN_PROGRESS.equalsIgnoreCase(x.getState()));
	}

	/*
	 * bond throw exception if there is an application workflow has the same
	 * name with general workflow
	 */
	private void fixExecuteGeneralWorkflowExecutionBugByReplaceWorkflowNameWithId(
			ExecuteWorkflowRequest executeRequest) {

		String applicationName = executeRequest.getApplication();
		String workflowName = executeRequest.getWorkflow().toString();

		if (!Utils.isNullOrEmpty(applicationName) || Utils.isNullOrEmpty(workflowName))
			return;

		WorkflowResponse response = workflowClient.getWorkflows("", workflowName);
		if (response.hasError())
			return;

		List<Workflow> workflows = (List<Workflow>) Utils.unarchievedData(response.getData());
		for (Workflow workflow : workflows) {
			if (workflow.getApplication() == null) {
				executeRequest.setWorkflow(workflow.getId());
			}
		}
	}

	private ExternalTaskResult stopTask() {
		return ExternalTaskResult.createResponseForFailure("Failed to stop task", "Cannot stop");
	}
}
