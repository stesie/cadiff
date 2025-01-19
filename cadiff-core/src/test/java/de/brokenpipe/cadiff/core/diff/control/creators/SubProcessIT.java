package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.*;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubProcessIT {

	@Nested
	public class CreateSubProcessWithStartEvent extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_132av6t";
		public static final String ELEMENT_ID = "StartEvent_1";
		public static final String SUB_PROCESS_ID = "SubProcess";
		public static final String START_EVENT_IN_SUB_PROCESS_ID = "StartEvent_SubProcess";

		public CreateSubProcessWithStartEvent(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
				@BpmnFile("sub-process.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			// top level
			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(4);

			// process level
			changeProcessActions.nextAction()
					.assertInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(SUB_PROCESS_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("subProcess", AddSimpleFlowNodeAction::elementTypeName);

			changeProcessActions.nextAction()
					.assertInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(ELEMENT_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

			changeProcessActions
					.assertExactlyOneInstanceOf(ChangeNameAction.class)
					.assertEquals(ELEMENT_ID, ChangeNameAction::id)
					.assertEquals("just a start event", ChangeNameAction::newValue);

			// sub process level
			final var changeSubProcessActions = changeProcessActions
					.assertExactlyOneChangeSubProcessAction()
					.assertId(SUB_PROCESS_ID)
					.actions();

			changeSubProcessActions.assertExactlyOneInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(START_EVENT_IN_SUB_PROCESS_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

			changeSubProcessActions.assertExactlyOneInstanceOf(ChangeNameAction.class)
					.assertEquals(START_EVENT_IN_SUB_PROCESS_ID, ChangeNameAction::id)
					.assertEquals("start event in sub process", ChangeNameAction::newValue);
		}

		@Test
		void shouldCreateCorrectReverseChanges() {
			final ChangeSet changeSet = new DiffCommand(to, from).execute();
			final var changes = new ActionCollectionAssertions(changeSet.changes());

			// top level
			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(2);

			// expect subprocess and start event to be removed (-> the sub process elements are removed implicitly)
			changeProcessActions.nextAction()
					.assertInstanceOf(DeleteElementAction.class)
					.assertEquals(SUB_PROCESS_ID, DeleteElementAction::id);

			changeProcessActions.nextAction()
					.assertInstanceOf(DeleteElementAction.class)
					.assertEquals(ELEMENT_ID, DeleteElementAction::id);
		}
	}

	@Nested
	public class SubProcessWithFlow extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ATTACHED_TO_ID = "CallActivity_1";
		public static final String FLOW_ID = "Flow_0gpglkd";
		public static final String SUB_PROCESS_ID = "Activity_12cyd6l";
		public static final List<String> FLOW_IDS = List.of("Event_1cvgi8f", "Flow_0s1mmtx", "Activity_11d3rp8",
				"Flow_00w3sub", "Event_1x7ujtw");

		public SubProcessWithFlow(@BpmnFile("../comparators/mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("sub-process-with-flow.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final var processActions = changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			processActions.assertSize(3);

			processActions.assertExactlyOneInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(SUB_PROCESS_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("subProcess", AddSimpleFlowNodeAction::elementTypeName);

			processActions.assertExactlyOneInstanceOf(AddSimpleSequenceFlowAction.class)
					.assertEquals(FLOW_ID, AddSimpleSequenceFlowAction::id)
					.assertEquals(ATTACHED_TO_ID, AddSimpleSequenceFlowAction::sourceId)
					.assertEquals(SUB_PROCESS_ID, AddSimpleSequenceFlowAction::targetId);

			final AddFlowAction afa = processActions.assertExactlyOneChangeSubProcessAction()
					.assertId(SUB_PROCESS_ID)
					.actions()
					.assertSize(1, "should only have single sub process change action, that creates the whole flow")
					.assertExactlyOneInstanceOf(AddFlowAction.class)
					.getAction();

			assertTrue(afa.attachedToId().isEmpty(), "AddFlowAction should not attach to anywhere");
			assertTrue(afa.finalElementIsNew());
			assertEquals(FLOW_IDS, afa.steps().stream().map(AddAction.Step::id).toList());
		}

		@Test
		void shouldCreateCorrectReverseChanges() {
			final ChangeSet changeSet = new DiffCommand(to, from).execute();
			final var changes = new ActionCollectionAssertions(changeSet.changes());

			changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(1, "should have exactly one delete action, that removes the whole subpocess (including children) and the sequence flow that targets it")
					.assertExactlyOneInstanceOf(DeleteElementAction.class)
					.assertEquals(SUB_PROCESS_ID, DeleteElementAction::id);
		}
	}

}
