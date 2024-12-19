package de.brokenpipe.cadiff.core.diff.entity;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;

import java.util.function.Function;

public record CompareContext<T>(BpmnModelInstance fromInstance, BpmnModelElementInstance fromContainer, String id, T from, T to) {

	public CompareContext(final BpmnModelInstance fromInstance, final BpmnModelElementInstance fromContainer, final T from, final T to) {
		this(fromInstance, fromContainer, to instanceof final BaseElement toEl ? toEl.getId() : null, from, to);
	}

	public <R> CompareContext<R> map(final Function<T, R> mapFn) {
		return new CompareContext<>(fromInstance, fromContainer, mapFn.apply(from), mapFn.apply(to));
	}

	public <R> CompareContext<R> mapValue(final Function<T, R> mapFn) {
		return new CompareContext<>(fromInstance, fromContainer, id, mapFn.apply(from), mapFn.apply(to));
	}

	public CompareContext<T> withFromContainer(final BpmnModelElementInstance container) {
		return new CompareContext<>(fromInstance, container, from, to);
	}

}
