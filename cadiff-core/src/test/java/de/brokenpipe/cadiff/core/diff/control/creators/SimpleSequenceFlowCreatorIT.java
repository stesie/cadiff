package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddSimpleSequenceFlowAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

public class SimpleSequenceFlowCreatorIT {

	@Nested
	public class CreateSingleSequenceFlowElement extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_1b6giix";
		public static final String ELEMENT_ID = "Flow_asdf";
		public static final String SOURCE_ID = "Gateway_0iqitjy";
		public static final String TARGET_ID = "Activity_0m1de7n";

		public CreateSingleSequenceFlowElement(@BpmnFile("create-sequence-flow-old.bpmn") final BpmnModelInstance from,
				@BpmnFile("create-sequence-flow-new.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			changes.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions()
					.assertSize(1)
					.assertExactlyOneInstanceOf(AddSimpleSequenceFlowAction.class)
					.assertEquals(ELEMENT_ID, AddSimpleSequenceFlowAction::id)
					.assertEquals(SOURCE_ID, AddSimpleSequenceFlowAction::sourceId)
					.assertEquals(TARGET_ID, AddSimpleSequenceFlowAction::targetId);
		}

	}

}
