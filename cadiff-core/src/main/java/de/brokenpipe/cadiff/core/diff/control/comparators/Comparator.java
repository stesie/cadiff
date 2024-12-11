package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Comparator extends Function<CompareContext<? extends BaseElement>, Stream<Action>> {
}
