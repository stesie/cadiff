package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.core.actions.Action;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ActionPrinterTest {

	@ParameterizedTest
	@MethodSource("getActionClasses")
	void shouldHaveActionPrinter(final Class<? extends Action> actionClass) {
		final Action action = mock(actionClass);
		final Optional<ActionPrinter> actionPrinter = ActionPrinterRegistry.INSTANCE.find(action);

		assertTrue(actionPrinter.isPresent(), "No action printer found for action " + actionClass);
	}

	static Stream<Class<? extends Action>> getActionClasses() {
		final Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forJavaClassPath())
				.setScanners(Scanners.SubTypes));

		return reflections.getSubTypesOf(Action.class).stream()
				.filter(clazz -> !clazz.isInterface());
	}

}