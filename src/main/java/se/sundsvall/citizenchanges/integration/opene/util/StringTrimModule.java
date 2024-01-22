package se.sundsvall.citizenchanges.integration.opene.util;

import java.io.IOException;
import java.io.Serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class StringTrimModule extends SimpleModule {

	@Serial
	private static final long serialVersionUID = 2581485258633691805L;

	public StringTrimModule() {
		addDeserializer(String.class, new StdScalarDeserializer<>(String.class) {

			@Serial
			private static final long serialVersionUID = -4835286013302460145L;

			@Override
			public String deserialize(final JsonParser jsonParser, final DeserializationContext ctx) throws IOException {
				return jsonParser.getValueAsString().trim();
			}
		});
	}

}
