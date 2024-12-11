package de.brokenpipe.cadiff.core.diff.entity;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.function.Function;

public record CompareContext<T>(BpmnModelInstance fromInstance, BaseElement fromContainer, T from, T to) {
	public <R> CompareContext<R> map(final Function<T, R> mapFn) {
		return new CompareContext<>(fromInstance, fromContainer, mapFn.apply(from), mapFn.apply(to));
	}

	public CompareContext<T> withFromContainer(final BaseElement container) {
		return new CompareContext<>(fromInstance, container, from, to);
	}

}
