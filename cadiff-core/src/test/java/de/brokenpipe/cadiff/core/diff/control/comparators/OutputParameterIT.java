package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.actions.ChangeOutputParameterAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class OutputParameterIT extends AbstractComparePatchIT {

	public static final String PROCESS_ID = "Process_14ftleg";
	public static final String ELEMENT_ID = "CallActivity_1";

	public OutputParameterIT(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
			@BpmnFile("output-values.bpmn") final BpmnModelInstance to) {
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

		final Map<String, ChangeOutputParameterAction> actions = changeProcessActions.getActions().stream()
				.peek(x -> assertInstanceOf(ChangeOutputParameterAction.class, x))
				.map(ChangeOutputParameterAction.class::cast)
				.collect(Collectors.toMap(ChangeOutputParameterAction::name, x -> x));

		assertTrue(actions.values().stream().allMatch(x -> ELEMENT_ID.equals(x.id())));
		assertTrue(actions.values().stream().allMatch(x -> x.oldValue() == null));
		assertEquals(Set.of("output_string", "output_list", "output_map"), actions.keySet());
		
		final var outputString = actions.get("output_string");
		assertInstanceOf(Value.StringValue.class, outputString.newValue());
		assertEquals("the string", ((Value.StringValue) outputString.newValue()).value());

		final var outputList = actions.get("output_list");
		assertInstanceOf(Value.ListValue.class, outputList.newValue());
		assertEquals(List.of("foo", "bar", "baz"), ((Value.ListValue) outputList.newValue()).values());

		final var outputMap = actions.get("output_map");
		assertInstanceOf(Value.MapValue.class, outputMap.newValue());
		assertEquals(Map.of("foo", "foo_value", "bar", "bar_value"), ((Value.MapValue) outputMap.newValue()).entries());

	}

	
}
