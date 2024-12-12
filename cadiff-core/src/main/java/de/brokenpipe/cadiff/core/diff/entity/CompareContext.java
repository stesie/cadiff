package de.brokenpipe.cadiff.core.diff.entity;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;

import java.util.function.Function;

public record CompareContext<T>(BpmnModelInstance fromInstance, BpmnModelElementInstance fromContainer, T from, T to) {
	public <R> CompareContext<R> map(final Function<T, R> mapFn) {
		return new CompareContext<>(fromInstance, fromContainer, mapFn.apply(from), mapFn.apply(to));
	}

	public CompareContext<T> withFromContainer(final BpmnModelElementInstance container) {
		return new CompareContext<>(fromInstance, container, from, to);
	}

}
