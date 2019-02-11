package ca.ucalgary.seng300.a1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a1.VendingLogic;
import ca.ucalgary.seng300.a1.VendingSetup;

public class TestFindHardwareIndex {
	private VendingMachine vm;
	private VendingLogic logic;
	

	@Before
	public void setup()
	{
		VendingSetup vendset = new VendingSetup();
		vm = vendset.getVendingMachine();
		logic  = vendset.getVendingLogic();
	}
	
	@After 
	public void teardown() {
		vm = null;
		logic = null;
	}
	
	@Test
	public void testFindHardwareOnCoinRacks() {
		for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
			assertEquals("failed to identify coin rack " + i, logic.findHardwareIndex(vm.getCoinRack(i)), i);
		}
	}
	
	@Test
	public void testFindHardwareOnSelectionButtons() {
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
			assertEquals("failed to identify selection button " + i, logic.findHardwareIndex(vm.getSelectionButton(i)), i);
		}
	}
	
	@Test
	public void testFindHardwareOnPopCanRack() {
		for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
			assertEquals("failed to identify pop can rack " + i, logic.findHardwareIndex(vm.getCoinRack(i)), i);
		}
	}
	
}
