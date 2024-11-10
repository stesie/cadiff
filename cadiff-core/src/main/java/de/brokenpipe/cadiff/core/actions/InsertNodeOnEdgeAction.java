package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.patch.control.patchers.InsertNodeOnEdgePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

import java.util.List;
import java.util.Optional;

public record InsertNodeOnEdgeAction(String replaceFlowId, List<Step> steps) implements AddAction {

	@Override
	public Patcher getPatcher() {
		return new InsertNodeOnEdgePatcher(this);
	}

	@Override
	public List<String> getIdsAdded() {
		return steps.subList(1, steps.size() - 1).stream()
				.map(Step::id)
				.filter(id -> !id.equals(replaceFlowId))
				.toList();
	}

	@Override
	public List<String> getIdsRemoved() {
		if (steps.stream().anyMatch(step -> step.id().equals(replaceFlowId))) {
			return List.of();
		}

		return List.of(replaceFlowId);
	}

	public record Step(String id, String elementTypeName, Optional<Bounds> bounds, Optional<List<Waypoint>> waypoints) {
	}
}
