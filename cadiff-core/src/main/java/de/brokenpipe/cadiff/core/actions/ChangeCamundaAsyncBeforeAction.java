package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaAsyncBeforePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

public class ChangeCamundaAsyncBeforeAction extends AbstractChangePropertyAction<Boolean> {
	public ChangeCamundaAsyncBeforeAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final Boolean oldValue,
			@JsonProperty("newValue") final Boolean newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaAsyncBeforePatcher(this);
	}
}
