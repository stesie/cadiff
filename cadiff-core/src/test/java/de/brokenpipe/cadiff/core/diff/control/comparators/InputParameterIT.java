package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.actions.ChangeInputParameterAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class InputParameterIT extends AbstractComparePatchIT {

	public static final String PROCESS_ID = "Process_14ftleg";
	public static final String ELEMENT_ID = "CallActivity_1";

	public InputParameterIT(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
			@BpmnFile("input-values.bpmn") final BpmnModelInstance to) {
		super(from, to);
	}

	@Override
	protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

		final ActionCollectionAssertions changeProcessActions = changes
				.assertSize(1)
				.assertExactlyOneChangeProcessAction()
				.assertId(PROCESS_ID)
				.actions();

		changeProcessActions.assertSize(3);

		final Map<String, ChangeInputParameterAction> actions = changeProcessActions.getActions().stream()
				.peek(x -> assertInstanceOf(ChangeInputParameterAction.class, x))
				.map(ChangeInputParameterAction.class::cast)
				.collect(Collectors.toMap(ChangeInputParameterAction::name, x -> x));

		assertTrue(actions.values().stream().allMatch(x -> ELEMENT_ID.equals(x.id())));
		assertTrue(actions.values().stream().allMatch(x -> x.oldValue() == null));
		assertEquals(Set.of("input_string", "input_list", "input_map"), actions.keySet());
		
		final var inputString = actions.get("input_string");
		assertInstanceOf(Value.StringValue.class, inputString.newValue());
		assertEquals("the string", ((Value.StringValue) inputString.newValue()).value());

		final var inputList = actions.get("input_list");
		assertInstanceOf(Value.ListValue.class, inputList.newValue());
		assertEquals(List.of("foo", "bar", "baz"), ((Value.ListValue) inputList.newValue()).values());

	}

	
}
