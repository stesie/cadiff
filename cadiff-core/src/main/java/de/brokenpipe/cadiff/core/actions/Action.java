package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

@JsonPropertyOrder({ "type", "id" })
@JsonIgnoreProperties( value = { "type" }, allowGetters = true)
public interface Action {

	@JsonIgnore
	Patcher getPatcher();

	@JsonProperty
	default String getType() {
		return this.getClass().getName();
	}

}
