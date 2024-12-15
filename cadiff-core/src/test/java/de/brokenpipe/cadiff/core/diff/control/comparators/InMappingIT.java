package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.ChangeInMappingAllAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class InMappingIT {

	public static final String PROCESS_ID = "Process_14ftleg";
	public static final String ELEMENT_ID = "CallActivity_1";

	@Nested
	public class EnablePropagateAll extends AbstractComparePatchIT {

		public EnablePropagateAll(@BpmnFile("in-mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("in-mapping-propagate-all.bpmn") final BpmnModelInstance to) {
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
					.assertExactlyOneInstanceOf(ChangeInMappingAllAction.class)
					.assertEquals(ELEMENT_ID, ChangeInMappingAllAction::id)
					.assertEquals(ChangeInMappingAllAction.Config.disabled(), ChangeInMappingAllAction::oldValue)
					.assertEquals(new ChangeInMappingAllAction.Config(true, Boolean.FALSE), ChangeInMappingAllAction::newValue);
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
					.assertExactlyOneInstanceOf(ChangeInMappingAllAction.class)
					.assertEquals(ELEMENT_ID, ChangeInMappingAllAction::id)
					.assertEquals(new ChangeInMappingAllAction.Config(true, Boolean.FALSE), ChangeInMappingAllAction::oldValue)
					.assertEquals(ChangeInMappingAllAction.Config.disabled(), ChangeInMappingAllAction::newValue);
		}
	}

	@Nested
	public class EnablePropagateAllLocal extends AbstractComparePatchIT {

		public EnablePropagateAllLocal(@BpmnFile("in-mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("in-mapping-propagate-all-local.bpmn") final BpmnModelInstance to) {
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
					.assertExactlyOneInstanceOf(ChangeInMappingAllAction.class)
					.assertEquals(ELEMENT_ID, ChangeInMappingAllAction::id)
					.assertEquals(ChangeInMappingAllAction.Config.disabled(), ChangeInMappingAllAction::oldValue)
					.assertEquals(new ChangeInMappingAllAction.Config(true, Boolean.TRUE), ChangeInMappingAllAction::newValue);

		}
	}

}
