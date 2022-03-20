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
	public ProductCart cart;
	
	public ItemAdder(ProductInventory inventory, ProductCart cart) {
		this.productInventory = inventory;
		this.cart = cart;
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
		ItemProductPair itemProduct = productInventory.getInventory(barcode);
		//BarcodedItem scannedItem = itemProduct.getItem();
		//BarcodedProduct scannedProduct = itemProduct.getProduct();
		
		cart.addToCart(itemProduct);
		//should throw up a 'please add item to bagging area' screen on the gui here
		barcodeScanner.disable();
	}
}