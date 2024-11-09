package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.List;
import java.util.ServiceLoader;

@RequiredArgsConstructor
public class VoterRegistry {

	private final List<Voter> voters;

	public static final VoterRegistry INSTANCE = VoterRegistry.init();

	private static VoterRegistry init() {
		return new VoterRegistry(
				ServiceLoader.load(Voter.class).stream()
						.map(ServiceLoader.Provider::get)
						.toList());
	}

	public int apply(final String left, final String right, final VoteContext<? extends BaseElement> context) {
		int score = 0;

		for (final Voter voter : voters) {
			final Vote vote = voter.apply(left, right, context);

			score += switch(vote) {
				case UP -> 1;
				case NEUTRAL -> 0;
				case DOWN -> -1;
			};
		}

		return score;
	}
}
