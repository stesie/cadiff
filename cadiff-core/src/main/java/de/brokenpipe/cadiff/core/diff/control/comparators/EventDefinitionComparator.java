package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeErrorEventDefinitionAction;
import de.brokenpipe.cadiff.core.actions.ChangeSignalEventDefinitionAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.comparators.PropertyComparator.compareProperty;

public class EventDefinitionComparator implements Comparator {

	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {

		if (compareContext.from() instanceof ThrowEvent && compareContext.to() instanceof ThrowEvent) {
			return compare(x -> ((ThrowEvent) x).getEventDefinitions(), compareContext);
		}

		if (compareContext.from() instanceof CatchEvent && compareContext.to() instanceof CatchEvent) {
			return compare(x -> ((CatchEvent) x).getEventDefinitions(), compareContext);
		}

		return Stream.empty();
	}

	private Stream<Action> compare(final Function<BaseElement, Collection<EventDefinition>> eventDefAccessor,
			final CompareContext<? extends BaseElement> compareContext) {
		return Stream.concat(eventDefAccessor.apply(compareContext.from()).stream(),
						eventDefAccessor.apply(compareContext.to()).stream())
				.map(BaseElement::getId)
				.distinct()
				.flatMap(eventDefinitionId -> {

					final Function<BaseElement, Optional<EventDefinition>> edLookup =
							baseElement -> eventDefAccessor.apply(baseElement).stream()
									.filter(x -> Objects.equals(x.getId(), eventDefinitionId))
									.findFirst();

					final Optional<Action> compareResult = compareProperty(
							baseElement -> {
								final Optional<EventDefinition> ed = edLookup.apply(baseElement);

								if (ed.isEmpty()) {
									return null;
								}

								final BaseElement refElement = switch (ed.get()) {
									case final ErrorEventDefinition errorEventDefinition ->
											errorEventDefinition.getError();
									case final SignalEventDefinition signalEventDefinition ->
											signalEventDefinition.getSignal();
									// case final MessageEventDefinition messageEventDefinition -> messageEventDefinition.getMessage();
									// case final TimerEventDefinition timerEventDefinition -> timerEventDefinition.getTimeDate();
									// case final ConditionalEventDefinition conditionalEventDefinition -> conditionalEventDefinition.getCondition();
									// case final EscalationEventDefinition escalationEventDefinition -> escalationEventDefinition.getEscalation();
									default -> throw new RuntimeException("can this happen?");
								};

								return Optional.ofNullable(refElement).map(BaseElement::getId).orElse(null);
							},
							(x, oldValue, newValue) -> {
								final EventDefinition ed = edLookup.apply(compareContext.from())
										.or(() -> edLookup.apply(compareContext.to()))
										.orElseThrow(() -> new IllegalStateException("where did it go!?"));
								return createAction(ed, x, eventDefinitionId, oldValue, newValue);
							},
							compareContext)
							.findFirst();

					// if we already have a compare result -> fine
					if (compareResult.isPresent()) {
						return compareResult.stream();
					}

					final var edFrom = edLookup.apply(compareContext.from());
					final var edTo = edLookup.apply(compareContext.to());

					// special case: error definition created, but no error referenced
					if (edFrom.isEmpty() && edTo.isPresent()) {
						return Stream.of(
								createAction(edTo.get(), compareContext.to().getId(), eventDefinitionId, null, null));
					}

					return Stream.empty();
				});

	}

	private static Action createAction(final EventDefinition ed, final String id, final String eventDefinitionId,
			final String oldValue, final String newValue) {

		final var clazz = switch (ed) {
			case final ErrorEventDefinition ignored -> ChangeErrorEventDefinitionAction.class;
			case final SignalEventDefinition ignored -> ChangeSignalEventDefinitionAction.class;
			default -> throw new RuntimeException("can this happen?");
		};

		try {
			return clazz.getConstructor(String.class, String.class, String.class, String.class)
					.newInstance(id, eventDefinitionId, oldValue, newValue);
		} catch (final InstantiationException | IllegalAccessException | NoSuchMethodException |
				InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
