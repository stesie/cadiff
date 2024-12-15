package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Test;

public class SubProcessIT extends AbstractComparePatchIT {

	public static final String PROCESS_ID = "Process_132av6t";
	public static final String ELEMENT_ID = "StartEvent_1";
	public static final String SUB_PROCESS_ID = "SubProcess";
	public static final String START_EVENT_IN_SUB_PROCESS_ID = "StartEvent_SubProcess";

	public SubProcessIT(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
			@BpmnFile("sub-process.bpmn") final BpmnModelInstance to) {
		super(from, to);
	}

	@Override
	protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

		// top level
		final ActionCollectionAssertions changeProcessActions = changes
				.assertSize(1)
				.assertExactlyOneChangeProcessAction()
				.assertId(PROCESS_ID)
				.actions()
				.assertSize(4);

		// process level
		changeProcessActions.nextAction()
				.assertInstanceOf(AddSimpleFlowNodeAction.class)
				.assertEquals(SUB_PROCESS_ID, AddSimpleFlowNodeAction::id)
				.assertEquals("subProcess", AddSimpleFlowNodeAction::elementTypeName);

		changeProcessActions.nextAction()
				.assertInstanceOf(AddSimpleFlowNodeAction.class)
				.assertEquals(ELEMENT_ID, AddSimpleFlowNodeAction::id)
				.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

		changeProcessActions
				.assertExactlyOneInstanceOf(ChangeNameAction.class)
				.assertEquals(ELEMENT_ID, ChangeNameAction::id)
				.assertEquals("just a start event", ChangeNameAction::newValue);

		// sub process level
		final var changeSubProcessActions = changeProcessActions
				.assertExactlyOneChangeSubProcessAction()
				.assertId(SUB_PROCESS_ID)
				.actions();

		changeSubProcessActions.assertExactlyOneInstanceOf(AddSimpleFlowNodeAction.class)
				.assertEquals(START_EVENT_IN_SUB_PROCESS_ID, AddSimpleFlowNodeAction::id)
				.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

		changeSubProcessActions.assertExactlyOneInstanceOf(ChangeNameAction.class)
				.assertEquals(START_EVENT_IN_SUB_PROCESS_ID, ChangeNameAction::id)
				.assertEquals("start event in sub process", ChangeNameAction::newValue);
	}

	@Test
	void shouldCreateCorrectReverseChanges() {
		final ChangeSet changeSet = new DiffCommand(to, from).execute();
		final var changes = new ActionCollectionAssertions(changeSet.changes());

		// top level
		final ActionCollectionAssertions changeProcessActions = changes
				.assertSize(1)
				.assertExactlyOneChangeProcessAction()
				.assertId(PROCESS_ID)
				.actions()
				.assertSize(2);

		// expect subprocess and start event to be removed (-> the sub process elements are removed implicitly)
		changeProcessActions.nextAction()
				.assertInstanceOf(DeleteElementAction.class)
				.assertEquals(SUB_PROCESS_ID, DeleteElementAction::id);

		changeProcessActions.nextAction()
				.assertInstanceOf(DeleteElementAction.class)
				.assertEquals(ELEMENT_ID, DeleteElementAction::id);
	}

}
