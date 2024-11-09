package de.brokenpipe.cadiff.core.diff.entity;

import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.Map;
import java.util.Set;

public record VoteContext<T extends BaseElement> (Map<String, T> fromMap, Map<String, T> toMap,
						  Set<String> updated, Set<String> removed, Set<String> added) {
}
