package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.control.voters.exceptions.VetoVoteException;
import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

public class ElementTypeVoter implements Voter {

	@Override
	public Vote apply(final String removeId, final String addId, final VoteContext<String, ? extends BaseElement> context)
			throws VetoVoteException {
		final var removed = context.fromMap().get(removeId);
		final var added = context.toMap().get(addId);

		if (removed.getClass().equals(added.getClass())) {
			return Vote.NEUTRAL;
		}

		throw new VetoVoteException();
	}

}
