package com.automic.ara.cdd.plugin.backend.rest.responses;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.automic.ara.cdd.plugin.backend.rest.models.ErrorResponse;

public class RestResponseParser {
	public static <T extends BaseRestResponse> T parseResponse(Class<T> clazz, Response response) {
		int status = response.getStatus();
		if (status == Status.OK.getStatusCode() || status == Status.CREATED.getStatusCode()) {
			T result = response.readEntity(clazz);
			result.setStatusCode(status);
			return result;
		} 
		else if (status == Status.NOT_FOUND.getStatusCode()) {
			return error(clazz, status, response.getStatusInfo().getReasonPhrase(), null);
		}
		else {
			return error(clazz, status, response.getStatusInfo().getReasonPhrase(), response.readEntity(ErrorResponse.class));
		}
	}
	
	public static <TK extends BaseRestResponse> TK error(Class<TK> clazz, int statusCode, String statusText,
			ErrorResponse errorResponse) {
		TK result = tryCreateNewInstance(clazz);
		
		result.setStatusCode(statusCode);
		result.setStatusText(statusText);
		result.setErrorResponse(errorResponse);
		
		return result;
	}
	
	private static <TK extends BaseRestResponse> TK tryCreateNewInstance(Class<TK> clazz) {
		TK result;
		try {
			result = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Error creating new error response " + e.getMessage(), e);
		}
		return result;
	}
}
