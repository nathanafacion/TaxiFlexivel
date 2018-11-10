package rules;

import java.util.List;

import model.Driver;
import model.Passenger;

/**
 * Permite encadear várias {@link Restriction}s
 *
 */
public class ChainedRestriction implements Restriction {
	private final List<Restriction> restricions;

	public ChainedRestriction(final List<Restriction> restricions) {
		if (restricions == null || restricions.isEmpty()) {
			throw new IllegalArgumentException("Restriction cannot be null or empty.");
		}
		this.restricions = restricions;
	}

	@Override
	public boolean isFeasible(final Driver cab, final Passenger passenger) {
		for (final Restriction restriction : restricions) {
			if (!restriction.isFeasible(cab, passenger)) {
				return false;
			}
		}
		return true;
	}

}
