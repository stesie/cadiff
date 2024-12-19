package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeOutputParameterPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeOutputParameterAction(String id, String name, Value oldValue, Value newValue) implements ChangePropertyAction<Value> {

	@Override
	public Patcher patcher() {
		return new ChangeOutputParameterPatcher(this);
	}

	@Override
	public String attributeName() {
		return "outputParam[%s]".formatted(name);
	}
}
