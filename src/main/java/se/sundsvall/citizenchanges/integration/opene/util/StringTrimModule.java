package se.sundsvall.citizenchanges.integration.opene.util;

import java.io.Serial;
import org.springframework.stereotype.Component;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdScalarDeserializer;
import tools.jackson.databind.module.SimpleModule;

@Component
public class StringTrimModule extends SimpleModule {

	@Serial
	private static final long serialVersionUID = 2581485258633691805L;

	public StringTrimModule() {
		addDeserializer(String.class, new StdScalarDeserializer<>(String.class) {

			@Serial
			private static final long serialVersionUID = -4835286013302460145L;

			@Override
			public String deserialize(final JsonParser jsonParser, final DeserializationContext ctx) {
				return jsonParser.getValueAsString().trim();
			}
		});
	}

}
