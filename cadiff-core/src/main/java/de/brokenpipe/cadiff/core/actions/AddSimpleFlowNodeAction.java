package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.patch.control.patchers.AddSimpleFlowNodePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddSimpleFlowNodeAction(String id, String elementTypeName, Bounds bounds) implements AddAction  {
	@Override
	public Patcher patcher() {
		return new AddSimpleFlowNodePatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return List.of(id);
	}
}
