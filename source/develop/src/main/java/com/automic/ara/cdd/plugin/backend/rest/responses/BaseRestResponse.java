package com.automic.ara.cdd.plugin.backend.rest.responses;

import javax.ws.rs.core.Response.Status;

import com.automic.ara.cdd.plugin.backend.rest.models.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseRestResponse {
	private int statusCode;
	private String statusText;
	private ErrorResponse errorResponse;

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(ErrorResponse error) {
		this.errorResponse = error;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean hasError() {
		return statusCode != Status.OK.getStatusCode() && statusCode != Status.CREATED.getStatusCode();
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String buildErrorMessage() {
		String result = "There is an error in response: " + getStatusCode() + " (" + getStatusText() + "). ";
		if (getErrorResponse() != null) {
			String errorResponseStr = trySerializeErrorResponse();
			if (!"".equals(errorResponseStr)) {
				result += errorResponseStr;
			}
		}
		return result;
	}

	private String trySerializeErrorResponse() {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writeValueAsString(getErrorResponse());
		} catch (JsonProcessingException e) {
			return null;
		}
	}

}
