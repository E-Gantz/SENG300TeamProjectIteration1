import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;

public class PlacingItem implements ElectronicScaleObserver{
	
	private boolean overload = false;
	private double lastWeight = 0.0;
	private double newWeight = 0.0;
	private double itemWeight = 0.0;
	
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
		if (newWeight == lastWeight + itemWeight) {
			lastWeight = newWeight;
			itemWeight = 0.0;
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

}
