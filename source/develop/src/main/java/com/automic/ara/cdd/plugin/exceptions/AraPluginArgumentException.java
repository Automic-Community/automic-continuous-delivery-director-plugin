package com.automic.ara.cdd.plugin.exceptions;

public class AraPluginArgumentException extends RuntimeException{
	public AraPluginArgumentException(String message) {
		super(message);
	}
	
	public AraPluginArgumentException(String message, Throwable cause) {
		super(message, cause);
	}
}
