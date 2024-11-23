package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeInMappingAllPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeInMappingAllAction(String id, Config oldValue, Config newValue) implements SingleIdRelatedAction {

	@Override
	public Patcher patcher() {
		return new ChangeInMappingAllPatcher(this);
	}

	public record Config(Boolean enabled, Boolean local) {
		public static Config disabled() {
			return new Config(Boolean.FALSE, null);
		}
	}
}
