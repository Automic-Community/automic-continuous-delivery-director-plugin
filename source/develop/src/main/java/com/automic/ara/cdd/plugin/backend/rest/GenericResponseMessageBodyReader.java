package com.automic.ara.cdd.plugin.backend.rest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.automic.ara.cdd.plugin.backend.rest.responses.BaseRestResponse;

public class GenericResponseMessageBodyReader implements MessageBodyReader {

	@Override
	public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		return ModelConvert.readFrom(type, entityStream);
	}
}
