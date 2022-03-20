import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteSlotObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteStorageUnitObserver; // may not need this
import org.lsmr.selfcheckout.devices.observers.BanknoteValidatorObserver;

public class PayWithBankNote implements BanknoteSlotObserver, BanknoteValidatorObserver {


/**
 * This class represents the use case of paying with a bank note at the self checkout station. 
 * Needs to be integrated into a more general "Make Cash Payment" class with coin transactions. 
 * 
 * Note: When a banknote is validated by the hardware, it gets added to the running total of cash input. 
 * 		If the banknote is then ejected in error, it is removed from the running total. This prevents case where user 
 * 		inputs a valid note, gets it back, and keeps their money. 
 * 
 *  Disables input slot when enough valid money is inserted. 
 * 
 * 
 */
	
	private final BanknoteSlot slot;
	private final BanknoteValidator validator; 
	private boolean validated; // tracks whether the most recently inserted bank note was valid
	private int validatedValue; // the value of the most recently inserted VALID bank note
	
	private BigDecimal price; // The total price of the transaction the user is paying for 
	public BigDecimal runningTotal; // the running total of money the user has inserted
	
	
	
	/**
	 * Constructor to set 
	 * @param BigDecimal price - the price of the transaction
	 * @param station - the checkout station the transaction is occurring on
	 */
	public PayWithBankNote(BigDecimal price, SelfCheckoutStation station) {
		
		// Register the devices needed for the use case 
		this.slot = station.banknoteInput;
		this.validator = station.banknoteValidator;

		
		// No bank notes have been inserted yet 
		validated = false; 
		runningTotal = BigDecimal.ZERO;
		this.price = price;
		validatedValue = 0;
		
	}
	

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// ignore
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
	}

	
	@Override
	public void banknoteInserted(BanknoteSlot slot) {
		validated = false;
	}
	
	@Override
	public void banknoteEjected(BanknoteSlot slot) {
		// if a valid bank note is ejected then we need to subtract it from the running total 
		if (validated) {
			subtractFromTotal(validatedValue);
		}
	}

	@Override
	public void banknoteRemoved(BanknoteSlot slot) {
		
	}



	@Override
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
		validated = true;
		validatedValue = value;
		addToTotal(validatedValue);
	}



	@Override
	public void invalidBanknoteDetected(BanknoteValidator validator) {
		
	}

	
	// Adds a banknote's value to the customer's running total
	public void addToTotal(int value) {
		runningTotal = runningTotal.add(new BigDecimal(value));
		int temp = this.price.compareTo(runningTotal);
		// if price is equal to or smaller than the running total
		if(temp == 0 || temp == -1) {
			finishedTransaction(this.slot);
		}
	}
	
	// Removes a banknote's value from the customer's running total (happens when valid bank note is ejected)
	public void subtractFromTotal(int value) {
		runningTotal = runningTotal.subtract(BigDecimal.valueOf(value));
	}
	
	// disable the input slot when transaction is over
	private void finishedTransaction(BanknoteSlot slot) {
		slot.disable();
	}
	
	public BigDecimal getRemainingPrice() {
		return this.price.subtract(runningTotal);
	}
}
