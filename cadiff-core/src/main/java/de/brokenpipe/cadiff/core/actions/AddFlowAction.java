package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddFlowPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;
import java.util.Optional;

public record AddFlowAction(Optional<String> attachedToId, boolean finalElementIsNew, List<Step> steps)
		implements AddAction {

	@Override
	public Patcher patcher() {
		return new AddFlowPatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return steps.subList(0, steps.size() - (finalElementIsNew ? 0 : 1)).stream()
				.map(Step::id)
				.toList();
	}

}
