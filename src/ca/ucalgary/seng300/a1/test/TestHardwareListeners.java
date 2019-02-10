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
	public void testInitilizedListeners() {
		int temp;
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
			temp = vm.getSelectionButton(i).numListenersRegistered();
			assertEquals("testInitilizedListeners found " + temp + " listeners attached to selection button " + i, temp, 1);
		}
		for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) { 
			temp = vm.getCoinRack(i).numListenersRegistered();
			assertEquals("testInitilizedListeners found " + temp + " listeners attached to CoinRack " + i, temp, 1);
		}
		for (int i = 0; i < vm.getNumberOfPopCanRacks(); i++) {
			temp = vm.getPopCanRack(i).numListenersRegistered();
			assertEquals("testInitilizedListeners found " + temp + " listeners attached to PopCanRack " + i, temp, 1);
		}
		temp = vm.getCoinReceptacle().numListenersRegistered();
		assertEquals("testInitilizedListeners found " + temp + " listeners attached to CoinReceptacle ", temp, 1);
		temp = vm.getCoinReturn().numListenersRegistered();
		assertEquals("testInitilizedListeners found " + temp + " listeners attached to CoinReturn ", temp, 1);
		temp = vm.getOutOfOrderLight().numListenersRegistered();
		assertEquals("testInitilizedListeners found " + temp + " listeners attached to OutOfOrderLight ", temp, 1);
		temp = vm.getDisplay().numListenersRegistered();
		assertEquals("testInitilizedListeners found " + temp + " listeners attached to Display ", temp, 1);
		
	}
	
}
