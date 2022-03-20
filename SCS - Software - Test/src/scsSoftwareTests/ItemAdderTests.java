package scsSoftwareTests;

import static org.junit.Assert.*;
import org.junit.*;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import java.math.BigDecimal;
import scsSoftware.ItemAdder;
import scsSoftware.ProductInventory;
import scsSoftware.ItemProductPair;
import scsSoftware.ProductCart;


public class ItemAdderTests {
	public BarcodeScanner scanner;
	public ItemAdder adder;
	public ProductInventory inventory;
	public ProductCart cart;
	public Numeral[] code1 = new Numeral[] {Numeral.zero, Numeral.zero, Numeral.one};
	public Numeral[] code2 = new Numeral[] {Numeral.zero, Numeral.zero, Numeral.two};
	public Barcode bc1 = new Barcode(code1); //001
	public Barcode bc2 = new Barcode(code2); //002
	public BarcodedItem item1 = new BarcodedItem(bc1, 3);
	public BarcodedItem item2 = new BarcodedItem(bc2, 4);
	public BarcodedProduct prod1 = new BarcodedProduct(bc1, "Bread", new BigDecimal(5));
	public BarcodedProduct prod2 = new BarcodedProduct(bc2, "Milk", new BigDecimal(10));
	public int cartSize;

	@Before
	public void setUp() {
		scanner = new BarcodeScanner();
		inventory = new ProductInventory();
		inventory.addInventory(bc1, item1, prod1);
		inventory.addInventory(bc2, item2, prod2);
		cart = new ProductCart();
		adder = new ItemAdder(inventory, cart);
		scanner.attach(adder);
		scanner.endConfigurationPhase();
		cartSize = cart.getItemNames().size();
	}

	@After
	public void tearDown() {
		scanner.detachAll();
		scanner = null;
		adder = null;
		cart = null;
		inventory = null;
	}

	@Test
	public void itemPriceAddedToCart() {
		scanner.scan(item1);
		//next two if statements simulate someone retrying to scan a couple times if the first scan doesn't work
		if (cart.getItemNames().size() == cartSize) {
			scanner.scan(item1);
		}
		if (cart.getItemNames().size() == cartSize) {
			scanner.scan(item1);
		}
		assertTrue(prod1.getPrice().equals(cart.getTotalPrice()));
	}
	
	@Test
	public void itemNameAddedToCart() {
		scanner.scan(item1);
		//next two if statements simulate someone retrying to scan a couple times if the first scan doesn't work
		if (cart.getItemNames().size() == cartSize) {
			scanner.scan(item1);
		}
		if (cart.getItemNames().size() == cartSize) {
			scanner.scan(item1);
		}
		assertTrue(cart.getItemNames().contains(prod1.getDescription()));
	}
	
	@Test
	public void scannerDisabledAfterScan() {
		scanner.scan(item2);
		//next two if statements simulate someone retrying to scan a couple times if the first scan doesn't work
		if (cart.getItemNames().size() == cartSize) {
			scanner.scan(item1);
		}
		if (cart.getItemNames().size() == cartSize) {
			scanner.scan(item1);
		}
		assertTrue(scanner.isDisabled());
	}

}
