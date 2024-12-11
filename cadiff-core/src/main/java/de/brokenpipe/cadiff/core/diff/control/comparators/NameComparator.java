package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.stream.Stream;

public class NameComparator extends UpcastComparator<FlowElement> implements StringPropertyComparator<FlowElement> {

	@Override
	protected Class<FlowElement> getClassType() {
		return FlowElement.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<FlowElement> compareContext) {
		return compareStringProperty(FlowElement::getName, ChangeNameAction.class, compareContext);
	}

}
