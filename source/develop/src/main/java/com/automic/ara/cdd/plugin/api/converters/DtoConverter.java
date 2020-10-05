package com.automic.ara.cdd.plugin.api.converters;

import java.util.*;

import com.automic.ara.cdd.plugin.backend.rest.models.Application;
import com.automic.ara.cdd.plugin.backend.rest.models.BaseModel;
import com.automic.ara.cdd.plugin.backend.rest.models.Environment;
import com.automic.ara.cdd.plugin.backend.rest.responses.ExecutionErtResponse;
import com.automic.ara.cdd.plugin.backend.rest.responses.ExecutionResponse;
import com.automic.ara.cdd.plugin.utils.Constants;
import com.automic.ara.cdd.plugin.utils.Guard;
import com.automic.ara.cdd.plugin.utils.Utils;
import com.ca.rp.plugins.dto.model.DynamicValuesOutput;
import com.ca.rp.plugins.dto.model.ExternalApplication;
import com.ca.rp.plugins.dto.model.ExternalEnvironment;
import com.ca.rp.plugins.dto.model.ExternalTaskInputs;
import com.ca.rp.plugins.dto.model.ExternalTaskResult;
import com.ca.rp.plugins.dto.model.PollableResult;
import com.ca.rp.plugins.dto.model.SerializablePair;

public class DtoConverter {

	private static final String UNKNOWN_EXECUTION_STATUS = "___Unknow";
	private static HashMap<String, PollableResult.ExecutionStatus> executionToExternalTaskStatusMap = new HashMap<>();
	
	static {
		executionToExternalTaskStatusMap.put("WaitingApproval", PollableResult.ExecutionStatus.RUNNING);
		executionToExternalTaskStatusMap.put("WaitingStartTime", PollableResult.ExecutionStatus.RUNNING);
		executionToExternalTaskStatusMap.put("WaitingManualConfirm", PollableResult.ExecutionStatus.RUNNING);
		executionToExternalTaskStatusMap.put("Active", PollableResult.ExecutionStatus.RUNNING);
		executionToExternalTaskStatusMap.put("Blocked", PollableResult.ExecutionStatus.RUNNING);
		executionToExternalTaskStatusMap.put("Rejected", PollableResult.ExecutionStatus.FAILED);
		executionToExternalTaskStatusMap.put("Revoked", PollableResult.ExecutionStatus.FAILED);
		executionToExternalTaskStatusMap.put("Canceled", PollableResult.ExecutionStatus.FAILED);
		executionToExternalTaskStatusMap.put("Finished", PollableResult.ExecutionStatus.FINISHED);
		executionToExternalTaskStatusMap.put(UNKNOWN_EXECUTION_STATUS, PollableResult.ExecutionStatus.FAILED);
	}
	
	public static ExternalApplication toExternalApplication(Application application) {
		ExternalApplication result = new ExternalApplication();
		
		result.setId(application.getId());
		result.setName(application.getName());
		result.setDescription(application.getDescription());
		
		return result;
	}
	
	public static ExternalEnvironment toExternalEnvironment(Environment environment) {
		ExternalEnvironment result = new ExternalEnvironment();
		
		result.setId(environment.getId());
		result.setName(environment.getName());
		result.setDescription(environment.getDescription());
		
		return result;
	}
	
	public static ExternalTaskResult convertToExternalTaskResult(ExecutionResponse response, ExecutionErtResponse ertResponse, ExternalTaskInputs taskInputs, String failMessagePrefix, String executionIdKey) {
		Guard.notNull(response, "response cannot be null");
		Guard.notNull(taskInputs, "taskInputs cannot be null");
		Guard.notNull(taskInputs.getExecutionContext(), "taskInputs ExecutionContext cannot be null");
		
		if(response.hasError()) {
			return ExternalTaskResult.createResponseForFailure(failMessagePrefix, response.buildErrorMessage());
		}
		
		PollableResult.ExecutionStatus mappedStatus = mapToExternalTaskExecutionStatus(response.getStatus());

		ExternalTaskResult result;
		
		if(PollableResult.ExecutionStatus.FINISHED.equals(mappedStatus)) {
			result = ExternalTaskResult.createResponseForFinished(mappedStatus.toString(), createExecutionFinishStatusMessage(response));
		}
		else if(PollableResult.ExecutionStatus.RUNNING.equals(mappedStatus)) {
			result = ExternalTaskResult.createResponseForRunning(
					mappedStatus.toString(), createExecutionRunningStatusMessage(response, ertResponse), 0f, Constants.TASK_POLL_DELAY,
					putIdToExecutionContext(response.getId(), taskInputs, executionIdKey));
		}
		else {
			result = ExternalTaskResult.createResponseForFailure(mappedStatus.toString(), createExecutionFailStatusMessage(response).toString());
		}
		result.setOutputParameter(Constants.TASK_OUTPUT_PARAMETER_EXECUTION_ID, String.valueOf(response.getId()));
		result.setOutputParameter(Constants.TASK_OUTPUT_PARAMETER_RUN_ID, response.getProcessRunId());
		return result;
	}
	
	private static String createExecutionFinishStatusMessage(ExecutionResponse response) {
		StringBuilder sbMessage = createExecutionFailStatusMessage(response);
		if (response.getInstallationUrl() != null && !response.getInstallationUrl().isEmpty())
			sbMessage.append(String.format(Constants.ROW_FORMAT, "Open Installations", String.format(Constants.ICON_FORMAT, response.getInstallationUrl(), Constants.BASE64_INSTALLATION_ICON, "Open Installations")));
		return sbMessage.toString();
	}	

	private static StringBuilder createExecutionFailStatusMessage(ExecutionResponse response) {
		StringBuilder sbMessage = new StringBuilder();
		String status = response.getStatus();
		String statusRow = String.format(Constants.ROW_FORMAT,"Status", "Execution " + response.getId() + ". Status is " + status);
		sbMessage.append(statusRow);

		if(status.equals("Rejected") || status.equals("Revoked"))
			return sbMessage;

		sbMessage.append(createOpenMonitoringMessage(response));
	    return sbMessage;
	}

	private static String createOpenMonitoringMessage(ExecutionResponse response){
		if (response.getMonitorUrl()== null ||  response.getMonitorUrl().isEmpty())
			return "";
		return String.format(Constants.ROW_FORMAT, "Open Monitoring", String.format(Constants.ICON_FORMAT, response.getMonitorUrl(), Constants.BASE64_MONITORING_ICON, "Open Monitoring"));
	}

	private static String createExecutionRunningStatusMessage(ExecutionResponse response, ExecutionErtResponse ertResponse) {
		StringBuilder sbMessage = new StringBuilder();
		sbMessage.append(String.format(Constants.ROW_FORMAT,"Status", "Execution " + response.getId() + ". Status is " + response.getStatus()));
		if (ertResponse != null && (ertResponse.getStartTime() != null || ertResponse.getEstimatedEndTime() != null )) {
			if (ertResponse.getStartTime() != null) {
				sbMessage.append(String.format(Constants.ROW_FORMAT,"ST", ertResponse.getStartTimeInString(Constants.TIME_OUTPUT_FORMAT)));
			} else {
				sbMessage.append(String.format(Constants.ROW_FORMAT,"ST", "N/A"));
			}

			if (ertResponse.getEstimatedEndTime() != null) {
				sbMessage.append(String.format(Constants.ROW_FORMAT,"EET", ertResponse.getEstimatedEndTimeInString(Constants.TIME_OUTPUT_FORMAT)));
			} else {
				sbMessage.append(String.format(Constants.ROW_FORMAT,"EET", "N/A"));
			}
		}
		sbMessage.append(createOpenMonitoringMessage(response));

		return sbMessage.toString();
	}

	private static Map<String, String> putIdToExecutionContext(long executionId, ExternalTaskInputs taskInputs, String executionIdKey) {
		Map<String, String> executionContext = taskInputs.getExecutionContext();
		executionContext.put(executionIdKey, String.valueOf(executionId));
		return executionContext;
	}
	
	
	private static PollableResult.ExecutionStatus mapToExternalTaskExecutionStatus(String araExecutionStatus) {
		for(String key : executionToExternalTaskStatusMap.keySet()) {
			if(key.equalsIgnoreCase(araExecutionStatus)) {
				return executionToExternalTaskStatusMap.get(key);
			}
		}
		
		return executionToExternalTaskStatusMap.get(UNKNOWN_EXECUTION_STATUS);
	}

	public static long getExecutionId(Map<String, String> executionContext, String executeIdKey) {
		if(!executionContext.containsKey(executeIdKey)) return 0;

		return Long.parseLong(executionContext.get(executeIdKey));
	}

	public static DynamicValuesOutput convertToDynamicValuesOutput(List<String> list, String filter) {
		DynamicValuesOutput dynamicValuesOutput = new DynamicValuesOutput();
		List<SerializablePair> pairs = new ArrayList<>();
		int count = 0;
		for (String item: list) {
			if(Utils.isNullOrEmpty(filter) || item.toLowerCase().contains(filter.toLowerCase())) {
				pairs.add(new SerializablePair<>(String.valueOf(++count), item));
			}
		}
		dynamicValuesOutput.setValues(pairs);
		dynamicValuesOutput.setNumOfResults(pairs.size());
		return dynamicValuesOutput;
	}
	
	public static DynamicValuesOutput toDynamicValuesOutput(List<? extends BaseModel> list, String filter) {
		DynamicValuesOutput dynamicValuesOutput = new DynamicValuesOutput();
		List<SerializablePair> pairs = new ArrayList<>();
		for(BaseModel model : list) {
			if(Utils.isNullOrEmpty(filter) || model.getName().toLowerCase().contains(filter.toLowerCase())) {
				pairs.add(new SerializablePair<>(String.valueOf(model.getId()), model.getName()));
			}
		}
		dynamicValuesOutput.setValues(pairs);
		dynamicValuesOutput.setNumOfResults(pairs.size());
		return dynamicValuesOutput;
	}

	public static String decorateDetailedStatusMessage(String mainTaskMessage, String compensationTaskMessage, PollableResult.ExecutionStatus status){
		StringBuilder sbMessage = new StringBuilder();
		boolean isRunningStatus = (status == PollableResult.ExecutionStatus.RUNNING);
		if(!isRunningStatus)
			sbMessage.append("Details:<div style='height:5px;'></div>");
		if(compensationTaskMessage!=null){
			sbMessage.append("<div class=\"rp-color-azure\" style='font-weight:bold'>Main Task:</div>")
					.append(mainTaskMessage)
					.append("<br/><div class=\"rp-color-azure\" style='font-weight:bold'>Compensation Task:</div>")
					.append(compensationTaskMessage);
		} else {
			sbMessage.append(mainTaskMessage);
		}
		return sbMessage.toString();
	}

	public static String getNotStartedStatusMessage(){
		return String.format(Constants.ROW_FORMAT, "Status", "Not started");
	}
}
