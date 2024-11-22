package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeExclusiveGatewayDefaultActionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeExclusiveGatewayDefaultAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeExclusiveGatewayDefaultActionPatcher(this);
	}

	@Override
	public String attributeName() {
		return "default";
	}
}
