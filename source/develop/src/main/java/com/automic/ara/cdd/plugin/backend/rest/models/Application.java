package com.automic.ara.cdd.plugin.backend.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Application extends DetailModel{
	public Application() {}

	public Application(long id, String name, String description) {
		super(id, name, description);
	}
}
