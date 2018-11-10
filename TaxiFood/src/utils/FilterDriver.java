package utils;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import model.Driver;
import model.Driver.Status;
import org.apache.log4j.Logger;

import main.Main;
/**
 * Oferece utilitários relacionados a {@link Cab}
 *
 */
public class FilterDriver {
	private static final Logger logger = Logger.getLogger(FilterDriver.class);
	
	private FilterDriver() {
		throw new AssertionError();
	}

	public static <C extends Driver> Set<C> filter(final Collection<C> cabs, final Status status) {
		//logger.info("Filter Driver Status: "+status.toString());
		if(cabs != null) {
			return cabs.stream().filter(c -> c.getStatus() == status).collect(Collectors.toSet());
		}
	    return null;
		
	}

}
