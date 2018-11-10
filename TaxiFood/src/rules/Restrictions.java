package rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import utils.Pair;
import model.Driver;
import model.Passenger;

/**
 * Oferece utilitários relacionados a {@link Restriction}
 *
 */
public class Restrictions {
	private static final Logger logger = Logger.getLogger(Restrictions.class);
	private static final ThreadPoolExecutor executor;
	private static int threadCount;

	static {
		executor = new ThreadPoolExecutor(50, 100, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(50000),
				new ThreadFactory() {
					@Override
					public Thread newThread(final Runnable target) {
						final Thread result = new Thread(target, "Restrictions " + threadCount++);
						result.setDaemon(true);
						return result;
					}
				});
	}

	/**
	 * Essa classe não tem a finalidade de criar objetos.
	 */
	private Restrictions() {
		throw new AssertionError();
	}

	/**
	 * Define quais são os pares {@link Cab} e {@link Passenger}s possíveis,
	 * isto é, aqueles pares que não desrespeitam nenhuma {@link Restriction}
	 * imposta.
	 * 
	 * @param cabs
	 * @param passengers
	 * @param restriction
	 * @return
	 */
	public static Set<Pair<Driver, Passenger>> getPossiblesMatches(final Set<Driver> drivers, final Set<Passenger> passengers,
			final Restriction restriction) {
		final Set<Pair<Driver, Passenger>> result = new HashSet<>();
		final List<Future<?>> futures = new ArrayList<>();
		final Set<Passenger> passengerMatched = new HashSet<>();
		for (final Driver cab : drivers) {
			final Set<Passenger> feasiblePassengers = Collections.synchronizedSet(new HashSet<>());
			for (final Passenger passenger : passengers) {
				futures.add(executor.submit(new Runnable() {
					@Override
					public void run() {
						if (! passengerMatched.contains(passenger) && restriction.isFeasible(cab, passenger)) {
							feasiblePassengers.add(passenger);
						}
					}
				}));
			}
			if (feasiblePassengers.iterator().hasNext()) {
				Passenger passengerMatcher = feasiblePassengers.iterator().next();
				result.add(Pair.newPair(cab, passengerMatcher));
				passengerMatched.add(passengerMatcher);
				logger.info("Passenger matcher:"+passengerMatcher.toString());
			}
		}
		futures.forEach(f -> {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});

		return result;
	}

}
