package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeInMappingBusinessKeyPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeCamundaBusinessKeyAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeInMappingBusinessKeyPatcher(this);
	}
}
