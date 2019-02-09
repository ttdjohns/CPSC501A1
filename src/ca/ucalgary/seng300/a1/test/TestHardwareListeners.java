package ca.ucalgary.seng300.a1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a1.VendingLogic;
import ca.ucalgary.seng300.a1.VendingSetup;

public class TestHardwareListeners {
	
	private VendingMachine vm;
	private VendingLogic logic;
	

	@Before
	public void setup()
	{
		VendingSetup vendset = new VendingSetup();
		vm = vendset.getVendingMachine();
		logic  = new VendingLogic(vm);
	}
	
	@After 
	public void teardown() {
		vm = null;
		logic = null;
	}
	
	/**
	 * Method to ensure that only 1 listener is registered to each piece of hardware on initialization
	 */
	@Test
	public void testPushButtonListener() {
		int temp = 0;
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
			temp = vm.getSelectionButton(i).numListenersRegistered();
			assertEquals("testPushButtonListener found " + temp + " listeners attached to selection button " + i, temp, 1);
		}
	}
}
