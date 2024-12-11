package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.assertions.ChangeProcessActionAssertions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public class CreateStartEventIT extends AbstractComparePatchIT {

	public CreateStartEventIT(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
			@BpmnFile("start-event.bpmn") final BpmnModelInstance to) {
		super(from, to);
	}

	@Override
	protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

		final ChangeProcessActionAssertions changeProcessAction = changes
				.assertSize(1)
				.assertExactlyOneChangeProcessAction();

		final ActionCollectionAssertions changeProcessActions = changeProcessAction
				.assertId("Process_132av6t")
				.actions();

		changeProcessActions.assertSize(2);

		changeProcessActions.nextAction()
				.assertInstanceOf(AddSimpleFlowNodeAction.class)
				.assertEquals("StartEvent_1", AddSimpleFlowNodeAction::id)
				.assertEquals("startEvent", AddSimpleFlowNodeAction::elementTypeName);

		changeProcessActions.nextAction()
				.assertInstanceOf(ChangeNameAction.class)
				.assertEquals("StartEvent_1", ChangeNameAction::id)
				.assertEquals("just a start event", ChangeNameAction::newValue);

	}
}
