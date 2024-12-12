package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddErrorPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddErrorAction(String id) implements AddAction  {
	@Override
	public Patcher patcher() {
		return new AddErrorPatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return List.of(id);
	}
}
