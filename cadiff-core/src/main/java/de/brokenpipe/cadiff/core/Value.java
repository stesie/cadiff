package de.brokenpipe.cadiff.core;

import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaEntry;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaList;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaMap;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaValue;
import org.camunda.bpm.model.xml.instance.DomElement;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

		final var mapChildren = element.getChildElementsByType(CamundaMap.class);

		if (!mapChildren.isEmpty()) {
			if (mapChildren.size() > 1) {
				throw new IllegalStateException("Multiple CamundaMap elements found");
			}

			return new MapValue(mapChildren.iterator().next().getCamundaEntries().stream()
					.collect(Collectors.toMap(CamundaEntry::getCamundaKey, BpmnModelElementInstance::getTextContent)));
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

	record MapValue(Map<String, String> entries) implements Value {

		@Override
		public void accept(final BpmnModelElementInstance elementInstance) {
			final var map = elementInstance.getModelInstance().newInstance(CamundaMap.class);

			map.getCamundaEntries().addAll(entries.entrySet().stream()
					.map(entry -> {
						final var newElement = elementInstance.getModelInstance().newInstance(CamundaEntry.class);
						newElement.setCamundaKey(entry.getKey());
						newElement.setTextContent(entry.getValue());
						return newElement;
					})
					.toList());

			final DomElement domElement = elementInstance.getDomElement();
			domElement.getChildElements().forEach(domElement::removeChild);
			domElement.insertChildElementAfter(map.getDomElement(), null);
		}

		@Override
		public String toString() {
			return entries.entrySet().stream()
					.map(entry -> entry.getKey() + "=" + entry.getValue())
					.collect(Collectors.joining(", "));
		}
	}
}
