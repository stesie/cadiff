package de.brokenpipe.bpmndiff.core.actions;

public class ChangeNameAction extends AbstractChangePropertyAction<String> {
	public ChangeNameAction(final String id, final String oldValue, final String newValue) {
		super(id, oldValue, newValue);
	}
}
