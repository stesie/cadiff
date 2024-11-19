package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddBranchToGatewayPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddBranchToGatewayAction(boolean finalElementIsNew, List<Step> steps) implements AddAction {

	@Override
	public Patcher patcher() {
		return new AddBranchToGatewayPatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return steps.subList(1, steps.size() - (finalElementIsNew ? 0 : 1)).stream()
				.map(Step::id)
				.toList();
	}

}
