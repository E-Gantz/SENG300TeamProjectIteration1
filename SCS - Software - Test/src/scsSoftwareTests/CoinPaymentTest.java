package scsSoftwareTests;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.DisabledException;
//import org.lsmr.selfcheckout.devices.OverloadException;
//import org.lsmr.selfcheckout.devices.SimulationException;

import scsSoftware.CoinPayment;


public class CoinPaymentTest {

	public SelfCheckoutStation station; 
	public CoinPayment payment;
	public Currency currency;
	public Coin c0, c1, c2, c3, c4, c5, cInvalid;

	
	@Before
	public void setUp() {
		
		currency = Currency.getInstance("CAD");
		BigDecimal[] coinArray = {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25),
						  new BigDecimal(0.50), new BigDecimal(1.00), new BigDecimal(2.00)};
		int [] bankNoteDenom = {5, 10, 20, 50, 100};
		
		station = new SelfCheckoutStation(currency, bankNoteDenom, coinArray, 100, 1);
		// Current testing transaction price = $4.00 CAD
		payment = new CoinPayment(new BigDecimal(4.00), station);
		station.coinValidator.attach(payment);
		station.coinSlot.attach(payment);
		
		c0 = new Coin(currency, coinArray[0]);
		c1 = new Coin(currency, coinArray[1]);
		c2 = new Coin(currency, coinArray[2]);
		c3 = new Coin(currency, coinArray[3]);
		c4 = new Coin(currency, coinArray[4]);
		c5 = new Coin(currency, coinArray[5]);
	
		cInvalid = new Coin(Currency.getInstance("JPY"), new BigDecimal(1));
		
	}
	
	@Test
	public void MultipleCoinsAdded() {
		try {
			station.coinSlot.accept(c0);
			station.coinSlot.accept(c1);
			station.coinSlot.accept(c2);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		}
		
		BigDecimal totalValue = c0.getValue().add(c1.getValue().add(c2.getValue()));
		int temp = totalValue.compareTo(payment.runningTotal);
		
		assert(temp == 0);
	}
	
	
	@Test
	public void InitialCoinTotal() {
		try { 
			station.coinSlot.accept(c1);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		}
		
		int temp = c1.getValue().compareTo(payment.runningTotal);
		assert(temp == 0);
	}
	
	@Test
	public void InvalidnoteNotAdded() {
		try { 
			station.coinSlot.accept(cInvalid);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		} 

		assert(0 == payment.runningTotal.intValue());
	}
	
	@Test
	public void DisablesInputSlotWhenExactlyEnoughMoney() {
		try { 
			// Insert 4 dollars to slot 
			station.coinSlot.accept(c5);
			station.coinSlot.accept(c5);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		}

		assert(payment.getRemainingPrice().intValue() == 0);
		assert(station.coinSlot.isDisabled());
	}
	
	@Test
	public void DisablesInputSlotWhenMoreThanEnoughMoney() {
		try { 
			// Insert 5 dollars to slot 
			station.coinSlot.accept(c4);
			station.coinSlot.accept(c5);
			station.coinSlot.accept(c5);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		}

		assert(payment.getRemainingPrice().intValue() < 0);
		assert(station.coinSlot.isDisabled());
	}
	
}