package de.brokenpipe.cadiff.core.actions;

import lombok.Data;

@Data
public abstract class AbstractChangePropertyAction<T> implements SingleIdRelatedAction {

	private final String id;
	private final T oldValue;
	private final T newValue;

}
