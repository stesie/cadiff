package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

public class SingleInstanceVoter implements Voter {

	@Override
	public Vote apply(final String removeId, final String addId, final VoteContext<String, ? extends BaseElement> context) {
		if (context.removed().size() == 1 && context.added().size() == 1 && context.updated().isEmpty()) {
			return Vote.UP;
		}

		final var removedType = context.fromMap().get(removeId).getElementType();
		final var addedType = context.toMap().get(addId).getElementType();

		if (!removedType.equals(addedType)) {
			return Vote.DOWN;
		}

		if (context.fromMap().values().stream()
				.filter(x -> x.getElementType().equals(removedType))
				.count() > 1) {
			return Vote.NEUTRAL;
		}

		if (context.toMap().values().stream()
				.filter(x -> x.getElementType().equals(removedType))
				.count() > 1) {
			return Vote.NEUTRAL;
		}

		return Vote.UP;
	}

}
