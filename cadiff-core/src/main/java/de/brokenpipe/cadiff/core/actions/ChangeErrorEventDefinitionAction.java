package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeErrorEventDefinitionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeErrorEventDefinitionAction(String id, String errorDefinitionId, String oldErrorRef,
											   String newErrorRef) implements Action, ChangePropertyAction<String> {

	@Override
	public Patcher getPatcher() {
		return new ChangeErrorEventDefinitionPatcher(this);
	}

	@Override
	@JsonIgnore
	public String getId() {
		return id;
	}

	@Override
	@JsonIgnore
	public String getOldValue() {
		return newErrorRef;
	}

	@Override
	@JsonIgnore
	public String getNewValue() {
		return newErrorRef;
	}

	@Override
	public String getAttributeName() {
		return "errorRef";
	}
}
