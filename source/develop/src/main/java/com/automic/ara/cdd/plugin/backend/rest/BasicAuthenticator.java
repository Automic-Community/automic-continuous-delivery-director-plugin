package com.automic.ara.cdd.plugin.backend.rest;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class BasicAuthenticator implements Authenticator{
	private String username;
	private String password;

	public BasicAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
		
		
	}

	@Override
	public void registerClient(Client client) {
		if (username != null && !username.isEmpty()) {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
			client.register(feature);
		}
	}

}
