package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.List;

public interface AddAction extends Action {

	@JsonIgnore
	List<String> getIdsAdded();

	@JsonIgnore
	default List<String> getIdsRemoved() {
		return Collections.emptyList();
	}

}
