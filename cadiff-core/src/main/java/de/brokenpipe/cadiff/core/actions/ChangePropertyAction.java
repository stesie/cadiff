package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ChangePropertyAction<T> extends SingleIdRelatedAction {

	T oldValue();
	T newValue();

	@JsonIgnore
	default String attributeName() {
		final String attributeName = getClass().getSimpleName()
				.replace("Change", "")
				.replace("Camunda", "")
				.replace("Action", "");
		return Character.toLowerCase(attributeName.charAt(0)) + attributeName.substring(1);
	}
}
