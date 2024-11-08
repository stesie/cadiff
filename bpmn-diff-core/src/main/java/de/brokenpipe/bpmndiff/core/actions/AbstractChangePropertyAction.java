package de.brokenpipe.bpmndiff.core.actions;

import lombok.Data;

@Data
public abstract class AbstractChangePropertyAction<T> implements Action {

	private final String id;
	private final T oldValue;
	private final T newValue;

}
