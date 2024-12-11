package de.brokenpipe.cadiff.core.diff.control;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(BpmnFile.Extension.class)
public @interface BpmnFile {

	String value();

	class Extension implements ParameterResolver {

		@Override
		public boolean supportsParameter(final ParameterContext parameterContext,
				final ExtensionContext extensionContext)
				throws ParameterResolutionException {
			return parameterContext.getParameter().getType().equals(BpmnModelInstance.class);
		}

		@Override
		public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
				throws ParameterResolutionException {

			final BpmnFile annotation = parameterContext.findAnnotation(BpmnFile.class)
					.orElseThrow(() -> new IllegalStateException("No annotation found"));

			try (final var is = BpmnFile.class.getClassLoader().getResourceAsStream(annotation.value())) {
				return Bpmn.readModelFromStream(is);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
