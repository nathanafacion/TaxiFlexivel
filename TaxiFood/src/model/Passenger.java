package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import utils.Pair;

public class Passenger extends People {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Passenger.class);
	private PaymentType paymentType;
    private Status status;
	private Boolean isSmoker; 

	public Passenger(String name, String telephone, Localization localization, PaymentType paymentType, Boolean isSmoking) {
		super(name,telephone,localization);
		
		if (PaymentType.valueOf(paymentType.toString()) == null) {
				throw new IllegalArgumentException("paymentType cannot be null.");
		}
		
		if(isSmoking == null) {
			    throw new IllegalArgumentException("isSmoking cannot be null.");
		}
		
		this.paymentType = paymentType;
		this.status = Status.AVAILABLE;
		this.isSmoker = isSmoking;
	}
	
	public static enum Status {

		AVAILABLE(true),
		WAITING_TAXI(false),
		FINISH_ROTE(false),
		GIVE_UP(false);

		private static final Set<Pair<Status, Status>> validNextStatus;

		static {
			final Set<Pair<Status, Status>> validStatus = new HashSet<>();
			validStatus.add(Pair.newPair(AVAILABLE, WAITING_TAXI));
			validStatus.add(Pair.newPair(WAITING_TAXI, FINISH_ROTE));
			validStatus.add(Pair.newPair(AVAILABLE, GIVE_UP));
			validStatus.add(Pair.newPair(WAITING_TAXI, GIVE_UP));

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

    public void setIsSmoker(Boolean isSmoker) {
    	if (isSmoker == null) {
    	    throw new IllegalArgumentException("isSmoker cannot be null.");
    	}
    	this.isSmoker = isSmoker;
    }
    

    public Boolean getIsSmoker() {
    	return isSmoker;
    }
	
	public Status getStatus() {
		return status;
	}

	
	public void changeStatus(final Status newStatus) {
		
		logger.info("Passenger " + this.getName() + " current Status: "+status.toString());
		logger.info("New Status: " + newStatus.toString());
		Status.validateNextStatus(status, newStatus);
		this.status = newStatus;
	}
	
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return "Passenger [paymentType=" + paymentType + ", getName()=" + getName() + ", getTelephone()="
				+ getTelephone() +  ", getLocalization()=" + getLocalization()
				+super.toString()
				+ "]";
	}
		
}
