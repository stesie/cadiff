package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.actions.ChangeErrorEventDefinitionAction;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

public class CreateErrorEndEventIT {

	@Nested
	public class WithoutRef extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_132av6t";
		public static final String ELEMENT_ID = "EndEvent_1";

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
}