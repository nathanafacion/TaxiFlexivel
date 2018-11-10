package utils;

import java.util.Objects;

public final class Pair<F, S> {

	private final F first;
	private final S second;

	// lazy properties
	private int hash = 0;
	private String toString;

	private Pair(final F first, final S second) {
		this.first = Objects.requireNonNull(first);
		this.second = Objects.requireNonNull(second);
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public static <F, S> Pair<F, S> newPair(final F first, final S second) {
		return new Pair<>(first, second);
	}
	
	@Override
	public int hashCode() {
		int h = hash;
		if (h == 0) {
			h = Objects.hash(first, second);
			hash = h;
		}
		return h;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof Pair<?, ?>))
			return false;

		final Pair<?, ?> other = (Pair<?, ?>) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}

	@Override
	public String toString() {
		if (toString == null) {
			toString = "(" + first + ", " + second + ")";
		}
		return toString;
	}

}
