package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.AddBranchToGatewayAction;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlowElementWalker_DeleteElementIT {

	/**
	 * Delete single flow node, add test that the incoming edge is deleted implicitly.
	 */
	@Nested
	public class ImplicitEdgeDelete extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_1b6giix";
		public static final String ELEMENT_ID = "Activity_0m1de7n";

		public ImplicitEdgeDelete(@BpmnFile("creators/create-sequence-flow-new.bpmn") final BpmnModelInstance from,
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
	}

	/**
	 * Delete single flow node, but re-use the incoming edge -> must be re-created!
	 */
	@Nested
	public class RecreateImplicitlyDeletedEdge extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_1b6giix";
		public static final String ELEMENT_ID = "Activity_0m1de7n";
		public static final List<String> CREATE_FLOW_IDS = List.of("Gateway_0iqitjy", "Flow_asdf", "Activity_0n2dgs8");

		public RecreateImplicitlyDeletedEdge(
				@BpmnFile("creators/create-sequence-flow-new.bpmn") final BpmnModelInstance from,
				@BpmnFile("delete-node-reuse-edge.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			ActionCollectionAssertions processActions = changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			processActions.assertSize(2);

			processActions.assertExactlyOneInstanceOf(DeleteElementAction.class)
					.assertEquals(ELEMENT_ID, DeleteElementAction::id);

			final AddBranchToGatewayAction action = processActions
					.assertExactlyOneInstanceOf(AddBranchToGatewayAction.class)
					.getAction();

			assertTrue(action.finalElementIsNew());
			assertEquals(CREATE_FLOW_IDS, action.steps().stream().map(AddBranchToGatewayAction.Step::id).toList());
		}
	}

}
