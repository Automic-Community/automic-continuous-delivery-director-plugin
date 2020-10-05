package com.automic.ara.cdd.plugin.backend.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AraPackage extends DetailModel {
	private BaseModel application;

	public AraPackage() {
	}

	public AraPackage(long id, String name, String description) {
		super(id, name, description);
	}

	public BaseModel getApplication() {
		return application;
	}

	public void setApplication(BaseModel application) {
		this.application = application;
	}
}
