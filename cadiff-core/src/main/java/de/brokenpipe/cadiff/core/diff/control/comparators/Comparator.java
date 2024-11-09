package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface Comparator extends BiFunction<FlowElement, FlowElement, Stream<Action>> {
}
