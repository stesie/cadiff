package de.brokenpipe.bpmndiff.core.diff.control.comparators;

import de.brokenpipe.bpmndiff.core.actions.Action;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface Comparator extends BiFunction<FlowElement, FlowElement, Stream<Action>> {
	// Stream<Action> compare(FlowElement from, FlowElement to);
}
