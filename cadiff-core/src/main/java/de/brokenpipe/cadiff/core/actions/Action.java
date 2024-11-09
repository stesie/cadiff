package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "type", "id" })
public interface Action {

	default String getType() {
		return this.getClass().getName();
	}
}
