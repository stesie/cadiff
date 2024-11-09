package de.brokenpipe.cadiff.core;

import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.di.DiagramElement;
import org.camunda.bpm.model.bpmn.instance.di.Shape;

public record Bounds(Double x, Double y, Double width, Double height) {
	public static Bounds of(final DiagramElement di) {
		if (di instanceof final Shape shape) {
			return Bounds.of(shape.getBounds());
		}

		throw new NotImplementedException();
	}

	private static Bounds of(final org.camunda.bpm.model.bpmn.instance.dc.Bounds bounds) {
		return new Bounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}
}
