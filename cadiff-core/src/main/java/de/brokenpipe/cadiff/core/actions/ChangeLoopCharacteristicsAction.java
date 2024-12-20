package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeLoopCharacteristicsPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeLoopCharacteristicsAction(String id, Boolean oldValue, Boolean newValue) implements ChangePropertyAction<Boolean> {

	@Override
	public Patcher patcher() {
		return new ChangeLoopCharacteristicsPatcher(this);
	}

	@Override
	public String attributeName() {
		return "multi-instance";
	}

}
