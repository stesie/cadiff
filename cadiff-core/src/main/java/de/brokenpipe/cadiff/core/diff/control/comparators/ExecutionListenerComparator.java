package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.ExecutionListenerKey;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddExecutionListenerAction;
import de.brokenpipe.cadiff.core.actions.ChangeExecutionListenerFieldAction;
import de.brokenpipe.cadiff.core.actions.DeleteExecutionListenerAction;
import de.brokenpipe.cadiff.core.diff.control.AbstractSimpleWalker;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaField;

import java.util.Collection;
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
				final ExecutionListenerKey key = ExecutionListenerKey.of(executionListener);

				return Stream.concat(
					Stream.of(new AddExecutionListenerAction(listenerContext.id(), key)),
					new FieldWalker(listenerContext.id(), key, Collections.emptyList(),
							executionListener.getCamundaFields()).walk());
			}

			@Override
			protected Stream<Action> handleUpdated(final CamundaExecutionListener from,
					final CamundaExecutionListener to) {
				// FIXME handle camunda script
				return new FieldWalker(listenerContext.id(), ExecutionListenerKey.of(from),
						from.getCamundaFields(), to.getCamundaFields())
						.walk();
			}

			@Override
			protected Stream<Action> handleRemoved(final CamundaExecutionListener executionListener) {
				return Stream.of(new DeleteExecutionListenerAction(listenerContext.id(),
						ExecutionListenerKey.of(executionListener)));
			}
		}.walk();
	}

	static class FieldWalker extends AbstractSimpleWalker<String, CamundaField> {

		private final String id;
		private final ExecutionListenerKey key;

		public FieldWalker(final String id, final ExecutionListenerKey key, final Collection<CamundaField> from,
				final Collection<CamundaField> to) {
			super(from, to);

			this.id = id;
			this.key = key;
		}

		@Override
		protected String extractKey(final CamundaField element) {
			return element.getCamundaName();
		}

		@Override
		protected Stream<Action> handleAdded(final CamundaField added) {
			return Stream.of(new ChangeExecutionListenerFieldAction(id, key, added.getCamundaName(),
					null, ChangeExecutionListenerFieldAction.Config.from(added)));
		}

		@Override
		protected Stream<Action> handleUpdated(final CamundaField from, final CamundaField to) {
			final var fromConfig = ChangeExecutionListenerFieldAction.Config.from(from);
			final var toConfig = ChangeExecutionListenerFieldAction.Config.from(to);

			if (fromConfig.equals(toConfig)) {
				return Stream.empty();
			}

			return Stream.of(new ChangeExecutionListenerFieldAction(id, key, from.getCamundaName(),
					fromConfig, toConfig));
		}

		@Override
		protected Stream<Action> handleRemoved(final CamundaField removed) {
			return Stream.of(new ChangeExecutionListenerFieldAction(id, key, removed.getCamundaName(),
					ChangeExecutionListenerFieldAction.Config.from(removed), null));
		}
	}

}
