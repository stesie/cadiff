package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddSignalPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddSignalAction(String id) implements AddAction  {
	@Override
	public Patcher patcher() {
		return new AddSignalPatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return List.of(id);
	}
}
