package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeAttachedToPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

public class ChangeAttachedToAction extends AbstractChangePropertyAction<String> {

	public ChangeAttachedToAction(
			@JsonProperty("id") final String id,
			@JsonProperty("oldValue") final String oldValue,
			@JsonProperty("newValue") final String newValue) {
		super(id, oldValue, newValue);
	}

	@Override
	public Patcher getPatcher() {
		return new ChangeAttachedToPatcher(this);
	}
}
