package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaExclusivePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public class ChangeCamundaExclusiveAction extends AbstractChangePropertyAction<Boolean> {
	public ChangeCamundaExclusiveAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final Boolean oldValue,
			@JsonProperty("newValue") final Boolean newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaExclusivePatcher(this);
	}
}
