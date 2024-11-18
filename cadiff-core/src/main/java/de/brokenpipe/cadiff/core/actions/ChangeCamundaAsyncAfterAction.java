package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaAsyncAfterPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public class ChangeCamundaAsyncAfterAction extends AbstractChangePropertyAction<Boolean> {
	@JsonCreator
	public ChangeCamundaAsyncAfterAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final Boolean oldValue,
			@JsonProperty("newValue") final Boolean newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaAsyncAfterPatcher(this);
	}
}
