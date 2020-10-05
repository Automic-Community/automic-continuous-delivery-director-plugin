package com.automic.ara.cdd.plugin.backend.api;

import java.util.List;

public interface IPromptService {
    List<String> getPrompts(String applicationName, String workflowName, String packageName);
}
