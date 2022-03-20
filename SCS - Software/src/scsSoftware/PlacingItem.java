package scsSoftware;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;

public class PlacingItem implements ElectronicScaleObserver{
	
	private boolean overload;
	private double lastWeight;
	private double newWeight;
	private double itemWeight;
	private BarcodeScanner scanner;
	private ProductCart productCart;
	
	public PlacingItem(BarcodeScanner scanner, ProductCart cart) {
		this.scanner = scanner;
		this.productCart = cart;
		this.lastWeight = 0.0;
		this.newWeight = 0.0;
		this.itemWeight = 0.0;
		this.overload = false;
	}
	
	
	public void itemWeightAdded (double itemWeight){
		this.itemWeight = itemWeight;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		this.newWeight = weightInGrams;
		this.itemWeight = productCart.cart.get((productCart.cart.size())-1).getWeight();
		if (newWeight == lastWeight + itemWeight) {
			this.lastWeight = newWeight;
			this.itemWeight = 0.0;
			this.scanner.enable();
			return;
		}
		else
			throw new SimulationException("The adding item was not correct one on the scale.");
	}

	@Override
	public void overload(ElectronicScale scale) {
		this.overload = true;
		throw new SimulationException(new IllegalStateException("The scale is overload now, please remove the last item"));
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		// TODO Auto-generated method stub
	}
	
	public double getLastWeight() {
		return this.lastWeight;
	}

}
