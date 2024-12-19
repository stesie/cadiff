package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.*;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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


	@Nested
	class EnableMultiInstanceSequentialIT extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ELEMENT_ID = "CallActivity_1";

		public EnableMultiInstanceSequentialIT(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("multi-sequential.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions.assertSize(2);

			changeProcessActions
					.nextAction()
					.assertInstanceOf(ChangeLoopCharacteristicsAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsAction::id)
					.assertEquals(Boolean.FALSE, ChangeLoopCharacteristicsAction::oldValue)
					.assertEquals(Boolean.TRUE, ChangeLoopCharacteristicsAction::newValue);

			changeProcessActions
					.nextAction()
					.assertInstanceOf(ChangeLoopCharacteristicsIsSequentialAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsIsSequentialAction::id)
					.assertEquals(Boolean.FALSE, ChangeLoopCharacteristicsIsSequentialAction::oldValue)
					.assertEquals(Boolean.TRUE, ChangeLoopCharacteristicsIsSequentialAction::newValue);

		}
	}


	@Nested
	class ChangeParallelToSequential extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ELEMENT_ID = "CallActivity_1";

		public ChangeParallelToSequential(@BpmnFile("multi-parallel.bpmn") final BpmnModelInstance from,
				@BpmnFile("multi-sequential.bpmn") final BpmnModelInstance to) {
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
					.assertInstanceOf(ChangeLoopCharacteristicsIsSequentialAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsIsSequentialAction::id)
					.assertEquals(Boolean.FALSE, ChangeLoopCharacteristicsIsSequentialAction::oldValue)
					.assertEquals(Boolean.TRUE, ChangeLoopCharacteristicsIsSequentialAction::newValue);

		}


		@Test
		void shouldCreateCorrectReverseChanges() {
			final ChangeSet changeSet = new DiffCommand(to, from).execute();
			final var changes = new ActionCollectionAssertions(changeSet.changes());
			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeLoopCharacteristicsIsSequentialAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsIsSequentialAction::id)
					.assertEquals(Boolean.TRUE, ChangeLoopCharacteristicsIsSequentialAction::oldValue)
					.assertEquals(Boolean.FALSE, ChangeLoopCharacteristicsIsSequentialAction::newValue);

		}
	}


	@Nested
	class ChangeCardinality extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ELEMENT_ID = "CallActivity_1";

		public ChangeCardinality(@BpmnFile("multi-sequential.bpmn") final BpmnModelInstance from,
				@BpmnFile("multi-cardinality.bpmn") final BpmnModelInstance to) {
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
					.assertInstanceOf(ChangeLoopCharacteristicsCardinalityAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsCardinalityAction::id)
					.assertEquals(null, ChangeLoopCharacteristicsCardinalityAction::oldValue)
					.assertEquals("3", ChangeLoopCharacteristicsCardinalityAction::newValue);

		}
	}


	@Nested
	class ChangeCollectionSettings extends AbstractComparePatchIT {

		public static final String PROCESS_ID = "Process_14ftleg";
		public static final String ELEMENT_ID = "CallActivity_1";

		public ChangeCollectionSettings(@BpmnFile("multi-sequential.bpmn") final BpmnModelInstance from,
				@BpmnFile("multi-collection.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {
			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions.assertSize(2);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeLoopCharacteristicsCollectionAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsCollectionAction::id)
					.assertEquals(null, ChangeLoopCharacteristicsCollectionAction::oldValue)
					.assertEquals("${foos}", ChangeLoopCharacteristicsCollectionAction::newValue);

			changeProcessActions.nextAction()
					.assertInstanceOf(ChangeLoopCharacteristicsElementVariableAction.class)
					.assertEquals(ELEMENT_ID, ChangeLoopCharacteristicsElementVariableAction::id)
					.assertEquals(null, ChangeLoopCharacteristicsElementVariableAction::oldValue)
					.assertEquals("foo", ChangeLoopCharacteristicsElementVariableAction::newValue);

		}
	}

}
