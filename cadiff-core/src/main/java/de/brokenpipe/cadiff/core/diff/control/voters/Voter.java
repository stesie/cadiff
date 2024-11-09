package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.control.voters.exceptions.VetoVoteException;
import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

@FunctionalInterface
public interface Voter {
	Vote apply(String removeId, String addId, VoteContext<? extends BaseElement> context) throws VetoVoteException;
}
