package com.automic.ara.cdd.plugin.backend.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Installation extends DetailModel {

    @JsonProperty("package")
    private DetailModel cdaPackage;

	private DetailModel application;
	private DetailModel component;
	private DetailModel deploymentTarget;
	private DetailModel execution;
	private String state;

    public BaseModel getApplication() {
        return application;
    }

    public void setApplication(DetailModel application) {
        this.application = application;
    }

    public BaseModel getCdaPackage() {
        return cdaPackage;
    }

    public void setCdaPackage(DetailModel cdaPackage) {
        this.cdaPackage = cdaPackage;
    }

    public BaseModel getComponent() {
        return component;
    }

    public void setComponent(DetailModel component) {
        this.component = component;
    }

    public BaseModel getDeploymentTarget() {
        return deploymentTarget;
    }

    public void setDeploymentTarget(DetailModel deploymentTarget) {
        this.deploymentTarget = deploymentTarget;
    }

    public BaseModel getExecution() {
        return execution;
    }

    public void setExecution(DetailModel execution) {
        this.execution = execution;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
