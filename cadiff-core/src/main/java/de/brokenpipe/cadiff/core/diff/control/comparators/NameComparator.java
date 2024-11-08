package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.stream.Stream;

public class NameComparator implements StringPropertyComparator<FlowElement> {
	@Override
	public Stream<Action> apply(final FlowElement from, final FlowElement to) {
		return compareStringProperty(FlowElement::getName, ChangeNameAction.class, from, to);
	}

}
