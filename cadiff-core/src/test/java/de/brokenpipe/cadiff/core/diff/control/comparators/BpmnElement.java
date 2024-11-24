package de.brokenpipe.cadiff.core.diff.control.comparators;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(BpmnElement.Extension.class)
public @interface BpmnElement {

	String file();

	String id();

	class Extension implements ParameterResolver {

		@Override
		public boolean supportsParameter(final ParameterContext parameterContext,
				final ExtensionContext extensionContext)
				throws ParameterResolutionException {
			return parameterContext.getParameter().getType().equals(BaseElement.class);
		}

		@Override
		public Object resolveParameter(final ParameterContext parameterContext,
				final ExtensionContext extensionContext)
				throws ParameterResolutionException {

			final BpmnElement annotation = parameterContext.findAnnotation(BpmnElement.class)
					.orElseThrow(() -> new IllegalStateException("No annotation found"));

			try (final var is = BpmnElement.class.getClassLoader().getResourceAsStream(annotation.file())) {
				final var bpmn = Bpmn.readModelFromStream(is);
				final ModelElementInstance element = bpmn.getModelElementById(annotation.id());

				if (element == null) {
					throw new IllegalArgumentException("Element with id " + annotation.id() + " not found in file " + annotation.file());
				}

				return element;
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
