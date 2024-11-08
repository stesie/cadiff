package de.brokenpipe.bpmndiff.core.actions;

public record ChangeNameAction(String id, String oldValue, String newValue) implements Action {
}
