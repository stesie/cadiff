package de.brokenpipe.cadiff.cli.control;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.brokenpipe.cadiff.core.actions.Action;

import java.io.IOException;

public class ActionDeserializer extends StdDeserializer<Action> {

	public ActionDeserializer() {
		this(null);
	}

	protected ActionDeserializer(final Class<?> vc) {
		super(vc);
	}

	@Override
	public Action deserialize(final JsonParser parser, final DeserializationContext deserializationContext)
			throws IOException, JacksonException {

		final JsonNode node = parser.getCodec().readTree(parser);

		if (!node.has("type")) {
			throw new IllegalArgumentException("Action does not have a type");
		}

		final JsonNode typeNode = node.get("type");

		if (!typeNode.isTextual()) {
			throw new IllegalArgumentException("Action type is not a string");
		}

		try {
			final Class<?> clazz = getClass().getClassLoader().loadClass( typeNode.asText() );

			if (!Action.class.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException("Class " + clazz.getName() + " does not implement Action interface");
			}

			final ObjectMapper mapper = (ObjectMapper)parser.getCodec();
			return mapper.treeToValue(node, (Class<Action>) clazz);


		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

	}
}
