package scsSoftware;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CoinSlotObserver;
import org.lsmr.selfcheckout.devices.observers.CoinValidatorObserver;

public class CoinPayment implements CoinSlotObserver, CoinValidatorObserver {
	
	private final CoinSlot slot;
	private final CoinValidator validator; 
	private boolean validated; 			// stores validity of most recently inserted coin
	private BigDecimal validatedValue; 	// stores the verified value of most recently inserted coin
	
	private BigDecimal price; 			// total price customer has to pay
	public BigDecimal runningTotal; 	// running total of money inserted by customer

	/**
	 * Constructor to set 
	 * @param BigDecimal price - the price of the transaction
	 * @param station - the checkout station the transaction is occurring on
	 */
	public CoinPayment (BigDecimal price, SelfCheckoutStation station) {
		
		/*
		 * devices needed for transaction
		 */
		this.slot = station.coinSlot;
		this.validator = station.coinValidator;

		
		/*
		 * Initial setup when no coins have been entered
		 */
		validated = false; 
		runningTotal = BigDecimal.ZERO;
		this.price = price;
		validatedValue = BigDecimal.ZERO;
	}
	
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// can be ignored
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// can be ignored
	}
	
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		// can be ignored
	}
	
	@Override
	public void coinInserted(CoinSlot slot) {
		// when Coin is entered, initially it is not validated
		validated = false;
	}

	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		// when a valid coin is entered it is added to the total value
		validated = true;
		validatedValue = value;
		addToTotal(validatedValue);
	}
	
	public void addToTotal (BigDecimal value) {
		// adding value of entered coin to the running total
		runningTotal = runningTotal.add(value);
		int temp = this.price.compareTo(runningTotal);
		// if price is equal to or smaller than the running total
		if(temp == 0 || temp == -1) {
			finishedTransaction(this.slot);
		}
	}
		
	private void finishedTransaction(CoinSlot slot) {
		// disable the input slot when transaction is over
		slot.disable();
	}
	
	public BigDecimal getRemainingPrice() {
		return this.price.subtract(runningTotal);
	}
}