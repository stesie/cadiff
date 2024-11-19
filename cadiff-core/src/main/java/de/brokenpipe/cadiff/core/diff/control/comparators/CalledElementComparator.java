package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCalledElementAction;
import org.camunda.bpm.model.bpmn.instance.CallActivity;

import java.util.stream.Stream;

public class CalledElementComparator extends UpcastComperator<CallActivity>
		implements StringPropertyComparator<CallActivity> {

	@Override
	protected Class<CallActivity> getClassType() {
		return CallActivity.class;
	}

	@Override
	protected Stream<Action> compare(final CallActivity from, final CallActivity to) {
		return compareStringProperty(CallActivity::getCalledElement,
				ChangeCalledElementAction.class, from, to);
	}
}
