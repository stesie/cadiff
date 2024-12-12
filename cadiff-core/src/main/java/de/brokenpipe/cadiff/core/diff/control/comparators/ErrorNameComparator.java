package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeErrorNameAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.Error;

import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.comparators.PropertyComparator.compareStringProperty;

public class ErrorNameComparator extends UpcastComparator<Error> {

	@Override
	protected Class<Error> getClassType() {
		return Error.class;
	}

	@Override
	public Stream<Action> compare(final CompareContext<Error> compareContext) {
		return compareStringProperty(Error::getName, ChangeErrorNameAction.class, compareContext);
	}

}
