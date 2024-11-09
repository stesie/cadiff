package de.brokenpipe.cadiff.core.actions;

public record ChangeIdAction(String oldId, String newId) implements Action {
}
