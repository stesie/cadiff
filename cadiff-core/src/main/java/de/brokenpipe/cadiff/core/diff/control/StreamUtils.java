package de.brokenpipe.cadiff.core.diff.control;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtils {

	public static <T> Stream<T> mergeStreams(final Collection<Stream<T>> streams) {
		return streams.stream().flatMap(x -> x);
	}
}
