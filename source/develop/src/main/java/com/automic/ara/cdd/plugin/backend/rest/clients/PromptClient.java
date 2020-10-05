package com.automic.ara.cdd.plugin.backend.rest.clients;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.responses.PromptResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class PromptClient extends BaseClient {
    private String PROMPTS = "/api/data/v1/prompts";
    public PromptClient(Connection connection) {
        super(connection);
    }

    public PromptResponse getPrompts(String applicationName, String workflowName, String packageName) {
        WebTarget target = createClient()
                .target(getAbsoluteUrl(PROMPTS));
        target = AddParam(target, "application", applicationName);
        target = AddParam(target, "workflow", workflowName);
        target = AddParam(target, "package", packageName);
        Response response = get(target, null);
        return parseResponse(PromptResponse.class, response);
    }

    private WebTarget AddParam(WebTarget target, String name, String value){
        if (value != null && !value.isEmpty())
            return target.queryParam(name, value);
        return target;
    }

}
