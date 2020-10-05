package com.automic.ara.cdd.plugin.api.converters;

import java.util.Map;

import com.automic.ara.cdd.plugin.backend.rest.ModelConvert;
import com.automic.ara.cdd.plugin.backend.rest.models.Prompt;
import com.automic.ara.cdd.plugin.backend.rest.requests.ExecuteWorkflowRequest;
import com.automic.ara.cdd.plugin.exceptions.AraPluginBadRequestException;
import com.ca.rp.plugins.dto.model.ExternalTaskInputs;

public class TaskInputsConverter {
	private final static String PARAM_WORKFLOW = "workflow";
	private final static String PARAM_APPLICATION = "application";
	private final static String PARAM_PACKAGE = "package";
	private final static String PARAM_DEPLOYMENT_PROFILE = "deployment_profile";
	private final static String PARAM_QUEUE = "queue";
	private final static String PARAM_NEED_MANUAL_START = "need_manual_start";
	private final static String PARAM_MANUAL_CONFIRMER = "manual_confirmer";
	private final static String PARAM_PLANNED_FROM = "planned_from";
	private final static String PARAM_INSTALL_MODE = "install_mode";
	private final static String PARAM_COMPENSATION_PREFIX= "compensation_";
	private final static String PARAM_COMPENSATION = "compensation_parameters";
	private final static String DYNAMIC_PROPERTY = "dynamic_property";
	private final static String DYNAMIC_PROPERTY_VALUE= "dynamic_property_value";
	private final static String ADDITIONAL_DYNAMIC_PROPERTIES= "additional_dynamic_properties";


	public static ExecuteWorkflowRequest toCompensationExecuteWorkflowRequest(ExternalTaskInputs taskInputs){
		String temp = GetTaskPropertyValue(taskInputs.getTaskProperties(), PARAM_COMPENSATION,"");
		if("true".equalsIgnoreCase(temp)){
			return toExecuteWorkflowRequest(taskInputs, PARAM_COMPENSATION_PREFIX);
		}
		return null;
	}

	public static ExecuteWorkflowRequest toMainExecuteWorkflowRequest(ExternalTaskInputs taskInputs){
		return toExecuteWorkflowRequest(taskInputs, "");
	}

	private static ExecuteWorkflowRequest toExecuteWorkflowRequest(ExternalTaskInputs taskInputs, String prefix) {
		ExecuteWorkflowRequest request = new ExecuteWorkflowRequest();
		Map<String, String> taskProperties = taskInputs.getTaskProperties();

		if (taskProperties == null || taskProperties.isEmpty())
			return request;

		String temp = GetTaskPropertyValue(taskProperties, PARAM_WORKFLOW, prefix);
		if (temp != null) {
			request.setWorkflow(temp);
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_APPLICATION, prefix);
		if (temp != null) {
			request.setApplication(temp);
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_PACKAGE, prefix);
		if (temp != null) {
			request.setPck(temp);
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_DEPLOYMENT_PROFILE, prefix);
		if (temp != null) {
			request.setDeploymentProfile(temp);
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_QUEUE, prefix);
		if (temp != null) {
			request.setQueue(temp);
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_NEED_MANUAL_START, prefix);
		if (temp != null) {
			request.setNeedsManualStart(toBoolean(temp));
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_MANUAL_CONFIRMER, prefix);
		if (temp != null) {
			request.setManualConfirmer(temp);
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_PLANNED_FROM, prefix);
		if (temp != null) {
			request.setPlannedFrom(temp);
		}

		temp = GetTaskPropertyValue(taskProperties, PARAM_INSTALL_MODE, prefix);
		if (temp != null) {
			request.setInstallMode(temp.replace(" ", ""));
		}

		temp = GetTaskPropertyValue(taskProperties, ADDITIONAL_DYNAMIC_PROPERTIES, prefix);
		Prompt prompt = null;
		if (temp != null && !temp.isEmpty()) {
			try {
				prompt = ModelConvert.readFrom(Prompt.class, temp);
			}
			catch (Exception e) {
				String additionalInfo = (prefix == null ||  prefix.isEmpty()) ? "" : " of compensation parameters";
				throw new AraPluginBadRequestException(
						"Fail to deserialize 'Additional Dynamic Property'" + additionalInfo, e);
			}
		}

		temp = GetTaskPropertyValue(taskProperties, DYNAMIC_PROPERTY, prefix);
		String overrideValue = GetTaskPropertyValue(taskProperties, DYNAMIC_PROPERTY_VALUE, prefix);
		if (temp!= null && !temp.isEmpty() && overrideValue!= null){
			if (prompt == null)
				prompt = new Prompt();
			prompt.addOverride(temp, overrideValue);
		}

		if (prompt!= null)
			request.setOverrides(prompt);

		return request;
	}

	private static String GetTaskPropertyValue(Map<String, String> taskProperties, String propertyName, String prefix){
		String proName = prefix + propertyName;
		if (taskProperties.containsKey(proName)) {
			return taskProperties.get(proName);
		}
		return null;
	}

	private static boolean toBoolean(String string) {
		return "Yes".equalsIgnoreCase(string);
	}
}