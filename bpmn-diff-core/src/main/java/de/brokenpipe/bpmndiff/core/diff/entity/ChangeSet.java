package de.brokenpipe.bpmndiff.core.diff.entity;

import de.brokenpipe.bpmndiff.core.actions.Action;

import java.util.List;

public record ChangeSet(List<Action> changes) {
}
