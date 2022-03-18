import java.util.ArrayList;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;

public class Customer {
	
	/**
	 * SCAN : Customer is able to scan an item
	 * BAG : Customer has just scanned an item and needs to place it in the bagging area
	 * PAY : Customer has finished scanning/bagging all items and is ready to pay
	 */
	protected enum State {SCAN, BAG, PAY};
	
	protected State state;
	SelfCheckoutStation scs;
	ArrayList<Item> cart;
	double total;
	
	public Customer(SelfCheckoutStation scs) {
		this.scs = scs;
		this.cart = new ArrayList<Item>();
		this.state = State.SCAN;
		this.total = 0;
	}
	
	/**
	 * Indicates that the customer is finished scanning items. Changes state from scan to pay.
	 * @throws SimulationException
	 *             If not in the scan state.
	 * @throws SimulationException
	 *             If the cart is empty.
	 */
	public void checkout() {
		if(state != State.SCAN)
			throw new SimulationException("This method may only be called in the scan state.");
		
		if(cart.isEmpty()) {
			throw new SimulationException("Cart must contain items in order to proceed to checkout.");
		}
		
		this.state = State.PAY;
	}
	
}
