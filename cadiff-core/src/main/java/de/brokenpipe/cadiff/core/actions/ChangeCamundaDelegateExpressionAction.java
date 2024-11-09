package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaDelegateExpressionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

public class ChangeCamundaDelegateExpressionAction extends AbstractChangePropertyAction<String> {

	public ChangeCamundaDelegateExpressionAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final String oldValue,
			@JsonProperty("newValue") final String newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaDelegateExpressionPatcher(this);
	}
}
