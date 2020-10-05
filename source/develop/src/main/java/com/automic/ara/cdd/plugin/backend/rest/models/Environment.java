package com.automic.ara.cdd.plugin.backend.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Environment extends DetailModel{
	public Environment() {}

	public Environment(long id, String name, String description) {
		super(id, name, description);
	}
}
