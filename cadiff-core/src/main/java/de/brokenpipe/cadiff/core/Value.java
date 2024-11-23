package de.brokenpipe.cadiff.core;

import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaList;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaValue;
import org.camunda.bpm.model.xml.instance.DomElement;

import java.util.List;
import java.util.function.Consumer;

public interface Value extends Consumer<BpmnModelElementInstance> {

	static Value of(final BpmnModelElementInstance element) {
		final var listChildren = element.getChildElementsByType(CamundaList.class);

		if (!listChildren.isEmpty()) {
			if (listChildren.size() > 1) {
				throw new IllegalStateException("Multiple CamundaList elements found");
			}

			return new ListValue(listChildren.iterator().next().getValues().stream()
					.map(BpmnModelElementInstance::getTextContent)
					.toList());
		}

		return new StringValue(element.getTextContent());
	}

	record StringValue(String value) implements Value {

		@Override
		public void accept(final BpmnModelElementInstance elementInstance) {
			elementInstance.getDomElement().setTextContent(value);
		}

		@Override
		public String toString() {
			return value;
		}
	}

	record ListValue(List<String> values) implements Value {

		@Override
		public void accept(final BpmnModelElementInstance elementInstance) {

			final var list = elementInstance.getModelInstance().newInstance(CamundaList.class);
			list.getValues().addAll(values.stream()
					.map(value -> {
						final var newElement = elementInstance.getModelInstance().newInstance(CamundaValue.class);
						newElement.setTextContent(value);
						return newElement;
					})
					.toList());

			final DomElement domElement = elementInstance.getDomElement();
			domElement.getChildElements().forEach(domElement::removeChild);
			domElement.insertChildElementAfter(list.getDomElement(), null);
		}

		@Override
		public String toString() {
			return String.join(", ", values);
		}
	}
}
