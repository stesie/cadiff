package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface AddAction extends Action {

	@JsonIgnore
	List<String> getIds();
}
