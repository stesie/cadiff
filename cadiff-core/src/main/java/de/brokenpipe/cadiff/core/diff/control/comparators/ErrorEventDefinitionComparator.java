package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeErrorEventDefinitionAction;
import org.camunda.bpm.model.bpmn.instance.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ErrorEventDefinitionComparator
		implements PropertyComparator<BaseElement, String> {

	@Override
	public Stream<Action> apply(final BaseElement from, final BaseElement to) {

		if (from instanceof final ThrowEvent fromThrowEvent && to instanceof final ThrowEvent toThrowEvent) {
			return compare(x -> ((ThrowEvent) x).getEventDefinitions(), fromThrowEvent, toThrowEvent);
		}

		if (from instanceof final CatchEvent fromCatchEvent && to instanceof final CatchEvent toCatchEvent) {
			return compare(x -> ((CatchEvent) x).getEventDefinitions(), fromCatchEvent, toCatchEvent);
		}

		return Stream.empty();
	}

	private Stream<Action> compare(final Function<BaseElement, Collection<EventDefinition>> eventDefAccessor,
			final BaseElement from, final BaseElement to) {
		return Stream.concat(eventDefAccessor.apply(from).stream(), eventDefAccessor.apply(to).stream())
				.map(BaseElement::getId)
				.distinct()
				.flatMap(eventDefinitionId -> {
					final Optional<Action> compareResult = compareProperty(
							baseElement -> {
								final var ed = eventDefAccessor.apply(baseElement).stream()
										.filter(x -> Objects.equals(x.getId(), eventDefinitionId))
										.findFirst();

								if (ed.isEmpty()) {
									return null;
								}

								if (ed.get() instanceof final ErrorEventDefinition errorEventDefinition) {
									return Optional.ofNullable(errorEventDefinition.getError()).map(BaseElement::getId)
											.orElse(null);
								}

								throw new RuntimeException("can this happen?");
							},
							(x, oldValue, newValue) ->
									new ChangeErrorEventDefinitionAction(x, eventDefinitionId, oldValue, newValue),
							from, to)
							.findFirst();

					// if we already have a compare result -> fine
					if (compareResult.isPresent()) {
						return compareResult.stream();
					}

					// special case: error definition created, but no error referenced
					if (eventDefAccessor.apply(from).stream().map(BaseElement::getId)
							.noneMatch(x -> Objects.equals(x, eventDefinitionId))
							&& eventDefAccessor.apply(to).stream().map(BaseElement::getId)
							.anyMatch(x -> Objects.equals(x, eventDefinitionId))) {
						return Stream.of(
								new ChangeErrorEventDefinitionAction(to.getId(), eventDefinitionId, null, null));
					}

					return Stream.empty();
				});

	}
}
