package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;

@RequiredArgsConstructor
public class CreatorRegistry implements Function<VoteContext<String, ? extends BaseElement>, Optional<AddAction>> {

	private final List<Creator> creators;

	public static final CreatorRegistry INSTANCE = CreatorRegistry.init();

	private static CreatorRegistry init() {
		return new CreatorRegistry(
				ServiceLoader.load(Creator.class).stream()
						.map(ServiceLoader.Provider::get)
						.sorted(Comparator.comparing(Creator::getPriority).reversed())
						.toList());
	}

	@Override
	public Optional<AddAction> apply(final VoteContext<String, ? extends BaseElement> voteContext) {
		return creators.stream()
				.flatMap(creator -> creator.apply(voteContext).stream())
				.findFirst();
	}
}
