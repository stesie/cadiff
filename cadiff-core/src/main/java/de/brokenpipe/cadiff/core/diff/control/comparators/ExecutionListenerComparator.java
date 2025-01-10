package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddExecutionListenerAction;
import de.brokenpipe.cadiff.core.actions.DeleteExecutionListenerAction;
import de.brokenpipe.cadiff.core.diff.control.AbstractSimpleWalker;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Comparator for execution listeners. We don't try to identify updates. Just compare from and to lists and create add
 * and remove actions as needed.
 */
public class ExecutionListenerComparator implements Comparator {
	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		final CompareContext<List<CamundaExecutionListener>> listenerContext = compareContext.mapValue(
				x -> Optional.ofNullable(x.getExtensionElements())
						.map(y -> y.getElementsQuery().filterByType(CamundaExecutionListener.class).list())
						.orElse(Collections.emptyList()));

		return new AbstractSimpleWalker<>(listenerContext.from(), listenerContext.to()) {

			@Override
			protected ExecutionListenerKey extractKey(final CamundaExecutionListener element) {
				return ExecutionListenerKey.of(element);
			}

			@Override
			protected Stream<Action> handleAdded(final CamundaExecutionListener executionListener) {
				// FIXME synthetically call update
				return Stream.of(new AddExecutionListenerAction(
						listenerContext.id(),
						executionListener.getCamundaEvent(),
						executionListener.getCamundaClass(),
						executionListener.getCamundaDelegateExpression(),
						executionListener.getCamundaExpression()
				));
			}

			@Override
			protected Stream<Action> handleUpdated(final CamundaExecutionListener from,
					final CamundaExecutionListener to) {
				// FIXME handle camunda script & fields, maybe create update when fields are changed !?
				return Stream.empty();
			}

			@Override
			protected Stream<Action> handleRemoved(final CamundaExecutionListener executionListener) {
				return Stream.of(new DeleteExecutionListenerAction(
						listenerContext.id(),
						executionListener.getCamundaEvent(),
						executionListener.getCamundaClass(),
						executionListener.getCamundaDelegateExpression(),
						executionListener.getCamundaExpression()
				));
			}
		}.walk();
	}

	record ExecutionListenerKey(String camundaEvent, String camundaClass,
								String camundaDelegateExpression, String camundaExpression) {
		static ExecutionListenerKey of(final CamundaExecutionListener listener) {
			return new ExecutionListenerKey(listener.getCamundaEvent(), listener.getCamundaClass(),
					listener.getCamundaDelegateExpression(), listener.getCamundaExpression());
		}
	}
}
