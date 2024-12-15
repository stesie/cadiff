package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public class StartEventIT extends AbstractComparePatchIT {

	public static final String PROCESS_ID = "Process_132av6t";
	public static final String ELEMENT_ID = "StartEvent_1";

	public StartEventIT(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
			@BpmnFile("start-event.bpmn") final BpmnModelInstance to) {
		super(from, to);
	}

	@Override
	protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

		final ActionCollectionAssertions changeProcessActions = changes
				.assertSize(1)
				.assertExactlyOneChangeProcessAction()
				.assertId(PROCESS_ID)
				.actions()
				.assertSize(2);

		changeProcessActions.nextAction()
				.assertInstanceOf(AddSimpleFlowNodeAction.class)
				.assertEquals(ELEMENT_ID, AddSimpleFlowNodeAction::id)
				.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

		changeProcessActions.nextAction()
				.assertInstanceOf(ChangeNameAction.class)
				.assertEquals(ELEMENT_ID, ChangeNameAction::id)
				.assertEquals("just a start event", ChangeNameAction::newValue);

	}
}
