package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeSignalEventDefinitionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeSignalEventDefinitionAction(String id, String signalDefinitionId, String oldSignalRef,
												String newSignalRef) implements Action, ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeSignalEventDefinitionPatcher(this);
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	@JsonIgnore
	public String oldValue() {
		return oldSignalRef;
	}

	@Override
	@JsonIgnore
	public String newValue() {
		return newSignalRef;
	}

	@Override
	public String attributeName() {
		return "signalRef";
	}
}
