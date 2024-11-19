package de.brokenpipe.cadiff.cli.entity;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeIdAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import lombok.Data;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class ActionPrintContext {

	final List<Action> actions;
	final Map<String, String> idMapForward;
	final Map<String, String> idMapBackward;
	final BpmnModelInstance from;
	final BpmnModelInstance to;
	final boolean printIdChanges;
	final boolean printAllEdgeDeletes;

	public ActionPrintContext(final Collection<Action> actions,
			final BpmnModelInstance from, final BpmnModelInstance to,
			final boolean printIdChanges, final boolean printAllEdgeDeletes) {

		this.actions = new ArrayList<>(actions);

		idMapForward = actions.stream()
				.filter(c -> c instanceof ChangeIdAction)
				.map(ChangeIdAction.class::cast)
				.collect(Collectors.toMap(ChangeIdAction::oldId, ChangeIdAction::newId));
		idMapBackward = idMapForward.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

		this.from = from;
		this.to = to;
		this.printIdChanges = printIdChanges;
		this.printAllEdgeDeletes = printAllEdgeDeletes;
	}

	public static ActionPrintContext of(final ChangeSet changeSet, final BpmnModelInstance from,
			final BpmnModelInstance to, final boolean printIdChanges, final boolean printAllEdgeDeletes) {

		return new ActionPrintContext(changeSet.changes(), from, to, printIdChanges, printAllEdgeDeletes);
	}

	public Stream<SingleIdRelatedAction> findChangesForId(final String id) {
		return actions.stream()
				.filter(c -> c instanceof SingleIdRelatedAction)
				.map(SingleIdRelatedAction.class::cast)
				.filter(c -> c.id().equals(id));
	}

	public ActionPrintContext forSubProcess(final Collection<Action> actions) {
		return new ActionPrintContext(actions, from, to, printIdChanges, printAllEdgeDeletes);
	}
}
