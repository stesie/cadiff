package de.brokenpipe.cadiff.core.actions;

import lombok.Data;

@Data
public abstract class AbstractChangePropertyAction<T> implements ChangePropertyAction<T>, SingleIdRelatedAction {

	private final String id;
	private final T oldValue;
	private final T newValue;

	@Override
	public String getAttributeName() {
		final String attributeName = getClass().getSimpleName()
				.replace("Change", "")
				.replace("Camunda", "")
				.replace("Action", "");
		return Character.toLowerCase(attributeName.charAt(0)) + attributeName.substring(1);
	}
}
