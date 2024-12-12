package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.*;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

public class ErrorEndEventIT {

	public static final String ERROR_ID = "Error_170pjce";
	public static final String PROCESS_ID = "Process_132av6t";
	public static final String ELEMENT_ID = "EndEvent_1";

	@Nested
	public class WithoutRef extends AbstractComparePatchIT {

		public WithoutRef(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
				@BpmnFile("error-end-event.bpmn") final BpmnModelInstance to) {
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
					.assertEquals("endEvent", AddSimpleFlowNodeAction::elementTypeName);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeNameAction.class)
					.assertEquals(ELEMENT_ID, ChangeNameAction::id)
					.assertEquals("validation failed", ChangeNameAction::newValue);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeErrorEventDefinitionAction.class)
					.assertEquals(ELEMENT_ID, ChangeErrorEventDefinitionAction::id)
					.assertEquals("ErrorEventDefinition_1ki687q", ChangeErrorEventDefinitionAction::errorDefinitionId);

		}
	}

	@Nested
	public class WithRef extends AbstractComparePatchIT {

		public WithRef(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
				@BpmnFile("error-end-event-with-ref.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			changes.assertSize(3);

			changes.assertExactlyOneInstanceOf(AddErrorAction.class)
					.assertEquals(ERROR_ID, AddErrorAction::id);

			changes.assertExactlyOneInstanceOf(ChangeErrorNameAction.class)
					.assertEquals(ERROR_ID, ChangeErrorNameAction::id)
					.assertEquals("Validation failed", ChangeErrorNameAction::newValue);

			final ActionCollectionAssertions changeProcessActions = changes
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(3);

			changeProcessActions.nextAction()
					.assertInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(ELEMENT_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("endEvent", AddSimpleFlowNodeAction::elementTypeName);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeNameAction.class)
					.assertEquals(ELEMENT_ID, ChangeNameAction::id)
					.assertEquals("validation failed", ChangeNameAction::newValue);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeErrorEventDefinitionAction.class)
					.assertEquals(ELEMENT_ID, ChangeErrorEventDefinitionAction::id)
					.assertEquals("ErrorEventDefinition_1ki687q", ChangeErrorEventDefinitionAction::errorDefinitionId);

		}
	}
}