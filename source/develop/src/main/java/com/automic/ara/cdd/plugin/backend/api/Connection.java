package com.automic.ara.cdd.plugin.backend.api;

import com.automic.ara.cdd.plugin.backend.rest.Authenticator;

public class Connection {
	private String url;
	private Authenticator authenticator;

	public Connection(String url, Authenticator authenticator) {
		super();
		this.url = url;
		this.authenticator = authenticator;
	}

	public String getUrl() {
		return url;
	}

	public Authenticator getAuthenticator() {
		return authenticator;
	}

}
