package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.Waypoint;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface AddAction extends Action {

	@JsonIgnore
	List<String> idsAdded();

	@JsonIgnore
	default List<String> idsRemoved() {
		return Collections.emptyList();
	}

	record Step(String id, String elementTypeName, Optional<Bounds> bounds, Optional<List<Waypoint>> waypoints) {
	}
}
