package com.automic.ara.cdd.plugin.backend.rest.models;

import java.util.Objects;

public class BaseModel {
	private long id;
	private String name;

	public BaseModel() {
		
	}

	public BaseModel(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseModel baseModel = (BaseModel) o;
		return id == baseModel.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
