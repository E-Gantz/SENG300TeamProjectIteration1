package scsSoftwareTests;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.*;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import scsSoftware.ItemProductPair;
import scsSoftware.PrintOutReceipt;
import scsSoftware.ProductCart;

public class ReceiptTest {
	public ProductCart cart;
	public Numeral[] code1 = new Numeral[] {Numeral.zero, Numeral.zero, Numeral.one};
	public Numeral[] code2 = new Numeral[] {Numeral.zero, Numeral.zero, Numeral.two};
	public Barcode bc1 = new Barcode(code1); //001
	public Barcode bc2 = new Barcode(code2); //002
	public BarcodedItem item1 = new BarcodedItem(bc1, 3);
	public BarcodedItem item2 = new BarcodedItem(bc2, 4);
	public BarcodedProduct prod1 = new BarcodedProduct(bc1, "Bread", new BigDecimal(5));
	public BarcodedProduct prod2 = new BarcodedProduct(bc2, "Milk", new BigDecimal(10));

	@Test
	public void TestReceiptPrint() {
		ReceiptPrinter printer = new ReceiptPrinter();
		cart = new ProductCart();
		ItemProductPair pair1 = new ItemProductPair(item1, prod1);
		cart.addToCart(pair1);
		ItemProductPair pair2 = new ItemProductPair(item2, prod2);
		cart.addToCart(pair2);
		printer.addInk(15);
		printer.addPaper(8);
		printer.endConfigurationPhase();
		PrintOutReceipt receiptPrintout = new PrintOutReceipt(cart, printer);
		receiptPrintout.printReceipt();
		String returnedReceipt = printer.removeReceipt();
		assertEquals(returnedReceipt, "Bread\nMilk\n");
	}

}
