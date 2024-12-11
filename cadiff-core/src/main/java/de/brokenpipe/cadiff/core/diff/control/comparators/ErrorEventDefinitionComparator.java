package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeErrorEventDefinitionAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ErrorEventDefinitionComparator
		implements PropertyComparator<BaseElement, String> {

	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {

		if (compareContext.from() instanceof final ThrowEvent fromThrowEvent && compareContext.to() instanceof final ThrowEvent toThrowEvent) {
			return compare(x -> ((ThrowEvent) x).getEventDefinitions(), compareContext);
		}

		if (compareContext.from() instanceof final CatchEvent fromCatchEvent && compareContext.to() instanceof final CatchEvent toCatchEvent) {
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
							compareContext)
							.findFirst();

					// if we already have a compare result -> fine
					if (compareResult.isPresent()) {
						return compareResult.stream();
					}

					// special case: error definition created, but no error referenced
					if (eventDefAccessor.apply(compareContext.from()).stream().map(BaseElement::getId)
							.noneMatch(x -> Objects.equals(x, eventDefinitionId))
							&& eventDefAccessor.apply(compareContext.to()).stream().map(BaseElement::getId)
							.anyMatch(x -> Objects.equals(x, eventDefinitionId))) {
						return Stream.of(
								new ChangeErrorEventDefinitionAction(compareContext.to().getId(), eventDefinitionId, null, null));
					}

					return Stream.empty();
				});

	}
}
