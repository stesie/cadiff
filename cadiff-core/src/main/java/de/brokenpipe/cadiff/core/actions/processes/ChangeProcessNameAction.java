package de.brokenpipe.cadiff.core.actions.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.actions.AbstractChangePropertyAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.processes.ChangeProcessNamePatcher;

public class ChangeProcessNameAction extends AbstractChangePropertyAction<String> {
	public ChangeProcessNameAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final String oldValue,
			@JsonProperty("newValue") final String newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeProcessNamePatcher(this);
	}
}
