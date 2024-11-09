package de.brokenpipe.cadiff.cli.entity;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionPrintContext {

	final List<Action> changes;
	final BpmnModelInstance to;

	public static ActionPrintContext of(final ChangeSet changeSet, final BpmnModelInstance to) {
		return new ActionPrintContext(new ArrayList<>(changeSet.changes()), to);
	}
}
