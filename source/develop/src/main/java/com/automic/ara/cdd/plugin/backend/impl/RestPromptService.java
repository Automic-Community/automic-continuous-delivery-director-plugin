package com.automic.ara.cdd.plugin.backend.impl;

import com.automic.ara.cdd.plugin.backend.api.IPromptService;
import com.automic.ara.cdd.plugin.backend.rest.clients.PromptClient;
import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.responses.PromptResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestPromptService implements IPromptService {
    private PromptClient promptClient;

    public RestPromptService(Connection connection) {
        this.promptClient = new PromptClient(connection);
    }
    @Override
    public List<String> getPrompts(String applicationName, String workflowName, String packageName) {
        PromptResponse promptResponse = promptClient.getPrompts(applicationName, workflowName,packageName);
        List<String> result = new ArrayList<>();
        AddToList(result, promptResponse.getPromptApplication(), "application");
        AddToList(result, promptResponse.getPromptPackage(), "package");
        AddToList(result, promptResponse.getPromptWorkflow(), "workflow");
        AddComponents(result, promptResponse);
        return result;
    }

    private void AddComponents(List<String> result, PromptResponse response){
        HashMap<String, HashMap<String,Object>> promptComponents = response.getPromptComponents();
        if (promptComponents == null || promptComponents.isEmpty())
            return;
        for (String key: promptComponents.keySet()){
            HashMap<String,Object> prompts = promptComponents.get(key);
            if (prompts == null || prompts.isEmpty())
                continue;
            for (String fullName:prompts.keySet()) {
                result.add("component/"+key + fullName);
            }
        }
    }

    private void AddToList(List<String> result, HashMap<String, Object> source, String prefix){
        if (source == null || source.isEmpty())
            return;
        for (String key: source.keySet()){
            result.add(prefix + key);
        }
    }
}
