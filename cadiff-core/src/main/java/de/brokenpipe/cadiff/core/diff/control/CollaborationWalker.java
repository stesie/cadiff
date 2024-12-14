package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddCollaborationAction;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.instance.Collaboration;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CollaborationWalker {

	private final CompareContext<Collection<Collaboration>> compareContext;
	private Collaboration fromContainer;

	public CollaborationWalker(final CompareContext<Collection<Collaboration>> compareContext) {
		this.compareContext = compareContext;

		if (compareContext.from().size() > 1 || compareContext.to().size() > 1) {
			throw new IllegalArgumentException("Only one collaboration is allowed");
		}

		fromContainer = compareContext.from().stream().findFirst().orElse(null);
	}

	public Stream<Action> walk() {
		return StreamUtils.mergeStreams(List.of(
				handleCreateCollaboration(),
				handleDeleteCollaboration(),
				handleUpdateCollaboration()
		));
	}

	private Stream<Action> handleCreateCollaboration() {
		if (compareContext.from().isEmpty() && !compareContext.to().isEmpty()) {
			final AddCollaborationAction action = new AddCollaborationAction(compareContext.to().iterator().next().getId());
			action.patcher().accept(PatcherContext.of(compareContext.fromInstance(), compareContext.fromContainer()));
			fromContainer = compareContext.fromInstance().getModelElementById(action.id());
			return Stream.of(action);
		}

		return Stream.empty();
	}

	private Stream<Action> handleDeleteCollaboration() {
		if (!compareContext.from().isEmpty() && compareContext.to().isEmpty()) {
			return Stream.of(new DeleteElementAction(compareContext.from().iterator().next().getId()));
		}

		return Stream.empty();
	}

	private Stream<Action> handleUpdateCollaboration() {
		if (compareContext.to().isEmpty()) {
			return Stream.empty();
		}

		return new ParticipantWalker(compareContext.map(x -> x.stream().findFirst()
						.map(Collaboration::getParticipants)
						.orElse(Collections.emptyList()))
				.withFromContainer(fromContainer))
				.walk();
	}
}
