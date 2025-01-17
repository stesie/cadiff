package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

public class FlowElementWalker_ChangeElementTypeIT {

	/**
	 * If flow node changes its type, it must be deleted and re-created.
	 */
	@Nested
	public class DeleteAndRecreate extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ELEMENT_ID = "CallActivity_1";

		public DeleteAndRecreate(@BpmnFile("comparators/mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("change-node-type.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final ActionCollectionAssertions processActions = changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			processActions.assertSize(2);

			processActions
					.assertExactlyOneInstanceOf(DeleteElementAction.class)
					.assertEquals(ELEMENT_ID, DeleteElementAction::id);

			processActions
					.assertExactlyOneInstanceOf(AddSimpleFlowNodeAction.class)
					.assertEquals(ELEMENT_ID, AddSimpleFlowNodeAction::id)
					.assertEquals("businessRuleTask", AddSimpleFlowNodeAction::elementTypeName);
		}
	}

}
