package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddBoundaryEventBranchPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddBoundaryEventBranchAction(String attachedToId, boolean finalElementIsNew, List<Step> steps)
		implements AddAction {

	@Override
	public Patcher getPatcher() {
		return new AddBoundaryEventBranchPatcher(this);
	}

	@Override
	public List<String> getIdsAdded() {
		return steps.subList(0, steps.size() - (finalElementIsNew ? 0 : 1)).stream()
				.map(Step::id)
				.toList();
	}

}
