package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeSignalNameAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.Signal;

import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.comparators.PropertyComparator.compareStringProperty;

public class SignalNameComparator extends UpcastComparator<Signal> {

	@Override
	protected Class<Signal> getClassType() {
		return Signal.class;
	}

	@Override
	public Stream<Action> compare(final CompareContext<Signal> compareContext) {
		return compareStringProperty(Signal::getName, ChangeSignalNameAction.class, compareContext);
	}

}
