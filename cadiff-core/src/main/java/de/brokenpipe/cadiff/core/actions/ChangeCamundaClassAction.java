package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaClassPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

public class ChangeCamundaClassAction extends AbstractChangePropertyAction<String> {
	public ChangeCamundaClassAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final String oldValue,
			@JsonProperty("newValue") final String newValue
	) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaClassPatcher(this);
	}
}
