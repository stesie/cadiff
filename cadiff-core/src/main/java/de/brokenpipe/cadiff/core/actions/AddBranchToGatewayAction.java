package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddBranchToGatewayPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

import java.util.List;

public record AddBranchToGatewayAction(List<Step> steps) implements AddAction {

	@Override
	public Patcher getPatcher() {
		return new AddBranchToGatewayPatcher(this);
	}

	@Override
	public List<String> getIdsAdded() {
		return steps.subList(1, steps.size() - 1).stream()
				.map(Step::id)
				.toList();
	}

}
