import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteSlotObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteStorageUnitObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteValidatorObserver;

public class PayWithBankNote implements BanknoteSlotObserver, BanknoteValidatorObserver, BanknoteStorageUnitObserver {


/**
 * This class represents the use case of paying with a bank note at the self checkout station
 * 
 * When an inserted banknote is validated, it 
 * 
 * 
 */
	
	// The total banknotes that have been inserted 
	private final BanknoteSlot slot;
	private final BanknoteValidator validator; 
	private final BanknoteStorageUnit storage;
	private boolean validated; // tracks whether the most recently inserted bank note was valid
	private int validatedValue; // the value of the most recently inserted VALID bank note
	private BigDecimal runningTotal; // the running total of money the user has inserted
	private BigDecimal price; // The total price of the transaction the user is paying for 
	
	
	
	/**
	 * Basic constructor, don't know what this needs yet 
	 */
	public PayWithBankNote(BigDecimal price, SelfCheckoutStation station) {
		
		// Register the devices needed for the use case 
		this.slot = station.banknoteInput;
		this.validator = station.banknoteValidator;
		this.storage = station.banknoteStorage;
		
		// Make this class an observer of the devices so we can receive notifications
		slot.attach(this);
		validator.attach(this);
		storage.attach(this);
		
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
		// ignore
		
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
		// TODO Auto-generated method stub
		
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



	@Override
	public void banknotesFull(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void banknotesLoaded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}
	
	
	// Adds a banknote's value to the customer's running total
	public void addToTotal(int value) {
		runningTotal.add(BigDecimal.valueOf(value));
	}
	
	// Removes a banknote's value from the customer's running total (happens when valid bank note is ejected)
	public void subtractFromTotal(int value) {
		runningTotal.subtract(BigDecimal.valueOf(value));
	}
	
}
