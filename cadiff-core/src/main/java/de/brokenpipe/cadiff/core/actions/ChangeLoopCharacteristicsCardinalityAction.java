package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeLoopCharacteristicsCardinalityPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeLoopCharacteristicsCardinalityAction(String id, String oldValue, String newValue)
		implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeLoopCharacteristicsCardinalityPatcher(this);
	}

	@Override
	public String attributeName() {
		return "[multi] loopCardinality";
	}

}
