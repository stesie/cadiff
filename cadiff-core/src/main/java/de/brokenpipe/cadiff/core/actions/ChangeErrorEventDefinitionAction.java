package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeErrorEventDefinitionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeErrorEventDefinitionAction(String id, String errorDefinitionId, String oldErrorRef,
											   String newErrorRef) implements Action, ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeErrorEventDefinitionPatcher(this);
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	@JsonIgnore
	public String oldValue() {
		return newErrorRef;
	}

	@Override
	@JsonIgnore
	public String newValue() {
		return newErrorRef;
	}

	@Override
	public String attributeName() {
		return "errorRef";
	}
}
