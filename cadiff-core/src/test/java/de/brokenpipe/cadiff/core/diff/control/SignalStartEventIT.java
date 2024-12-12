package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.*;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

public class SignalStartEventIT {

	public static final String SIGNAL_ID = "Signal_19qetdh";
	public static final String PROCESS_ID = "Process_132av6t";
	public static final String ELEMENT_ID = "StartEvent_1";

	@Nested
	public class WithoutRef extends AbstractComparePatchIT {

		public WithoutRef(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
				@BpmnFile("signal-start-event.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(3);

			changeProcessActions.nextAction()
					.assertInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(ELEMENT_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeNameAction.class)
					.assertEquals(ELEMENT_ID, ChangeNameAction::id)
					.assertEquals("signal start event", ChangeNameAction::newValue);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeSignalEventDefinitionAction.class)
					.assertEquals(ELEMENT_ID, ChangeSignalEventDefinitionAction::id)
					.assertEquals("SignalEventDefinition_0jnrmjs", ChangeSignalEventDefinitionAction::signalDefinitionId);

		}
	}

	@Nested
	public class WithRef extends AbstractComparePatchIT {

		public WithRef(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
				@BpmnFile("signal-start-event-with-ref.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			changes.assertSize(3);

			changes.assertExactlyOneInstanceOf(AddSignalAction.class)
					.assertEquals(SIGNAL_ID, AddSignalAction::id);

			changes.assertExactlyOneInstanceOf(ChangeSignalNameAction.class)
					.assertEquals(SIGNAL_ID, ChangeSignalNameAction::id)
					.assertEquals("TheSignal", ChangeSignalNameAction::newValue);

			final ActionCollectionAssertions changeProcessActions = changes
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(3);

			changeProcessActions.nextAction()
					.assertInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(ELEMENT_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeNameAction.class)
					.assertEquals(ELEMENT_ID, ChangeNameAction::id)
					.assertEquals("signal start event", ChangeNameAction::newValue);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeSignalEventDefinitionAction.class)
					.assertEquals(ELEMENT_ID, ChangeSignalEventDefinitionAction::id)
					.assertEquals("SignalEventDefinition_0jnrmjs", ChangeSignalEventDefinitionAction::signalDefinitionId);

		}
	}


	@Nested
	public class RemoveRefFromExistingEndEvent extends AbstractComparePatchIT {

		public RemoveRefFromExistingEndEvent(@BpmnFile("signal-start-event-with-ref.bpmn") final BpmnModelInstance from,
				@BpmnFile("signal-start-event.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			// expect exactly on change, removal of the global signal, the rest should just cascade
			changes.assertSize(1)
					.assertExactlyOneInstanceOf(DeleteElementAction.class)
					.assertEquals(SIGNAL_ID, DeleteElementAction::id);

		}
	}

	@Nested
	public class AddRefToExistingEndEvent extends AbstractComparePatchIT {

		public AddRefToExistingEndEvent(@BpmnFile("signal-start-event.bpmn") final BpmnModelInstance from,
				@BpmnFile("signal-start-event-with-ref.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			changes.assertSize(3);

			changes.assertExactlyOneInstanceOf(AddSignalAction.class)
					.assertEquals(SIGNAL_ID, AddSignalAction::id);

			changes.assertExactlyOneInstanceOf(ChangeSignalNameAction.class)
					.assertEquals(SIGNAL_ID, ChangeSignalNameAction::id)
					.assertEquals("TheSignal", ChangeSignalNameAction::newValue);

			final ActionCollectionAssertions changeProcessActions = changes
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(1);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeSignalEventDefinitionAction.class)
					.assertEquals(ELEMENT_ID, ChangeSignalEventDefinitionAction::id)
					.assertEquals("SignalEventDefinition_0jnrmjs", ChangeSignalEventDefinitionAction::signalDefinitionId);

		}
	}
}