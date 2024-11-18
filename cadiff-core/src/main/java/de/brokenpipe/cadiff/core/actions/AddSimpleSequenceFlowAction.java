package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.patch.control.patchers.AddSimpleSequenceFlowPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddSimpleSequenceFlowAction(String id, String sourceId, String targetId, List<Waypoint> waypoints) implements AddAction  {
	@Override
	public Patcher getPatcher() {
		return new AddSimpleSequenceFlowPatcher(this);
	}

	@Override
	public List<String> getIdsAdded() {
		return List.of(id);
	}
}
