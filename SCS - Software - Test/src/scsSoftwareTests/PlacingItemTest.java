package scsSoftwareTests;

import static org.junit.Assert.*;
import org.junit.*;
import java.math.BigDecimal;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import scsSoftware.ItemProductPair;
import scsSoftware.PlacingItem;
import scsSoftware.ProductCart;

public class PlacingItemTest {
	public Numeral[] code1 = new Numeral[] {Numeral.zero, Numeral.zero, Numeral.one};
	public Numeral[] code2 = new Numeral[] {Numeral.zero, Numeral.zero, Numeral.two};
	public Barcode bc1 = new Barcode(code1); //001
	public Barcode bc2 = new Barcode(code2); //002
	public BarcodedProduct prod1 = new BarcodedProduct(bc1, "Bread", new BigDecimal(5));
	public BarcodedProduct prod2 = new BarcodedProduct(bc2, "Milk", new BigDecimal(10));
	private ElectronicScale scale;
	private ProductCart cart;
	private BarcodeScanner scanner;
	private PlacingItem temp;
	BarcodedItem item1;
	
	@Before
	public void setUp() {
		scale = new ElectronicScale(50, 3);
		scale.endConfigurationPhase();
		cart = new ProductCart();
		scanner = new BarcodeScanner();
		scanner.endConfigurationPhase();
		temp = new PlacingItem(scanner, cart);
		scale.attach(temp);
		item1 = new BarcodedItem(bc2, 24.99);
	}

	@After
	public void tearDown() {
		scale.detachAll();
		scale = null;
		cart = null;
		scanner = null;
		temp = null;
		item1 = null;
	}
	
	
	@Test
	public void testCheckAdd() throws OverloadException {
		ItemProductPair pair1 = new ItemProductPair(item1, prod1);
		cart.addToCart(pair1);
		scale.add(item1);
		assertEquals(24.99, temp.getLastWeight(), 0.1);
	}
	
	@Test
	public void testCheckAdd1() {
		ElectronicScale scale1 = new ElectronicScale(15, 3);
		scale1.attach(temp);
		boolean test = false;
		try {
			scale1.add(item1);
		} catch (SimulationException e) {
		    test = true;
		}
		assertTrue(test);
	}
	
	@Test
	public void testCheckAdd2() {
		BarcodedItem wrongItem = new BarcodedItem(bc1, 28.91);
		ItemProductPair pair1 = new ItemProductPair(item1, prod1);
		cart.addToCart(pair1);
		boolean test = false;
		try {
			scale.add(wrongItem);
		} catch (SimulationException e) {
		    test = true;
		}
		assertTrue(test);
	}
	
	@Test
	public void testCheckAdd3() {
		BarcodedItem item2 = new BarcodedItem(bc1, 23.91);
		
		ItemProductPair pair1 = new ItemProductPair(item1, prod1);
		cart.addToCart(pair1);
		scale.add(item1);
		
		ItemProductPair pair2 = new ItemProductPair(item2, prod2);
		cart.addToCart(pair2);
		scale.add(item2);
		assertEquals((item1.getWeight() + item2.getWeight()), temp.getLastWeight(), 0.1);
	}
	
	@Test
	public void scannerReEnabled() {
		ItemProductPair pair1 = new ItemProductPair(item1, prod1);
		cart.addToCart(pair1);
		scanner.disable();
		scale.add(item1);
		assertTrue(!(scanner.isDisabled()));
	}
}
