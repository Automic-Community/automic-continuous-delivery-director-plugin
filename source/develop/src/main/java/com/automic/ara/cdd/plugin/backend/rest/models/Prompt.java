package com.automic.ara.cdd.plugin.backend.rest.models;

import com.automic.ara.cdd.plugin.backend.rest.ModelConvert;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Prompt {
    @JsonProperty("package")
    private HashMap<String, Object> promptPackage;

    @JsonProperty("application")
    private HashMap<String, Object> promptApplication;

    @JsonProperty("workflow")
    private HashMap<String, Object> promptWorkflow;

    @JsonProperty("components")
    private HashMap<String, HashMap<String, Object>> promptComponents;

    public Prompt(){}

    public Prompt(HashMap<String, Object> promptPackage, HashMap<String, Object> promptApplication, HashMap<String, Object> promptWorkflow, HashMap<String, HashMap<String, Object>> promptComponents) {
        this.promptPackage = promptPackage;
        this.promptApplication = promptApplication;
        this.promptWorkflow = promptWorkflow;
        this.promptComponents = promptComponents;
    }

    public void addOverride(String input, String overrideValue){
        if (input.isEmpty())
            return;

        Object overrideObject = toOverrideObject(overrideValue);

        promptApplication = process(input, "application", overrideObject, promptApplication);
        promptPackage =  process(input, "package", overrideObject, promptPackage);
        promptWorkflow = process(input, "workflow", overrideObject, promptWorkflow);
        promptComponents = processComponent(input, overrideObject, promptComponents);
    }

    private Object toOverrideObject(String overrideValue){
        if (overrideValue.startsWith("{"))
            return ModelConvert.readFrom(ObjectNode.class, overrideValue);
        return overrideValue;
    }

    private HashMap<String,HashMap<String,Object>> processComponent(String input, Object overrideValue, HashMap<String,HashMap<String,Object>> map){
        if (!input.startsWith("component"))
            return map;
        String nameValue = input.substring("component".length() + 1);

        int index = nameValue.indexOf('/');
        if (index < 0)
            return map;

        if (map == null)
            map = new HashMap<>();

        String componentName = nameValue.substring(0,index);
        String dynamicName = nameValue.substring(index);

        if (!map.containsKey(componentName))
            map.put(componentName, new HashMap<String, Object>());

        HashMap<String, Object> dynamicMap = map.get(componentName);
        if (!map.containsKey(dynamicName)) {
            dynamicMap.put(dynamicName, overrideValue);
        }

        return map;
    }

    private HashMap<String,Object> process(String input, String prefix, Object overrideValue, HashMap<String,Object> map){
        if (!input.startsWith(prefix))
            return map;
        String name = input.substring(prefix.length());
        if (map == null)
            map = new HashMap<>();

        if (!map.containsKey(name)){
            map.put(name, overrideValue);
        }
        return map;
    }

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

    public HashMap<String, Object> getPromptWorkflow() {
        return promptWorkflow;
    }

    public void setPromptWorkflow(HashMap<String, Object> promptWorkflow) {
        this.promptWorkflow = promptWorkflow;
    }

    public HashMap<String, HashMap<String, Object>> getPromptComponents() {
        return promptComponents;
    }

    public void setPromptComponents(HashMap<String, HashMap<String, Object>> promptComponents) {
        this.promptComponents = promptComponents;
    }
}
