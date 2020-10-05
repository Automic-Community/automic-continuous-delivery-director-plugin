package com.automic.ara.cdd.plugin.backend.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Profile extends DetailModel {
	private Environment environment;

	public Profile() {
	}

	public Profile(long id, String name, String description) {
		super(id, name, description);
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
