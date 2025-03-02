package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.AddExecutionListenerAction;
import de.brokenpipe.cadiff.core.actions.ChangeExecutionListenerFieldAction;
import de.brokenpipe.cadiff.core.actions.DeleteExecutionListenerAction;
import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.control.AbstractComparePatchIT;
import de.brokenpipe.cadiff.core.diff.control.BpmnFile;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ExecutionListenerIT {

	public static final String PROCESS_ID = "Process_14ftleg";
	public static final String ELEMENT_ID = "CallActivity_1";

	@Nested
	public class AddStartListener extends AbstractComparePatchIT {

		public AddStartListener(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("execution-listener-start.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(AddExecutionListenerAction.class)
					.assertEquals(ELEMENT_ID, AddExecutionListenerAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression());

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

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(DeleteExecutionListenerAction.class)
					.assertEquals(ELEMENT_ID, DeleteExecutionListenerAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression());
		}
	}

	@Nested
	public class AddEndListener extends AbstractComparePatchIT {

		public AddEndListener(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("execution-listener-end.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(AddExecutionListenerAction.class)
					.assertEquals(ELEMENT_ID, AddExecutionListenerAction::id)
					.assertEquals("end", x -> x.key().camundaEvent())
					.assertEquals("${endListener}", x -> x.key().camundaDelegateExpression());

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

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(DeleteExecutionListenerAction.class)
					.assertEquals(ELEMENT_ID, DeleteExecutionListenerAction::id)
					.assertEquals("end", x -> x.key().camundaEvent())
					.assertEquals("${endListener}", x -> x.key().camundaDelegateExpression());
		}
	}

	@Nested
	public class AddExpressionFieldToExistingListener extends AbstractComparePatchIT {

		public AddExpressionFieldToExistingListener(@BpmnFile("execution-listener-start.bpmn") final BpmnModelInstance from,
				@BpmnFile("execution-listener-start-add-field-expression.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeExecutionListenerFieldAction.class)
					.assertEquals(ELEMENT_ID, ChangeExecutionListenerFieldAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression())
					.assertEquals("foo", ChangeExecutionListenerFieldAction::fieldName)
					.assertEquals(null, ChangeExecutionListenerFieldAction::oldValue)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSourceExpression("${fooExpression}"),
							ChangeExecutionListenerFieldAction::newValue);

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

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeExecutionListenerFieldAction.class)
					.assertEquals(ELEMENT_ID, ChangeExecutionListenerFieldAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression())
					.assertEquals("foo", ChangeExecutionListenerFieldAction::fieldName)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSourceExpression("${fooExpression}"),
							ChangeExecutionListenerFieldAction::oldValue)
					.assertEquals(null, ChangeExecutionListenerFieldAction::newValue);
		}
	}

	@Nested
	public class AddStringFieldToExistingListener extends AbstractComparePatchIT {

		public AddStringFieldToExistingListener(@BpmnFile("execution-listener-start.bpmn") final BpmnModelInstance from,
				@BpmnFile("execution-listener-start-add-field-value.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeExecutionListenerFieldAction.class)
					.assertEquals(ELEMENT_ID, ChangeExecutionListenerFieldAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression())
					.assertEquals("foo", ChangeExecutionListenerFieldAction::fieldName)
					.assertEquals(null, ChangeExecutionListenerFieldAction::oldValue)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSource("foo-value"),
							ChangeExecutionListenerFieldAction::newValue);

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

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeExecutionListenerFieldAction.class)
					.assertEquals(ELEMENT_ID, ChangeExecutionListenerFieldAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression())
					.assertEquals("foo", ChangeExecutionListenerFieldAction::fieldName)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSource("foo-value"),
							ChangeExecutionListenerFieldAction::oldValue)
					.assertEquals(null, ChangeExecutionListenerFieldAction::newValue);
		}
	}

	@Nested
	public class ChangeFieldOfExistingListener extends AbstractComparePatchIT {

		public ChangeFieldOfExistingListener(@BpmnFile("execution-listener-start-add-field-expression.bpmn") final BpmnModelInstance from,
				@BpmnFile("execution-listener-start-add-field-value.bpmn") final BpmnModelInstance to) {
			super(from, to);
		}

		@Override
		protected void verifyForwardChanges(final ActionCollectionAssertions changes) {

			final ActionCollectionAssertions changeProcessActions = changes
					.assertSize(1)
					.assertExactlyOneChangeProcessAction()
					.assertId(PROCESS_ID)
					.actions();

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeExecutionListenerFieldAction.class)
					.assertEquals(ELEMENT_ID, ChangeExecutionListenerFieldAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression())
					.assertEquals("foo", ChangeExecutionListenerFieldAction::fieldName)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSourceExpression("${fooExpression}"),
							ChangeExecutionListenerFieldAction::oldValue)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSource("foo-value"),
							ChangeExecutionListenerFieldAction::newValue);

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

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(ChangeExecutionListenerFieldAction.class)
					.assertEquals(ELEMENT_ID, ChangeExecutionListenerFieldAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression())
					.assertEquals("foo", ChangeExecutionListenerFieldAction::fieldName)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSource("foo-value"),
							ChangeExecutionListenerFieldAction::oldValue)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSourceExpression("${fooExpression}"),
							ChangeExecutionListenerFieldAction::newValue);
		}
	}


	@Nested
	public class AddNewListenerWithFieldInjection extends AbstractComparePatchIT {

		public AddNewListenerWithFieldInjection(@BpmnFile("mapping-none.bpmn") final BpmnModelInstance from,
				@BpmnFile("execution-listener-start-add-field-value.bpmn") final BpmnModelInstance to) {
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

			changeProcessActions.assertExactlyOneInstanceOf(AddExecutionListenerAction.class)
					.assertEquals(ELEMENT_ID, AddExecutionListenerAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression());

			changeProcessActions
					.assertExactlyOneInstanceOf(ChangeExecutionListenerFieldAction.class)
					.assertEquals(ELEMENT_ID, ChangeExecutionListenerFieldAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression())
					.assertEquals("foo", ChangeExecutionListenerFieldAction::fieldName)
					.assertEquals(null, ChangeExecutionListenerFieldAction::oldValue)
					.assertEquals(ChangeExecutionListenerFieldAction.Config.ofSource("foo-value"),
							ChangeExecutionListenerFieldAction::newValue);

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

			changeProcessActions
					.assertSize(1)
					.nextAction()
					.assertInstanceOf(DeleteExecutionListenerAction.class)
					.assertEquals(ELEMENT_ID, DeleteExecutionListenerAction::id)
					.assertEquals("start", x -> x.key().camundaEvent())
					.assertEquals("${startListener}", x -> x.key().camundaDelegateExpression());
		}
	}

}
