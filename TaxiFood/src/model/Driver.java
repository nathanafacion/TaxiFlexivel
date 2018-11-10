package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import model.Passenger;
import model.Driver;
import model.Localization;
import utils.Pair;


public class Driver extends People {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Driver.class);
	private Car car;
	private ArrayList<PaymentType>  paymentType;
	private Status status;
	private Boolean acceptSmokers; 
	private Passenger passenger;
	
	public Driver(String name, String telephone, Localization localization, Car car, ArrayList<PaymentType> paymentType) {
		super(name,telephone,localization);
		acceptSmokers=true;
		if(car == null) {
			throw new IllegalArgumentException("Car cannot be null.");
		}		
		
		if(paymentType == null) {
			throw new IllegalArgumentException("PaymentType cannot be null.");
		}
		this.status = Status.AVAILABLE;
		this.paymentType = paymentType;
		this.car = car;
	}

	public static enum Status {

		AVAILABLE(true),
		BUSY(false),
		BLOCKED(false);
		
		private static final Set<Pair<Status, Status>> validNextStatus;

		static {
			final Set<Pair<Status, Status>> validStatus = new HashSet<>();
			validStatus.add(Pair.newPair(AVAILABLE, BUSY));
			validStatus.add(Pair.newPair(BUSY, AVAILABLE));
			validStatus.add(Pair.newPair(AVAILABLE, BLOCKED));
			validStatus.add(Pair.newPair(BLOCKED, AVAILABLE));

			validNextStatus = Collections.unmodifiableSet(validStatus);
		}

		public static void validateNextStatus(final Status currentStatus, final Status nextStatus) {
			final Pair<Status, Status> transition = Pair.newPair(currentStatus, nextStatus);
			if (!validNextStatus.contains(transition)) {
				throw new IllegalArgumentException("Invalid Transition: " + currentStatus + " to " + nextStatus);
			}
		}

		private final boolean initialState;

		Status(final boolean initialState) {
			this.initialState = initialState;
		}

		public boolean isInitialState() {
			return initialState;
		}
	}

    public void setAcceptSmoking(Boolean acceptSmoking) {
    	if (acceptSmoking == null) {
    	    throw new IllegalArgumentException("acceptSmoking cannot be null.");
    	}
    	this.acceptSmokers = acceptSmoking;
    }
	
    public Boolean getAcceptSmokers() {
    	return acceptSmokers;
    }
	
	public Status getStatus() {
		return status;
	}

	public ArrayList<PaymentType> getPaymentType() {
		return paymentType;
	}
	
	public void changeStatus(final Status newStatus) {
		logger.info("Driver " + this.getName() + " Current Status: "+status.toString());
		logger.info("New Status: "+newStatus.toString());
		Status.validateNextStatus(status, newStatus);
		this.status = newStatus;
	}
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public static class UnfeasibleOperationException extends Exception {

		private static final long serialVersionUID = -794175677552184441L;

		public UnfeasibleOperationException(final String message) {
			super(message);
		}

		public UnfeasibleOperationException(final String message, final Throwable throwable) {
			super(message, throwable);
		}
	}

	public synchronized void accept(final Passenger passenger) throws UnfeasibleOperationException {
		if (this.passenger != null) {
			throw new IllegalStateException("Cannot accept two passengers.");
		}

		logger.info("Accept: Change Status...");
		passenger.changeStatus(Passenger.Status.WAITING_TAXI);
		changeStatus(Status.BUSY);

		this.passenger = passenger;
	}
	

	public synchronized void reachesDestination() throws UnfeasibleOperationException {
		if (passenger == null) {
			throw new UnfeasibleOperationException("Unknown passenger");
		}

		logger.info("reachesDestination: Change Status...");
		passenger.changeStatus(Passenger.Status.FINISH_ROTE);
		changeStatus(Status.AVAILABLE);

		passenger = null;
	}

	
	@Override
	public String toString() {
		return "Driver [car=" + car + ", getCar()=" + getCar() + ", getName()=" + getName() + ", getTelephone()="
				+ getTelephone() + ", getLocalization()=" + getLocalization()
				+ super.toString()
				+ "]";
	}

	
	
}
