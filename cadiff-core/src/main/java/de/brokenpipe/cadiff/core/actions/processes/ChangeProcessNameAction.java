package de.brokenpipe.cadiff.core.actions.processes;

import de.brokenpipe.cadiff.core.actions.AbstractChangePropertyAction;

public class ChangeProcessNameAction extends AbstractChangePropertyAction<String> {
	public ChangeProcessNameAction(final String id, final String oldValue, final String newValue) {
		super(id, oldValue, newValue);
	}
}
