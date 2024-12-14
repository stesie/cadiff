package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.*;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CollaborationIT {

	@Nested
	public class AddCollaborationAroundProcess extends AbstractComparePatchIT {

		public static final String COLLABORATION_ID = "Collaboration_19q2vs5";
		public static final String PARTICIPANT_ID = "Participant_04bfsrn";
		public static final String PROCESS_ID = "Process_132av6t";

		public AddCollaborationAroundProcess(@BpmnFile("sub-process.bpmn") final BpmnModelInstance from,
				@BpmnFile("participant.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			changes.assertSize(4);

			changes.assertExactlyOneInstanceOf(AddCollaborationAction.class)
					.assertEquals(COLLABORATION_ID, AddCollaborationAction::id);

			changes.assertExactlyOneInstanceOf(AddParticipantAction.class)
					.assertEquals(PARTICIPANT_ID, AddParticipantAction::id);

			changes.assertExactlyOneInstanceOf(ChangeParticipantNameAction.class)
					.assertEquals(PARTICIPANT_ID, ChangeParticipantNameAction::id)
					.assertEquals(null, ChangeParticipantNameAction::oldValue)
					.assertEquals("the participant name", ChangeParticipantNameAction::newValue);

			changes.assertExactlyOneInstanceOf(ChangeParticipantProcessAction.class)
					.assertEquals(PARTICIPANT_ID, ChangeParticipantProcessAction::id)
					.assertEquals(PROCESS_ID, ChangeParticipantProcessAction::newValue);
		}

		@Test
		void shouldCreateCorrectReverseChanges() {
			final ChangeSet changeSet = new DiffCommand(to, from).execute();
			final var changes = new ActionCollectionAssertions(changeSet.changes());

			changes.assertSize(1)
					.assertExactlyOneInstanceOf(DeleteElementAction.class)
					.assertEquals(COLLABORATION_ID, DeleteElementAction::id);

		}

	}

}
