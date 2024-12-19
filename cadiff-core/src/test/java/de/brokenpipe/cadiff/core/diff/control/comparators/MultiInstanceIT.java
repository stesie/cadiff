package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.ChangeLoopCharacteristicsAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;

public class MultiInstanceIT {

	@Nested
	class EnableMultiInstanceIT extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ELEMENT_ID = "CallActivity_1";

		public EnableMultiInstanceIT(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("multi-parallel.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeLoopCharacteristicsAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsAction::id)
					.assertEquals(Boolean.FALSE, ChangeLoopCharacteristicsAction::oldValue)
					.assertEquals(Boolean.TRUE, ChangeLoopCharacteristicsAction::newValue);

		}
	}
}
