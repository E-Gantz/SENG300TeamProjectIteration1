import java.math.BigDecimal;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;

public class ItemAdder implements BarcodeScannerObserver{
	public ProductInventory productInventory;
	
	public ItemAdder(ProductInventory inventory) {
		this.productInventory = inventory;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// Auto-generated method stub
		
	}

	@Override
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		/* Have: Bar code
		 * Need to: 
		 * -add price of item with given bar code to total due
		 * -add name of item to whatever the receipt will print based off of
		 * -bagging area should expect weight equal to the scanned products weight to be added
		 * Need:
		 * -Item with corresponding bar code to get weight
		 * -Product with corresponding bar code to get price and name
		*/
		ItemProductPair itemProduct = productInventory.getInventory(barcode);
		BarcodedItem scannedItem = itemProduct.getItem();
		BarcodedProduct scannedProduct = itemProduct.getProduct();
		
		double scannedWeight = scannedItem.getWeight();
		//tell scale to expect this amount of weight to be added
		
		BigDecimal scannedPrice = scannedProduct.getPrice();
		//add this price to the total due
		
		String productDesc = scannedProduct.getDescription();
		//add this product name to the receipt and/or cart
		
	}
}
