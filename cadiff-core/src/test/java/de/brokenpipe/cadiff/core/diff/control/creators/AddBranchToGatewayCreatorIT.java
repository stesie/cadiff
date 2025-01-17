package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddBranchToGatewayAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddBranchToGatewayCreatorIT {

	@Nested
	public class Minimal extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_1b6giix";
		public static final String GATEWAY_ID = "Gateway_0iqitjy";
		public static final String EDGE_1 = "Flow_1014479";
		public static final String NODE_1 = "Event_06zpvr1";

		public Minimal(@BpmnFile("create-sequence-flow-old.bpmn") final BpmnModelInstance from,
				@BpmnFile("add-branch-to-gw-minimal.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final AddBranchToGatewayAction action = changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(1)
					.assertExactlyOneInstanceOf(AddBranchToGatewayAction.class)
					.getAction();

			assertTrue(action.finalElementIsNew());

			assertEquals(3, action.steps().size());

			assertEquals(GATEWAY_ID, action.steps().getFirst().id());
			assertEquals(EDGE_1, action.steps().get(1).id());
			assertEquals(NODE_1, action.steps().get(2).id());
			assertEquals("endEvent", action.steps().get(2).elementTypeName());
		}

	}

	@Nested
	public class WithIntermediateThrow extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_1b6giix";
		public static final List<String> ELEMENT_IDS = List.of(
				"Gateway_0iqitjy",
				"Flow_1tchsde",
				"Activity_14rmzbk",
				"Flow_00v33js",
				"Event_1avikzb",
				"Flow_0oee8ze",
				"Activity_0konqtb",
				"Flow_1i0q9c8",
				"Event_16oj5m3"
		);

		public WithIntermediateThrow(@BpmnFile("create-sequence-flow-old.bpmn") final BpmnModelInstance from,
				@BpmnFile("add-branch-to-gw-intermediate-throw.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final AddBranchToGatewayAction action = changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertExactlyOneInstanceOf(AddBranchToGatewayAction.class)
					.getAction();

			assertTrue(action.finalElementIsNew());

			assertEquals(9, action.steps().size());
			assertEquals(ELEMENT_IDS, action.steps().stream().map(AddBranchToGatewayAction.Step::id).toList());
		}

	}

}
