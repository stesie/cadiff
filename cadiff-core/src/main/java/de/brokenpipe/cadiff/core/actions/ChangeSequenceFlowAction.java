package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeSequenceFlowPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeSequenceFlowAction(String id, String oldSourceId, String newSourceId, String oldTargetId, String newTargetId) implements Action {
	@Override
	public Patcher getPatcher() {
		return new ChangeSequenceFlowPatcher(this);
	}
}
