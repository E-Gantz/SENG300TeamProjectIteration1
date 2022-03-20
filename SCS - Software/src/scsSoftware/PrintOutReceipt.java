package scsSoftware;

import java.util.ArrayList;

import org.lsmr.selfcheckout.devices.ReceiptPrinter;

public class PrintOutReceipt {
	ProductCart prodCart;
	ReceiptPrinter printer;
	public ArrayList<String> items;
	
	public PrintOutReceipt(ProductCart prodCart,ReceiptPrinter printer) {
		this.prodCart = prodCart;
		this.items = prodCart.getItemNames();
		this.printer  = printer;
	}
	
	public void printReceipt() {
		String workingOn;
		items = prodCart.getItemNames();
		for (int i=0; i<items.size(); i++) {
			workingOn = items.get(i);
			for (int j=0; j<workingOn.length(); j++) {
				printer.print(workingOn.charAt(j));
			}
			//maybe print the price here too
			printer.print('\n');
		}
		printer.cutPaper();
	}
}
