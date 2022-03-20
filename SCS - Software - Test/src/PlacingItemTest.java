import static org.junit.Assert.assertTrue;

import org.junit.*;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SimulationException;

public class PlacingItemTest {
	
	
	@Test
	public void testCheckAdd() {
		ItemStub item1 = new ItemStub(24.99);
		ElectronicScale scale = new ElectronicScale(50, 3);
		scale.endConfigurationPhase();
		PlacingItem temp = new PlacingItem();
		scale.attach(temp);
		
		temp.itemWeightAdded(item1.getWeight());
		scale.add(item1);		
	}
	
	@Test
	public void testCheckAdd1() {
		ItemStub item1 = new ItemStub(24.99);
		ElectronicScale scale = new ElectronicScale(15, 3);
		scale.endConfigurationPhase();
		PlacingItem temp = new PlacingItem();
		scale.attach(temp);
		temp.itemWeightAdded(item1.getWeight());
		
		boolean test = false;
		try {
			scale.add(item1);
		} catch (SimulationException e) {
		    test = true;
		}
		assertTrue(test);
	}
	
	@Test
	public void testCheckAdd2() {
		ItemStub item1 = new ItemStub(24.99);
		ItemStub wrongItem = new ItemStub(28.91);
		ElectronicScale scale = new ElectronicScale(50, 3);
		scale.endConfigurationPhase();
		PlacingItem temp = new PlacingItem();
		scale.attach(temp);
		temp.itemWeightAdded(item1.getWeight());
		
		boolean test = false;
		try {
			scale.add(wrongItem);
		} catch (SimulationException e) {
		    test = true;
		}
		assertTrue(test);
	}
	
	@Test
	public void testCheckAdd3() {
		ItemStub item1 = new ItemStub(24.99);
		ItemStub item2 = new ItemStub(23.91);
		ElectronicScale scale = new ElectronicScale(50, 3);
		scale.endConfigurationPhase();
		PlacingItem temp = new PlacingItem();
		scale.attach(temp);
		
		temp.itemWeightAdded(item1.getWeight());
		scale.add(item1);
		
		temp.itemWeightAdded(item2.getWeight());
		scale.add(item2);
	}	
}
