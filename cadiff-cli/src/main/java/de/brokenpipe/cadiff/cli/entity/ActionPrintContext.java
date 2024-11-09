package de.brokenpipe.cadiff.cli.entity;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionPrintContext {

	final List<Action> changes;
	final BpmnModelInstance to;
	final boolean printIdChanges;

	public static ActionPrintContext of(final ChangeSet changeSet, final BpmnModelInstance to, final boolean printIdChanges) {
		return new ActionPrintContext(new ArrayList<>(changeSet.changes()), to, printIdChanges);
	}

	public Stream<SingleIdRelatedAction> findChangesForId(final String id) {
		return changes.stream()
				.filter(c -> c instanceof SingleIdRelatedAction)
				.map(SingleIdRelatedAction.class::cast)
				.filter(c -> c.getId().equals(id));
	}
}
