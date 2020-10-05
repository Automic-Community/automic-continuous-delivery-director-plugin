package com.automic.ara.cdd.plugin.backend.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import javax.ws.rs.ProcessingException;

import com.automic.ara.cdd.plugin.utils.Iso8601;
import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelConvert {

	public static <T> T readFrom(Class<T> type, String content) {
		try (InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
			return readFrom(type, stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T readFrom(Class<T> type, InputStream entityStream) {
		ObjectMapper mapper = new ObjectMapper();
		/* Comment the code as it doesn't work all the time due to jackson dataind library 2.8 version. Seems to be intermittent issue.
		   So we will ignore unknown fields based on pojo annotation only*/
		//mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		T response = null;

		try {
			response = mapper.readValue(entityStream, type);
		} catch (Exception e) {
			throw new ProcessingException("Failed to deserialize " + type.getName() + ": " + e.getMessage(), e);
		}
		return response;
	}

	public static String writeString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(Iso8601.getIsoDateFormat());

		String str;
		try {
			str = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error write value as string: " + e.getMessage(), e);
		}

		return str;
	}
}
