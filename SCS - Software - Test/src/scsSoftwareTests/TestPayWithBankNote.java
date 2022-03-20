package scsSoftwareTests;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;

import scsSoftware.PayWithBankNote;


/**
 * Tests the expected behavior of the "Complete payment with bank note" use case as implemented by the self-checkout system
 * hardware. 
 * 
 * @author Quyanna
 *
 */
public class TestPayWithBankNote {

	public SelfCheckoutStation station; 
	public PayWithBankNote payment;
	public Currency c;
	public Banknote b0, b1, b2, b3, b4, bInvalid;

	
	@Before
	public void setUp() {
		
		c = Currency.getInstance("CAD");
		BigDecimal[] coinArry = {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25),
						  new BigDecimal(0.50), new BigDecimal(1.00), new BigDecimal(2.00)};
		int [] bankNoteDenom = {5, 10, 20, 50, 100};
		
		station = new SelfCheckoutStation(c, bankNoteDenom, coinArry, 100, 1);
		// Current testing transaction price = $110.00 CAD
		payment = new PayWithBankNote(new BigDecimal(110.00), station);
		station.banknoteValidator.attach(payment);
		station.banknoteInput.attach(payment);
		
		b0 = new Banknote(c, bankNoteDenom[0]);
		b1 = new Banknote(c, bankNoteDenom[1]);
		b2 = new Banknote(c, bankNoteDenom[2]);
		b3 = new Banknote(c, bankNoteDenom[3]);
		b4 = new Banknote(c, bankNoteDenom[4]);
	
		bInvalid = new Banknote(Currency.getInstance("JPY"), 1);
		
	}
	
	@Test
	public void MultipleBanknoteAdded() {
		try {
			station.banknoteInput.accept(b0);
			station.banknoteInput.accept(b1);
			station.banknoteInput.accept(b2);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		} catch (OverloadException e) {
			fail("checkout bank note storage is full");
			e.printStackTrace();
		}
		
		int totalValue = b0.getValue() + b1.getValue() + b2.getValue();
		assert(totalValue == payment.runningTotal.intValue());
	}
	
	
	@Test
	public void InitialBanknoteTotal() {
		try { 
			station.banknoteInput.accept(b1);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		} catch (OverloadException e) {
			fail("checkout bank note storage is full");
			e.printStackTrace();
		}
		
		assert(b1.getValue() == payment.runningTotal.intValue());
	}
	
	@Test
	public void InvalidnoteNotAdded() {
		try { 
			station.banknoteInput.accept(bInvalid);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		} catch (OverloadException e) {
			fail("checkout bank note storage is full");
			e.printStackTrace();
		}

		assert(0 == payment.runningTotal.intValue());
	}
	
	@Test
	public void EjectionOnValidBanknote() {
		try { 
			station.banknoteInput.accept(b2);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		} catch (OverloadException e) {
			fail("checkout bank note storage is full");
			e.printStackTrace();
		}
		
		// Check that valid bank note has been counted
		int temp = payment.runningTotal.intValue();
		assert(b2.getValue() == payment.runningTotal.intValue());
		
		try {
			station.banknoteInput.emit(b2);
		} catch (SimulationException e) {
			fail("Simulation error occured");
			e.printStackTrace();
		} catch (DisabledException e) {
			fail("Device is disabled");
			e.printStackTrace();
		}

		// Check that banknote was subtracted from total when ejected
		assert(payment.runningTotal.intValue() == temp - b2.getValue());
	}
	
	@Test
	public void DisablesInputSlotWhenExactlyEnoughMoney() {
		try { 
			// Insert 110 dollars to slot 
			station.banknoteInput.accept(b4);
			station.banknoteInput.accept(b1);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		} catch (OverloadException e) {
			fail("checkout bank note storage is full");
			e.printStackTrace();
		}

		assert(payment.getRemainingPrice().intValue() == 0);
		assert(station.banknoteInput.isDisabled());
	}
	
	@Test
	public void DisablesInputSlotWhenMoreThanEnoughMoney() {
		try { 
			// Insert 120 dollars to slot 
			station.banknoteInput.accept(b4);
			station.banknoteInput.accept(b2);
		} catch (DisabledException e) {
			fail("checkout station is disabled");
			e.printStackTrace();
		} catch (OverloadException e) {
			fail("checkout bank note storage is full");
			e.printStackTrace();
		}

		assert(payment.getRemainingPrice().intValue() < 0);
		assert(station.banknoteInput.isDisabled());
	}
	
}
