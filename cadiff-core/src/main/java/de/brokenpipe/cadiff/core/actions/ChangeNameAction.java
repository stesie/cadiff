package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeNamePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public class ChangeNameAction extends AbstractChangePropertyAction<String> {
	public ChangeNameAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final String oldValue,
			@JsonProperty("newValue") final String newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeNamePatcher(this);
	}
}
