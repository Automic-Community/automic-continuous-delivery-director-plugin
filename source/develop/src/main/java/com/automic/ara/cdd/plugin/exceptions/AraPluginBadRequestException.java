package com.automic.ara.cdd.plugin.exceptions;

public class AraPluginBadRequestException extends AraPluginException{
	public AraPluginBadRequestException(String message) {
		super(message);
	}
	
	public AraPluginBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
