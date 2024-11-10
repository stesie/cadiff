package de.brokenpipe.cadiff.cli.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import de.brokenpipe.cadiff.core.actions.Action;

public class Jackson {

	public static final ObjectMapper MAPPER = Jackson.createObjectMapper();

	public static ObjectMapper createObjectMapper() {
		final SimpleModule cadiffModule = new SimpleModule();
		cadiffModule.addDeserializer(Action.class, new ActionDeserializer());

		return new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
				.registerModule(new Jdk8Module())
				.registerModule(cadiffModule);
	}
}
