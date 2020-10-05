package com.automic.ara.cdd.plugin.backend.api;

import java.util.Map;

import com.automic.ara.cdd.plugin.backend.rest.BasicAuthenticator;
import com.automic.ara.cdd.plugin.exceptions.AraPluginException;

public class AraConnectionFactory {
	public static Connection basic(String url, String username, String password) {
		if(username == null || password == null || url == null) {
			throw new AraPluginException("username, password and url are requires");
		}
		
		return new Connection(url, new BasicAuthenticator(username, password)) ;
	}
	
	public static Connection withBasicAuth(Map<String, String> endpointProperties) {
		String username = null;
		String password = null;
		String url = null;

		if (endpointProperties != null && !endpointProperties.isEmpty()) {
			username = endpointProperties.get("username");
			password = endpointProperties.get("password");
			url = endpointProperties.get("URL");
		}
		
		return basic(url, username, password);
	}
}
