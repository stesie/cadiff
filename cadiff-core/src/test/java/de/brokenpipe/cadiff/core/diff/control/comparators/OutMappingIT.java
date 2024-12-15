package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAllAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OutMappingIT {


	public static final String PROCESS_ID = "Process_14ftleg";
	public static final String ELEMENT_ID = "CallActivity_1";

	@Nested
	public class EnablePropagateAll extends AbstractComparePatchIT {

		public EnablePropagateAll(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("out-mapping-propagate-all.bpmn") final BpmnModelInstance to) {
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
					.assertExactlyOneInstanceOf(ChangeOutMappingAllAction.class)
					.assertEquals(ELEMENT_ID, ChangeOutMappingAllAction::id)
					.assertEquals(ChangeOutMappingAllAction.Config.disabled(), ChangeOutMappingAllAction::oldValue)
					.assertEquals(new ChangeOutMappingAllAction.Config(true, Boolean.FALSE),
							ChangeOutMappingAllAction::newValue);
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
					.assertExactlyOneInstanceOf(ChangeOutMappingAllAction.class)
					.assertEquals(ELEMENT_ID, ChangeOutMappingAllAction::id)
					.assertEquals(new ChangeOutMappingAllAction.Config(true, Boolean.FALSE),
							ChangeOutMappingAllAction::oldValue)
					.assertEquals(ChangeOutMappingAllAction.Config.disabled(), ChangeOutMappingAllAction::newValue);
		}
	}

	@Nested
	public class EnablePropagateAllLocal extends AbstractComparePatchIT {

		public EnablePropagateAllLocal(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("out-mapping-propagate-all-local.bpmn") final BpmnModelInstance to) {
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
					.assertExactlyOneInstanceOf(ChangeOutMappingAllAction.class)
					.assertEquals(ELEMENT_ID, ChangeOutMappingAllAction::id)
					.assertEquals(ChangeOutMappingAllAction.Config.disabled(), ChangeOutMappingAllAction::oldValue)
					.assertEquals(new ChangeOutMappingAllAction.Config(true, Boolean.TRUE),
							ChangeOutMappingAllAction::newValue);

		}
	}

}
