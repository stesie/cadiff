package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaExpressionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public class ChangeCamundaExpressionAction extends AbstractChangePropertyAction<String> {
	public ChangeCamundaExpressionAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final String oldValue,
			@JsonProperty("newValue") final String newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaExpressionPatcher(this);
	}
}
