package matcher;

import java.util.HashSet;
import java.util.Set;

import model.Driver;
import model.Passenger;
import utils.Pair;

/**
 * Classe esqueleto de {@link Matcher}.
 *
 */
public abstract class AbstractMatcher implements Matcher {

	@Override
	public final MatcherResult match(final Set<Driver> Drivers, final Set<Passenger> passengers) {

		if(Drivers == null || passengers == null) {
			return MatcherResult.EMPTY;
		} 
		 
		if (Drivers.isEmpty() || passengers.isEmpty()) {
			return MatcherResult.EMPTY;
		}

		final Set<Pair<Driver, Passenger>> matches = doMatch(Drivers, passengers);

		final Set<Driver> matchedDrivers = new HashSet<>();
		final Set<Passenger> matchedPassengers = new HashSet<>();

		for (final Pair<Driver, Passenger> selected : matches) {
			matchedDrivers.add(selected.getFirst());
			matchedPassengers.add(selected.getSecond());
		}

		final Set<Driver> unmatchedDrivers = new HashSet<>(Drivers);
		unmatchedDrivers.removeAll(matchedDrivers);

		final Set<Passenger> unmatchedPassengers = new HashSet<>(passengers);
		unmatchedPassengers.removeAll(matchedPassengers);

		MatcherResult.validate(Drivers, passengers, matches, unmatchedDrivers, unmatchedPassengers);

		return new MatcherResult(matches, unmatchedDrivers, unmatchedPassengers);
	}

	protected abstract Set<Pair<Driver, Passenger>> doMatch(Set<Driver> Drivers, Set<Passenger> passengers);

}
