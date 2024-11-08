package de.brokenpipe.bpmndiff.core.diff.control.comparators;

import de.brokenpipe.bpmndiff.core.actions.Action;
import de.brokenpipe.bpmndiff.core.actions.ChangeNameAction;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.stream.Stream;

public class NameComparator implements Comparator {
	@Override
	public Stream<Action> apply(final FlowElement from, final FlowElement to) {
		if (from.getName() == null && to.getName() == null) {
			return Stream.empty();
		}

		if (from.getName() == null) {
			return Stream.of(new ChangeNameAction(to.getId(), null, to.getName()));
		}

		if (from.getName().equals(to.getName())) {
			return Stream.empty();
		}

		return Stream.of(new ChangeNameAction(from.getId(), from.getName(), to.getName()));
	}

}
