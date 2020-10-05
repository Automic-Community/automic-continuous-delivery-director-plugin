package com.automic.ara.cdd.plugin.utils;

import com.automic.ara.cdd.plugin.exceptions.AraPluginArgumentException;

public class Guard {
	public static void notNull(Object obj, String message) {
		if(obj == null) {
			throw new AraPluginArgumentException(message);
		}
	}
	
	public static void notNullOrEmpty(String s, String message) {
		if(s == null || "".equals(s)) {
			throw new AraPluginArgumentException(message);
		}
	}
}
