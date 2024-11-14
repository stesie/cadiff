package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ChangePropertyAction<T> extends SingleIdRelatedAction {

	T getOldValue();
	T getNewValue();

	@JsonIgnore
	String getAttributeName();
}
