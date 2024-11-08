package de.brokenpipe.bpmndiff.core.actions;

import lombok.Data;

@Data
public abstract class AbstractChangeStringPropertyAction implements Action {

	private final String id;
	private final String oldValue;
	private final String newValue;

}
