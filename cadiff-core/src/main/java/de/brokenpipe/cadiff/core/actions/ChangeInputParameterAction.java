package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeInputParameterPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeInputParameterAction(String id, String name, Value oldValue, Value newValue) implements ChangePropertyAction<Value> {

	@Override
	public Patcher patcher() {
		return new ChangeInputParameterPatcher(this);
	}

	@Override
	public String attributeName() {
		return "inputParam[%s]".formatted(name);
	}
}
