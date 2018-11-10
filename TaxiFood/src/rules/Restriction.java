package rules;

import model.Driver;
import model.Passenger;
import model.PaymentType;

public interface Restriction {

	boolean isFeasible(Driver driver, Passenger passenger);

	public static enum StandardRestriction implements Restriction {

		DRIVER_STATUS {
			@Override
			public boolean isFeasible(final Driver driver, final Passenger passenger) {

				return driver.getStatus() == Driver.Status.AVAILABLE;
			}

		},

		PASSENGER_STATUS {
			@Override
			public boolean isFeasible(final Driver driver, final Passenger passenger) {

				return passenger.getStatus() == Passenger.Status.AVAILABLE;
			}

		},
		
		SMOKING {
			@Override
			public boolean isFeasible(final Driver driver, final Passenger passenger) {
				
				return (!passenger.getIsSmoker() || (passenger.getIsSmoker() == driver.getAcceptSmokers()));
			}

		},
		
		PAYMENT {
			@Override
			public boolean isFeasible(final Driver driver, final Passenger passenger) {
				for (PaymentType p : driver.getPaymentType()) {
					if (p.equals(passenger.getPaymentType())){
						return true;
					}
				}
				return false;
			}

		},

	}

}
