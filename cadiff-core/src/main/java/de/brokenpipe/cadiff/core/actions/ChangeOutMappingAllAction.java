package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeOutMappingAllPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeOutMappingAllAction(String id, Config oldValue, Config newValue) implements SingleIdRelatedAction {

	@Override
	public Patcher patcher() {
		return new ChangeOutMappingAllPatcher(this);
	}

	public record Config(boolean enabled, Boolean local) {
		public static Config disabled() {
			return new Config(false, null);
		}
	}
}
