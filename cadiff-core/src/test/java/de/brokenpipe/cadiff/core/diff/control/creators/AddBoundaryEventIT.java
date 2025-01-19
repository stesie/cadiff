package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddFlowAction;
import de.brokenpipe.cadiff.core.actions.ChangeAttachedToAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddBoundaryEventIT {

	@Nested
	public class BoundaryEventWithFlow extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ATTACHED_TO_ID = "CallActivity_1";
		public static final List<String> FLOW_IDS = List.of("Event_121e2fi", "Flow_196m9u8", "Activity_139kl5a",
				"Flow_19h6nje", "Event_0atkqf8");

		public BoundaryEventWithFlow(@BpmnFile("../comparators/mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("add-boundary-event.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final var processActions = changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			processActions.assertSize(2);

			final AddFlowAction afa = processActions.assertExactlyOneInstanceOf(AddFlowAction.class).getAction();
			assertEquals(Optional.of(ATTACHED_TO_ID), afa.attachedToId());
			assertTrue(afa.finalElementIsNew());
			assertEquals(FLOW_IDS, afa.steps().stream().map(AddAction.Step::id).toList());

			// AddFlowAction should already attach to previous element
			processActions.assertNoInstanceOf(ChangeAttachedToAction.class);
		}

		@Test
		void shouldCreateCorrectReverseChanges() {
			final ChangeSet changeSet = new DiffCommand(to, from).execute();
			final var changes = new ActionCollectionAssertions(changeSet.changes());

			changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(3, "expect 3 DeleteElementAction, one for each flow node (edges deleted implicitly)");
		}
	}

}
