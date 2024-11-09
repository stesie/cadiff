package de.brokenpipe.cadiff.core;

public record Waypoint(Double x, Double y) {
	public static Waypoint of(final org.camunda.bpm.model.bpmn.instance.di.Waypoint di) {
		return new Waypoint(di.getX(), di.getY());
	}

}
