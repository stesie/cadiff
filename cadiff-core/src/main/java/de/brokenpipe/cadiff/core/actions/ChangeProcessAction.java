package de.brokenpipe.cadiff.core.actions;

import java.util.Collection;

public record ChangeProcessAction(String id, Collection<Action> actions) implements Action {
}
