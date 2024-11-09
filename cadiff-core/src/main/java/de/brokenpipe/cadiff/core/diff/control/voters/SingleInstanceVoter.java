package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

public class SingleInstanceVoter implements Voter {

	@Override
	public Vote apply(final String removeId, final String addId, final VoteContext<? extends BaseElement> context) {
		if (context.removed().size() == 1 && context.added().size() == 1 && context.updated().isEmpty()) {
			return Vote.UP;
		}

		return Vote.NEUTRAL;
	}

}
