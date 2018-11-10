package service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;

import model.Driver;
import model.Driver.UnfeasibleOperationException;
import model.Passenger;
import utils.Pair;
import utils.FilterDriver;
import utils.FilterPassenger;
import matcher.Matcher;
import matcher.Matcher.MatcherResult;
public class Service {
	private final Object lock = new Object();
	private final Matcher matcher;
    
	private final Set<Driver> drivers = new HashSet<Driver>();
	private final Set<Passenger> passengers = new HashSet<Passenger>();
	private final HashMap<Integer,Set<Driver>> driversHash = new HashMap<>();
	private final HashMap<Integer,Set<Passenger>> passengersHash = new HashMap<>();
	
	private static final Logger logger = Logger.getLogger(Service.class);

	public Service(final Matcher matcher, final int K) {
		this.matcher = Objects.requireNonNull(matcher);
		// Thread matcher
		final Thread [] threadsMatcher = new Thread[K]; 
		
		for (int i=0; i< K;i++) {
			threadsMatcher[i] = new Thread(new MatcherTask(i), "Matcher");
			threadsMatcher[i].setDaemon(true);
			threadsMatcher[i].start();
			logger.info("Create ThreadMatcher:"+i);
		}

	}
	
	private class MatcherTask implements Runnable {

		final int key;
		
		public MatcherTask(int key){
			this.key = key; 
		}
		
		@Override
		public void run() {
			while (true) {
				final MatcherResult matcherResult;
				final Set<Driver> emptyDrivers;
				final Set<Passenger> noCabPassengers;
				synchronized (lock) {
					emptyDrivers = FilterDriver.filter(driversHash.get(this.key), Driver.Status.AVAILABLE);
					noCabPassengers = FilterPassenger.filter(passengersHash.get(this.key), Passenger.Status.AVAILABLE);
				}
				matcherResult = matcher.match(emptyDrivers, noCabPassengers);
				for (final Pair<Driver, Passenger> match : matcherResult.getMatches()) {
					try {
						match.getFirst().accept(match.getSecond());
					} catch (final UnfeasibleOperationException e) {
						e.printStackTrace(System.out);
					}
				}
				try {
					Thread.sleep(30);
				} catch (final InterruptedException e) {
					e.printStackTrace(System.out);
				}
			}
		}


	}
    
	public void add(final Driver driver, final int position) {
		Objects.requireNonNull(driver);
		if (!driver.getStatus().isInitialState()) {
			throw new IllegalArgumentException("Only cab in initial state are alloweds.");
		}
		logger.info("Add Driver:");
		logger.info(driver.toString());
		synchronized (lock) {
			drivers.add(driver);
			driversHash.put(position, drivers);

		}
	}
	

	public void add(final Passenger passenger, final int position) {
		Objects.requireNonNull(passenger);
		if (!passenger.getStatus().isInitialState()) {
			throw new IllegalArgumentException("Only cab in initial state are alloweds.");
		}
		logger.info("Add Passenger:");
		logger.info(passenger.toString());
		synchronized (lock) {
			passengers.add(passenger);
			passengersHash.put(position, passengers);
		}
	}
}
