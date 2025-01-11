package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaFormKeyAction;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangePropertyActionPrinterTest {

	@Spy
	private ChangePropertyActionPrinter printer;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private ActionPrintContext context;

	private ChangeCamundaFormKeyAction action;

	@BeforeEach
	void setUp() {
		final BpmnModelInstance modelInstance = mock(BpmnModelInstance.class);
		when(context.getFrom()).thenReturn(modelInstance);
		when(context.getTo()).thenReturn(modelInstance);

		final ModelElementInstance modelElementInstance = mock(BaseElement.class);
		lenient().when(modelInstance.getModelElementById("foo")).thenReturn(modelElementInstance);

		final ModelElementType elementType = mock(ModelElementType.class);
		lenient().when(modelElementInstance.getElementType()).thenReturn(elementType);

		action = new ChangeCamundaFormKeyAction("foo", "old-key", "new-key");
	}

	@Test
	void shouldSupportAction() {
		assertTrue(printer.supports(action));
		assertFalse(printer.supports(mock(Action.class)));
	}

	@Test
	void shouldProperlyPrintAction() {
		printer.accept(context, action);

		// Verify that the methods are called with expected parameters
		verify(printer).startBlock(context, "foo", AbstractActionPrinter.ChangeType.UPDATE);
		verify(printer).writeLine(context, action, " -> ", AbstractActionPrinter.ChangeType.UPDATE);

		// You would also verify printAttributeChangesForId if it were accessible
		verify(printer).printAttributeChangesForId(context, "foo", AbstractActionPrinter.ChangeType.UPDATE);
	}
}
