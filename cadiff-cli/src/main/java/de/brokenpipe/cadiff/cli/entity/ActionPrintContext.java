package de.brokenpipe.cadiff.cli.entity;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeIdAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionPrintContext {

	final List<Action> changes;
	final Map<String, String> idMapForward;
	final Map<String, String> idMapBackward;
	final BpmnModelInstance from;
	final BpmnModelInstance to;
	final boolean printIdChanges;
	final boolean printAllEdgeDeletes;

	public static ActionPrintContext of(final ChangeSet changeSet, final BpmnModelInstance from,
			final BpmnModelInstance to, final boolean printIdChanges, final boolean printAllEdgeDeletes) {
		final Map<String, String> idMapForward = changeSet.changes().stream()
				.filter(c -> c instanceof ChangeIdAction)
				.map(ChangeIdAction.class::cast)
				.collect(Collectors.toMap(ChangeIdAction::oldId, ChangeIdAction::newId));
		final Map<String, String> idMapBackward = idMapForward.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

		return new ActionPrintContext(new ArrayList<>(changeSet.changes()), idMapForward, idMapBackward, from, to, printIdChanges, printAllEdgeDeletes);
	}

	public Stream<SingleIdRelatedAction> findChangesForId(final String id) {
		return changes.stream()
				.filter(c -> c instanceof SingleIdRelatedAction)
				.map(SingleIdRelatedAction.class::cast)
				.filter(c -> c.getId().equals(id));
	}
}
