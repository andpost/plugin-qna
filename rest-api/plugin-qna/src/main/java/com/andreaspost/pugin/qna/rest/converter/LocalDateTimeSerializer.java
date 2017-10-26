package com.andreaspost.pugin.qna.rest.converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serializer for {@link LocalDateTime}.
 * 
 * @author Andreas Post
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

	public LocalDateTimeSerializer() {
		super(LocalDateTime.class);
	}

	@Override
	public void serialize(LocalDateTime value, JsonGenerator jsonGen, SerializerProvider sp)
			throws IOException, JsonGenerationException {
		jsonGen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}
}
