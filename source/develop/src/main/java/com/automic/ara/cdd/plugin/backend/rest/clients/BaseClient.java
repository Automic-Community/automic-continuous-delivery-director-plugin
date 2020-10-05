package com.automic.ara.cdd.plugin.backend.rest.clients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.GenericResponseMessageBodyReader;
import com.automic.ara.cdd.plugin.backend.rest.ModelConvert;
import com.automic.ara.cdd.plugin.backend.rest.responses.BaseRestResponse;
import com.automic.ara.cdd.plugin.backend.rest.responses.RestResponseParser;
import com.automic.ara.cdd.plugin.exceptions.AraPluginBackendException;

public class BaseClient {
	protected static final int MAX_RESULT = 1000;

	protected static final Logger logger = LoggerFactory.getLogger(BaseClient.class);

	private Connection connection;

	protected int maxResult;

	public BaseClient(Connection connection) {
		this.connection = connection;
		this.maxResult = MAX_RESULT;
	}

	protected Client createClient() {
		Client httpClient = ClientBuilder.newClient().register(GenericResponseMessageBodyReader.class);
		connection.getAuthenticator().registerClient(httpClient);
		return httpClient;
	}

	protected String getAbsoluteUrl(String url) {
		return connection.getUrl() + url;
	}

	protected Response get(WebTarget target, String body) {
		return invoke(target, "GET", body);
	}

	protected Response post(WebTarget target, String body) {
		return invoke(target, "POST", body);
	}

	protected Response invoke(WebTarget target, String method, String body) {
		Builder request = target.request();
		logger.info("Request " + target.getUri().toString() + "; method: " + method + "; body: " + body);
		request.header("Content-Type", "application/json");
		Response response = request.method(method, Entity.json(body));
		if (response == null) {
			throw new AraPluginBackendException("No response from " + target.getUri().toString());
		}
		return response;
	}

	protected <T extends BaseRestResponse> T parseResponse(Class<T> clazz, Response response) {
		T result = RestResponseParser.parseResponse(clazz, response);
		logResult(result);
		return result;
	}

	private <T extends BaseRestResponse> void logResult(T result) {
		if (!result.hasError()) {
			logger.info("Response SUCCESS. " + ModelConvert.writeString(result));
		} else {
			logger.info("Response ERROR. status: " + result.getStatusCode() + "; status text: " + result.getStatusText()
					+ "; error response: " + ModelConvert.writeString(result.getErrorResponse()));
		}
	}

	protected WebTarget addQueryString(WebTarget target, String parameter, String value) {
		if (value != null && !value.equals("")) {
			target = target.queryParam(parameter, value);
		}
		return target;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

}