package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.RemoveEventDefinitionPatcher;

public record RemoveEventDefinitionAction(String id, String eventDefinitionId) implements SingleIdRelatedAction {
	@Override
	public Patcher patcher() {
		return new RemoveEventDefinitionPatcher(id, eventDefinitionId);
	}
}
