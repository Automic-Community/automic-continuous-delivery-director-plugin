package com.automic.ara.cdd.plugin.backend.rest;

import javax.ws.rs.client.Client;

public interface Authenticator {
	public void registerClient(Client client);
}
