package com.automic.ara.cdd.plugin.backend.rest.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class PromptResponse extends BaseRestResponse {
    @JsonProperty("package")
    private HashMap<String, Object> promptPackage;

    @JsonProperty("application")
    private HashMap<String, Object> promptApplication;

    @JsonProperty("workflow")
    private HashMap<String, Object> promptWorkflow;

    @JsonProperty("components")
    private HashMap<String, HashMap<String, Object>> promptComponents;

    public HashMap<String, Object> getPromptPackage() {
        return promptPackage;
    }

    public void setPromptPackage(HashMap<String, Object> promptPackage) {
        this.promptPackage = promptPackage;
    }

    public HashMap<String, Object> getPromptApplication() {
        return promptApplication;
    }

    public void setPromptApplication(HashMap<String, Object> promptApplication) {
        this.promptApplication = promptApplication;
    }

    public HashMap<String, HashMap<String, Object>> getPromptComponents() {
        return promptComponents;
    }

    public void setPromptComponents(HashMap<String, HashMap<String, Object>> promptComponents) {
        this.promptComponents = promptComponents;
    }

    public HashMap<String, Object> getPromptWorkflow() {
        return promptWorkflow;
    }

    public void setPromptWorkflow(HashMap<String, Object> promptWorkflow) {
        this.promptWorkflow = promptWorkflow;
    }
}
