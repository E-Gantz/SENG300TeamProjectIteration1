import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.*;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.devices.SimulationException;

public class CheckoutTest {
	public BarcodeScanner scanner;
	public ItemAdder adder;
	public ProductInventory inventory;
	public ProductCart cart;
	public Numeral[] code1 = new Numeral[] {Numeral.zero, Numeral.zero, Numeral.one};
	public Barcode bc1 = new Barcode(code1); //001
	public BarcodedItem item1 = new BarcodedItem(bc1, 3);
	public BarcodedProduct prod1 = new BarcodedProduct(bc1, "Bread", new BigDecimal(5));
	public Checkout checkout;
	
	@Before
	public void setUp() {
		scanner = new BarcodeScanner();
		inventory = new ProductInventory();
		inventory.addInventory(bc1, item1, prod1);
		cart = new ProductCart();
		adder = new ItemAdder(inventory, cart);
		scanner.attach(adder);
		scanner.endConfigurationPhase();
		checkout = new Checkout(scanner, cart);
	}

	@After
	public void tearDown() {
		scanner.detachAll();
		scanner = null;
		adder = null;
		cart = null;
		inventory = null;
		checkout = null;
	}

	@Test
	public void testEnable() {
		scanner.scan(item1);
		scanner.enable();
		checkout.enable();
		assertTrue(checkout.state);
	}
	
	@Test(expected = SimulationException.class)
	public void testScannerDissabled() {
		scanner.scan(item1);
		scanner.disable();
		checkout.enable();
	}
	
	@Test(expected = SimulationException.class)
	public void testEmptyCart() {
		scanner.enable();
		checkout.enable();
	}
}
