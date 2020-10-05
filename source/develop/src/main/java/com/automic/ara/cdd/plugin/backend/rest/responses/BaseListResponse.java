package com.automic.ara.cdd.plugin.backend.rest.responses;

import com.automic.ara.cdd.plugin.exceptions.AraPluginBackendException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseListResponse<T> extends BaseRestResponse {
	private boolean has_more;
	private int total;
	private T data;

	public T getData() {
		if (hasError()) {
			throw new AraPluginBackendException("Cannot get data." + buildErrorMessage());
		}

		return data;
	}
	public void setData(T data) {
		this.data = data;
	}


	public boolean isHas_more() {
		return has_more;
	}
	public void setHas_more(boolean has_more) {
		this.has_more = has_more;
	}
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
