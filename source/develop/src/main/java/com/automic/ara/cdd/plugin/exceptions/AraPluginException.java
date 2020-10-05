package com.automic.ara.cdd.plugin.exceptions;

public class AraPluginException extends RuntimeException{
	public AraPluginException(String message) {
		super(message);
	}
	
	public AraPluginException(String message, Throwable cause) {
		super(message, cause);
	}
}
