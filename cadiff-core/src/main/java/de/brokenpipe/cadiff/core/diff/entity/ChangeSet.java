package de.brokenpipe.cadiff.core.diff.entity;

import de.brokenpipe.cadiff.core.actions.Action;

import java.util.List;

public record ChangeSet(List<Action> changes) {
}
