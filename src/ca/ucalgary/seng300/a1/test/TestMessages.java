package ca.ucalgary.seng300.a1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a1.MessageDriver;
import ca.ucalgary.seng300.a1.VendingLogic;
import ca.ucalgary.seng300.a1.VendingSetup;

public class TestMessages {

	private VendingMachine vm;
	private MessageDriver msgDriver;
	

	@Before
	public void setup()
	{
		VendingSetup vendset = new VendingSetup();
		vm = vendset.getVendingMachine();
		msgDriver  = vendset.getVendingLogic().msgDriver;
	}
	
	@After 
	public void teardown() {
		vm = null;
		msgDriver = null;
	}
	
	@Test
	public void testWelcome() {
		assertEquals(vm.getDisplay().getMessage(), "Hi There!");
	}
	
	@Test
	public void testClearMsg() {
		msgDriver.clearDisplayMessage();
		assertEquals(vm.getDisplay().getMessage(), "");
	}
	
	@Test
	public void testOutOfOrder() {
		msgDriver.vendOutOfOrder();
		assertEquals(vm.getDisplay().getMessage(), "Out Of Order");
	}
	
	@Test
	public void testDisplayCredit() {
		msgDriver.displayCredit(0);
		assertEquals(vm.getDisplay().getMessage(), "Current Credit: $0.0");
	}
	
	@Test
	public void testDisplayPrice() {
		msgDriver.displayPrice(0, 0);
		String message = "Price of " + vm.getPopKindName(0) + ": $" + (((double) vm.getPopKindCost(0)) / 100);
		assertEquals(msgDriver.prevMessage, message);
	}
	
	@Test
	public void testInvalidCoinMsg() {
		msgDriver.invalidCoinInserted(0);
		assertEquals(msgDriver.prevMessage, "Invalid coin!");
	}
	
	@Test
	public void testValidCoin() {
		msgDriver.validCoinInserted(25);
		assertEquals(vm.getDisplay().getMessage(), "Current Credit: $0.25");
	}
	
	@Test
	public void testDispensingMsg() {
		msgDriver.dispensingMessage();
		assertEquals(vm.getDisplay().getMessage(), "Despensing. Enjoy!");
	}
}
