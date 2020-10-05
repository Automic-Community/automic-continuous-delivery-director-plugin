package com.automic.ara.cdd.plugin.backend.rest.clients;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.automic.ara.cdd.plugin.backend.api.Connection;
import com.automic.ara.cdd.plugin.backend.rest.responses.PackageResponse;

public class PackageClient extends BaseClient {
    private String PACKAGE_LIST = "/api/data/v1/packages?max_results={max_result}";

    public PackageClient(Connection connection) {
        super(connection);
    }

    public PackageResponse getPackages(String applicationName) {
        return getWorkflows(applicationName, null);
    }

    public PackageResponse getWorkflows(String applicationName, String packageName) {
        WebTarget target = createClient().target(getAbsoluteUrl(PACKAGE_LIST)).resolveTemplate("max_result", maxResult);

        target = addQueryString(target, "application.name", applicationName);
        target = addQueryString(target, "name", packageName);

        Response response = get(target, null);
        return parseResponse(PackageResponse.class, response);
    }

}
