package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.diff.control.comparators.ComparatorRegistry;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.*;
import java.util.stream.Stream;

public class FlowElementWalker extends AbstractVoteAddWalker<FlowElement> {

	public FlowElementWalker(final CompareContext<Collection<FlowElement>> compareContext) {
		super(compareContext);
	}

	@Override
	protected Stream<Action> handleUpdated(final CompareContext<FlowElement> context) {
		return ComparatorRegistry.INSTANCE.apply(context);
	}

	@Override
	protected Stream<Action> handleRemoved(final VoteContext<String, FlowElement> voteContext) {

		final List<Action> actions = new ArrayList<>();

		final Map<String, FlowElement> edgesToDelete = new HashMap<>();
		final Set<String> implicitEdgeDeletions = new HashSet<>();

		voteContext.removed().forEach(id -> {
			final FlowElement element = voteContext.fromMap().get(id);

			if (element instanceof final FlowNode flowNode) {
				actions.add(new DeleteElementAction(id));

				flowNode.getIncoming().stream().map(FlowElement::getId).forEach(implicitEdgeDeletions::add);
				flowNode.getOutgoing().stream().map(FlowElement::getId).forEach(implicitEdgeDeletions::add);
			} else {
				edgesToDelete.put(id, element);
			}
		});

		implicitEdgeDeletions.forEach(edgesToDelete::remove);

		edgesToDelete.forEach((id, element) -> actions.add(new DeleteElementAction(id)));

		return actions.stream();
	}



}
