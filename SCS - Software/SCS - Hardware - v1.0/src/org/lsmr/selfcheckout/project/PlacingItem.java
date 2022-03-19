import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;

public class PlacingItem implements ElectronicScaleObserver{
	
	private ElectronicScale scale;
	private boolean overload = false;
	private double weight = 0.0;
	private Item newItem;
	
	public PlacingItem (ElectronicScale scale, Item newItem) throws OverloadException {
		this.scale = scale;
		this.scale.attach(this);
		this.newItem = newItem;
		this.weight = scale.getCurrentWeight();
	}
	
	
	public void checkScaleAdd() throws OverloadException{
		double currentWeight = scale.getCurrentWeight();
		double itemWeight = newItem.getWeight();
		scale.add(newItem);
		if (scale.getCurrentWeight() == currentWeight + itemWeight)
			return;
		else if(overload)
			throw new SimulationException(new IllegalStateException("The scale is overload now, please remove the last item"));
		else
			throw new SimulationException("The adding item was not correct one on the scale.");
	}
	
	public double getLastWeight() {
		return weight;
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
		this.weight = weightInGrams;
	}

	@Override
	public void overload(ElectronicScale scale) {
		this.overload = true;
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
