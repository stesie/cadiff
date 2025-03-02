package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.InsertNodeOnEdgePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record InsertNodeOnEdgeAction(String replaceFlowId, List<Step> steps) implements AddAction {

	@Override
	public Patcher patcher() {
		return new InsertNodeOnEdgePatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return steps.subList(1, steps.size() - 1).stream()
				.map(Step::id)
				.filter(id -> !id.equals(replaceFlowId))
				.toList();
	}

	@Override
	public List<String> idsRemoved() {
		if (steps.stream().anyMatch(step -> step.id().equals(replaceFlowId))) {
			return List.of();
		}

		return List.of(replaceFlowId);
	}

}
