package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

public class FlowElementWalker_DeleteElementIT {

	@Nested
	public class CreateSingleSequenceFlowElement extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_1b6giix";
		public static final String ELEMENT_ID = "Activity_0m1de7n";

		public CreateSingleSequenceFlowElement(@BpmnFile("creators/create-sequence-flow-new.bpmn") final BpmnModelInstance from,
				@BpmnFile("delete-node-and-edge.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(1, "should create single delete for the flow node, the edge is deleted implicitly")
					.assertExactlyOneInstanceOf(DeleteElementAction.class)
					.assertEquals(ELEMENT_ID, DeleteElementAction::id);
		}

	}}
