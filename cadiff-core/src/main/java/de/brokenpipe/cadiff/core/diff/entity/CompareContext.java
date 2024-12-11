package de.brokenpipe.cadiff.core.diff.entity;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.function.Function;

public record CompareContext<T>(BpmnModelInstance fromInstance, T from, T to) {
	public <R> CompareContext<R> map(final Function<T, R> mapFn) {
		return new CompareContext<>(fromInstance, mapFn.apply(from), mapFn.apply(to));
	}
}
