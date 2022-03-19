import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.SimulationException;

public class Checkout {
	boolean state;
	ProductCart pcart;
	BarcodeScanner scanner;
	
	public Checkout(BarcodeScanner scanner, ProductCart pcart) {
		this.scanner = scanner;
		this.pcart = pcart;
		state = false;
	}
	
	/**
	 * Changes the state to enable checkout to occur
	 * @throws SimulationException
	 *             If the scanner is disabled and the bagging area is waiting for items.
	 * @throws SimulationException
	 *             If the cart is empty.
	 */
	public void enable() {
		if(scanner.isDisabled()) {
			throw new SimulationException("Need to place all items in bagging area before proceeding to checkout.");
		}
		
		if(pcart.cart.isEmpty()) {
			throw new SimulationException("Cart must contain items in order to proceed to checkout.");
		}
		
		scanner.disable();
		state = true;
	}
	
}
