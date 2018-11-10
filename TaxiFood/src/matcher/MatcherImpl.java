package matcher;

import java.util.Objects;
import java.util.Set;
import rules.Restriction;
import rules.Restrictions;
import model.Driver;
import model.Passenger;
import utils.Pair;

/**
 * Implementação de {@link Matcher} baseada em uma {@link Restriction} e
 * {@link Selector}.
 *
 */
public class MatcherImpl extends AbstractMatcher {

	private final Restriction restriction;

	public MatcherImpl(Restriction restriction) {
		this.restriction = Objects.requireNonNull(restriction);
	}

	@Override
	protected Set<Pair<Driver, Passenger>> doMatch(Set<Driver> Drivers, Set<Passenger> passengers) {
		return Restrictions.getPossiblesMatches(Drivers, passengers,
				restriction);
	}

}
