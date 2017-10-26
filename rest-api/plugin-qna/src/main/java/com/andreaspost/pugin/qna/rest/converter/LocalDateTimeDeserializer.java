package com.andreaspost.pugin.qna.rest.converter;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Deserializer for {@link LocalDateTime}.
 * 
 * @author Andreas Post
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

	private static final long serialVersionUID = -7108393159969911978L;

	public LocalDateTimeDeserializer() {
		super(LocalDateTime.class);
	}

	@Override
	public LocalDateTime deserialize(JsonParser jp, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return LocalDateTime.parse(jp.readValueAs(String.class));
	}

}
