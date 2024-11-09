package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface Comparator extends BiFunction<BaseElement, BaseElement, Stream<Action>> {
}
