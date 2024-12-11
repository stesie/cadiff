package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateStartEventIT extends AbstractComparePatchIT {

	public CreateStartEventIT(@BpmnFile("empty-diagram.bpmn") final BpmnModelInstance from,
			@BpmnFile("start-event.bpmn") final BpmnModelInstance to) {
		super(from, to);
	}

	@Override
	protected void verifyForwardChanges(final List<Action> changes) {
		assertEquals(1, changes.size());
		assertInstanceOf(ChangeProcessAction.class, changes.getFirst());

		final ChangeProcessAction changeProcessAction = (ChangeProcessAction) changes.getFirst();
		assertEquals("Process_132av6t", changeProcessAction.id());

		final Collection<Action> actions = changeProcessAction.actions();
		assertEquals(2, actions.size());

		final var actionIterator = actions.iterator();

		final var addSimpleFlowNodeAction = actionIterator.next();
		assertInstanceOf(AddSimpleFlowNodeAction.class, addSimpleFlowNodeAction);
		assertEquals("StartEvent_1", ((AddSimpleFlowNodeAction) addSimpleFlowNodeAction).id());
		assertEquals("startEvent", ((AddSimpleFlowNodeAction) addSimpleFlowNodeAction).elementTypeName());

		final var changeNameAction = actionIterator.next();
		assertInstanceOf(ChangeNameAction.class, changeNameAction);
		assertEquals("StartEvent_1", ((ChangeNameAction) changeNameAction).id());
		assertNull(((ChangeNameAction) changeNameAction).oldValue());
		assertEquals("just a start event", ((ChangeNameAction) changeNameAction).newValue());
	}
}
