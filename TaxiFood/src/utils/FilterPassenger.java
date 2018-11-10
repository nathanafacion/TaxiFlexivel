package utils;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import model.Passenger;
import model.Passenger.Status;

/**
 * Oferece utilitários relacionados a {@link Cab}
 *
 */
public class FilterPassenger {
	private static final Logger logger = Logger.getLogger(FilterDriver.class);
	
	private FilterPassenger() {
		throw new AssertionError();
	}

	public static <C extends Passenger> Set<C> filter(final Collection<C> passengers, final Status status) {
		//logger.info("Filter Passenger Status: "+status.toString());
		if(passengers != null) {
			return passengers.stream().filter(c -> c.getStatus() == status).collect(Collectors.toSet());
		}
		return null;
	}
}
