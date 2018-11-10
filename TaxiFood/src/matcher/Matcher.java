package matcher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import model.Driver;
import model.Passenger;
import utils.Pair;


/**
 * Implementações dessa interface são responsáveis por realizar o casamento de
 * {@link Driver}s e {@link Passenger}.
 * 
 */
public interface Matcher {
	
	public static class MatcherResult {

		public static MatcherResult EMPTY = new MatcherResult(Collections.emptySet(), Collections.emptySet(),
				Collections.emptySet());

		/**
		 * Método utilitário para validar a corretude do resultado. As
		 * invariantes são:<br>
		 * <li>Todos os {@link Driver}s iniciais devem estar presentes no resultado
		 * e somente estes.
		 * <li>Todos os {@link Passenger}s iniciais devem estar presentes no
		 * resultado e somente estes.
		 * <li>Um {@link Driver} não pode ser ao mesmo tempo matched e unmatched
		 * <li>Um {@link Passenger} não pode ser ao mesmo tempo matched e
		 * unmatched
		 * 
		 * @param initialDrivers
		 *            {@link Driver}s iniciais.
		 * @param initialPassengers
		 *            {@link Passenger}s iniciais
		 * @param matches
		 *            Casamento realizados
		 * @param unmatchedPassengers
		 *            {@link Driver}s sem {@link Passenger}
		 * @param unmatchedPassengers
		 *            {@link Passenger} sem {@link Driver}s
		 */
		public static void validate(Set<Driver> initialDrivers, Set<Passenger> initialPassengers,
				Set<Pair<Driver, Passenger>> matches, Set<Driver> unmatchedDrivers, Set<Passenger> unmatchedPassengers) {
			final Set<Driver> matchesDrivers = matches.stream().map(Pair::getFirst).collect(Collectors.toSet());
			final Set<Passenger> matchesPassengers = matches.stream().map(Pair::getSecond).collect(Collectors.toSet());

			if (!Collections.disjoint(matchesDrivers, unmatchedPassengers)) {
				throw new IllegalStateException("Drivers cannot be match and unmatched in same time.");
			}

			if (!Collections.disjoint(matchesPassengers, unmatchedPassengers)) {
				throw new IllegalStateException("Passengers cannot be match and unmatched in same time.");
			}

			final Set<Driver> allDrivers = new HashSet<>(matchesDrivers);
			allDrivers.addAll(unmatchedDrivers);
			if (!initialDrivers.equals(allDrivers)) {
				throw new IllegalStateException("All Drivers must be returned.");
			}

			final Set<Passenger> allPassengers = new HashSet<>(matchesPassengers);
			allPassengers.addAll(unmatchedPassengers);
			if (!initialPassengers.equals(allPassengers)) {
				throw new IllegalStateException("All Drivers must be returned.");
			}

		}

		private final Set<Pair<Driver, Passenger>> matches;
		private final Set<Driver> unmatchedDrivers;
		private final Set<Passenger> unmatchedPassengers;

		public MatcherResult(Set<Pair<Driver, Passenger>> matches, Set<Driver> unmatchedDrivers,
				Set<Passenger> unmatchedPassengers) {
			this.matches = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(matches)));
			this.unmatchedDrivers = Collections.unmodifiableSet(Objects.requireNonNull(unmatchedDrivers));
			this.unmatchedPassengers = Collections.unmodifiableSet(Objects.requireNonNull(unmatchedPassengers));
		}

		public Set<Pair<Driver, Passenger>> getMatches() {
			return matches;
		}

		public Set<Driver> getUnmatchedDrivers() {
			return unmatchedDrivers;
		}

		public Set<Passenger> getUnmatchedPassengers() {
			return unmatchedPassengers;
		}

	}

	/**
	 * Realiza o casamento entre {@link Driver}s e {@link Passenger}s
	 * 
	 * @param Drivers
	 * @param passengers
	 * 
	 * @throws IllegalArgumentException
	 *             Caso os {@link Driver}s ou {@link Passenger}s especificados não
	 *             estejam em seu estado inicial
	 */
	MatcherResult match(Set<Driver> Drivers, Set<Passenger> passengers);

}
