package ca.ucalgary.seng300.a1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a1.VendingLogic;
import ca.ucalgary.seng300.a1.VendingSetup;

public class TestMessages {

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
	public void testWelcome() {
		assertEquals(vm.getDisplay().getMessage(), "Hi There!");
	}
	
	@Test
	public void testClearMsg() {
		logic.clearDisplayMessage();
		assertEquals(vm.getDisplay().getMessage(), "");
	}
	
	@Test
	public void testOutOfOrder() {
		logic.vendOutOfOrder();
		assertEquals(vm.getDisplay().getMessage(), "Out Of Order");
	}
	
	@Test
	public void testDisplayCredit() {
		logic.displayCredit();
		assertEquals(vm.getDisplay().getMessage(), "Current Credit: $0.0");
	}
	
	@Test
	public void testDisplayPrice() {
		logic.displayPrice(0);
		String message = "Price of " + vm.getPopKindName(0) + ": $" + (((double) vm.getPopKindCost(0)) / 100);
		assertEquals(logic.prevMessage, message);
	}
	
	@Test
	public void testInvalidCoinMsg() {
		logic.invalidCoinInserted();
		assertEquals(logic.prevMessage, "Invalid coin!");
	}
	
	@Test
	public void testValidCoin() {
		logic.validCoinInserted(new Coin(25));
		assertEquals(vm.getDisplay().getMessage(), "Current Credit: $0.25");
	}
	
	@Test
	public void testDispensingMsg() {
		logic.dispensingMessage();
		assertEquals(vm.getDisplay().getMessage(), "Despensing. Enjoy!");
	}
}
