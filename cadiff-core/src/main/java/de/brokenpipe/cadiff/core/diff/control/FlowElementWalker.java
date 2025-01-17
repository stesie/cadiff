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

		// if some of the implicitEdgeDeletions are on the updated list, we need to move them to the add list, so that
		// they are re-created
		implicitEdgeDeletions.stream()
				.filter(voteContext.updated()::contains)
				.forEach(id -> {
					voteContext.updated().remove(id);
					voteContext.added().add(id);
				});

		edgesToDelete.forEach((id, element) -> actions.add(new DeleteElementAction(id)));

		return actions.stream();
	}

	@Override
	protected VoteContext<String, FlowElement> createVoteContext() {
		final VoteContext<String, FlowElement> voteContext = super.createVoteContext();

		// check if updated elements have changed their type, if so, delete and re-create them
		voteContext.updated().stream()
				.filter(id -> {
					final FlowElement from = voteContext.fromMap().get(id);
					final FlowElement to = voteContext.toMap().get(id);
					return !from.getElementType().equals(to.getElementType());
				})
				.forEach(id -> {
					voteContext.removed().add(id);
					voteContext.added().add(id);
				});

		return voteContext;
	}
}
