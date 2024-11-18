package de.brokenpipe.cadiff.cli.control;

import de.brokenpipe.cadiff.cli.control.exceptions.ElementSetMismatchException;
import de.brokenpipe.cadiff.cli.control.exceptions.SelftestException;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import de.brokenpipe.cadiff.core.patch.boundary.PatchCommand;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.fusesource.jansi.Ansi;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.fusesource.jansi.Ansi.ansi;

public class SelftestControl {

	private final BpmnModelInstance test;
	private final BpmnModelInstance expectation;
	private final ChangeSet changeSet;

	private final Set<ModelElementType> ignoreIfEmpty;

	public SelftestControl(final BpmnModelInstance from, final BpmnModelInstance to, final ChangeSet changeSet) {
		this.test = from.clone();
		this.expectation = to.clone();
		this.changeSet = changeSet;

		ignoreIfEmpty = Set.of(expectation.getModel().getType(ExtensionElements.class));
	}

	public void execute() {

		new PatchCommand(test, changeSet).execute();

		expectation.getModelElementsByType(Process.class).forEach(this::checkProcess);

		System.out.println();
		System.out.println(ansi().fg(Ansi.Color.GREEN).a("Selftest successful.").reset());
	}

	private void checkProcess(final Process processExpected) {
		final Process processActual = test.getModelElementById(processExpected.getId());

		if (processActual == null) {
			throw new SelftestException("Process missing in test instance", processExpected.getId());
		}

		final Collection<FlowElement> flowElementsExpected = processExpected.getFlowElements();
		final Collection<FlowElement> flowElementsActual = processActual.getFlowElements();

		compareElements(processExpected.getId(), flowElementsExpected, flowElementsActual);
	}

	private void compareElements(final String containerId, final Collection<? extends BaseElement> flowElementsExpected,
			final Collection<? extends BaseElement> flowElementsActual) {
		final var flowElementIdsExpected = flowElementsExpected.stream().map(BaseElement::getId).toList();
		final var flowElementIdsActual = flowElementsActual.stream().map(BaseElement::getId).toList();

		final var flowElementIdsMissing = flowElementIdsExpected.stream()
				.filter(id -> !flowElementIdsActual.contains(id))
				.toList();
		final var flowElementIdsExcess = flowElementIdsActual.stream()
				.filter(id -> !flowElementIdsExpected.contains(id))
				.toList();

		if (!flowElementIdsMissing.isEmpty() || !flowElementIdsExcess.isEmpty()) {
			throw new ElementSetMismatchException(containerId, flowElementIdsMissing, flowElementIdsExcess);
		}

		flowElementsExpected.forEach(feExpected -> {
			final var feActual = test.getModelElementById(feExpected.getId());
			compareElements(feExpected.getDomElement(), feActual.getDomElement(), "#" + feExpected.getId());
		});
	}

	private void compareElements(final DomElement expected, final DomElement actual, final String path) {
		compareString(expected.getNamespaceURI(), actual.getNamespaceURI(), path + "/@namespaceURI");
		compareString(expected.getLocalName(), actual.getLocalName(), path + "/@localName");

		final ModelElementType type = expectation.getModel()
				.getTypeForName(expected.getNamespaceURI(), expected.getLocalName());

		compareAttributes(type, expected, actual, path);

		// ExtensionElements has child elements, despite the type registry listing nothing
		if (type.getBaseType() == null && !type.equals(expectation.getModel().getType(ExtensionElements.class))) {
			compareString(expected.getTextContent(), actual.getTextContent(), path + "/@text");
			return;
		}

		compareChildren(expected, actual, path);
	}

	private void compareChildren(final DomElement expected, final DomElement actual, final String path) {
		ignoreIfEmpty.forEach(type -> {
			Stream.of(expected, actual)
					.flatMap(el -> el.getChildElements().stream())
					.filter(c -> c.getChildElements().isEmpty())
					.filter(c -> type.getTypeNamespace().equals(c.getNamespaceURI()))
					.filter(c -> type.getTypeName().equals(c.getLocalName()))
					.forEach(c -> c.getParentElement().removeChild(c));
		});

		if (expected.getChildElements().size() != actual.getChildElements().size()) {
			throw new SelftestException("Number of child elements mismatching", path);
		}

		for (int i = 0; i < expected.getChildElements().size(); i++) {
			final DomElement expectedChild = expected.getChildElements().get(i);
			final DomElement actualChild = actual.getChildElements().get(i);
			compareElements(expectedChild, actualChild, path + "/" + expectedChild.getLocalName());
		}
	}

	private void compareAttributes(final ModelElementType type, final DomElement expected, final DomElement actual,
			final String path) {

		type.getAttributes().forEach(attribute -> {
			final var defaultValue = Optional.ofNullable(attribute.getDefaultValue())
					.map(Object::toString)
					.orElse(null);

			final var expectedValue = Optional.ofNullable(expected.getAttribute(attribute.getAttributeName()))
					.orElse(defaultValue);
			final var actualValue = Optional.ofNullable(actual.getAttribute(attribute.getAttributeName()))
					.orElse(defaultValue);

			compareString(expectedValue, actualValue, path + "/@" + attribute.getAttributeName());
		});
	}

	private void compareString(final String expected, final String actual, final String path) {
		if (!Objects.equals(expected, actual)) {
			throw new SelftestException("Strings mismatch", path, expected, actual);
		}
	}
}
