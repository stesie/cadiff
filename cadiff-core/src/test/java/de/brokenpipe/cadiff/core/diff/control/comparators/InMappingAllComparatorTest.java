package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.ChangeInMappingAllAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMappingAllComparatorTest {

	@Test
	void testNewNonLocalAllInMapping(@BpmnElement(file = "camunda-in.bpmn", id = "NoInMapping") final BaseElement from,
			@BpmnElement(file = "camunda-in.bpmn", id = "PropagateAll") final BaseElement to) {
		final var comparator = new InMappingAllComparator();
		final var result = comparator.apply(new CompareContext<>(null, null, from, to)).toList();

		assertEquals(1, result.size());
		assertInstanceOf(ChangeInMappingAllAction.class, result.getFirst());

		final var action = (ChangeInMappingAllAction) result.getFirst();
		assertEquals("PropagateAll", action.id());
		assertEquals(ChangeInMappingAllAction.Config.disabled(), action.oldValue());
		assertEquals(new ChangeInMappingAllAction.Config(true, Boolean.FALSE), action.newValue());
	}

	@Test
	void testNewLocalAllInMapping(@BpmnElement(file = "camunda-in.bpmn", id = "NoInMapping") final BaseElement from,
			@BpmnElement(file = "camunda-in.bpmn", id = "PropagateAllLocal") final BaseElement to) {
		final var comparator = new InMappingAllComparator();
		final var result = comparator.apply(new CompareContext<>(null, null, from, to)).toList();

		assertEquals(1, result.size());
		assertInstanceOf(ChangeInMappingAllAction.class, result.getFirst());

		final var action = (ChangeInMappingAllAction) result.getFirst();
		assertEquals("PropagateAllLocal", action.id());
		assertEquals(ChangeInMappingAllAction.Config.disabled(), action.oldValue());
		assertEquals(new ChangeInMappingAllAction.Config(true, Boolean.TRUE), action.newValue());
	}

	@Test
	void testUnchanged(@BpmnElement(file = "camunda-in.bpmn", id = "PropagateAllLocal") final BaseElement from,
			@BpmnElement(file = "camunda-in.bpmn", id = "PropagateAllLocal") final BaseElement to) {
		final var comparator = new InMappingAllComparator();
		final var result = comparator.apply(new CompareContext<>(null, null, from, to)).toList();

		assertTrue(result.isEmpty());
	}

	@Test
	void testRemoveInAllMapping(@BpmnElement(file = "camunda-in.bpmn", id = "PropagateAllLocal") final BaseElement from,
			@BpmnElement(file = "camunda-in.bpmn", id = "NoInMapping") final BaseElement to) {
		final var comparator = new InMappingAllComparator();
		final var result = comparator.apply(new CompareContext<>(null, null, from, to)).toList();

		assertEquals(1, result.size());
		assertInstanceOf(ChangeInMappingAllAction.class, result.getFirst());

		final var action = (ChangeInMappingAllAction) result.getFirst();
		assertEquals("NoInMapping", action.id());
		assertEquals(new ChangeInMappingAllAction.Config(true, Boolean.TRUE), action.oldValue());
		assertEquals(ChangeInMappingAllAction.Config.disabled(), action.newValue());
	}

}