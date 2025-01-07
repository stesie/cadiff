package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAction;
import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAllAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

	@Nested
	public class AddValueMappings extends AbstractComparePatchIT {

		public AddValueMappings(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("out-mapping-values.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions.assertSize(4);

			final Map<String, ChangeOutMappingAction> actions = changeProcessActions.getActions().stream()
					.peek(x -> assertInstanceOf(ChangeOutMappingAction.class, x))
					.map(ChangeOutMappingAction.class::cast)
					.collect(Collectors.toMap(ChangeOutMappingAction::targetName, x -> x));

			assertEquals(Set.of("target", "target-local", "target-expression", "target-expression-local"),
					actions.keySet());

			assertEquals("source", actions.get("target").newValue().source());
			assertEquals("source-local", actions.get("target-local").newValue().source());
			assertEquals("source-expression", actions.get("target-expression").newValue().source());
			assertEquals("source-expression-local", actions.get("target-expression-local").newValue().source());

			assertFalse(actions.get("target").newValue().isSourceExpression());
			assertFalse(actions.get("target-local").newValue().isSourceExpression());
			assertTrue(actions.get("target-expression").newValue().isSourceExpression());
			assertTrue(actions.get("target-expression-local").newValue().isSourceExpression());

			assertFalse(actions.get("target").newValue().local());
			assertTrue(actions.get("target-local").newValue().local());
			assertFalse(actions.get("target-expression").newValue().local());
			assertTrue(actions.get("target-expression-local").newValue().local());
		}
	}
}
