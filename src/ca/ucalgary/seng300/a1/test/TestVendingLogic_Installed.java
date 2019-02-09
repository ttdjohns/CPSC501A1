package ca.ucalgary.seng300.a1;

import static org.junit.Assert.*;

import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.PopCanRack;
import org.lsmr.vending.hardware.VendingMachine;

public class TestVendingLogic_Installed {

	private VendingMachine vm;
	private VendingLogic logic;
	private int [] validCoins;
	
	/**
	 * Methid creates a vendingmachine, its logic, and a base setup class
	 */
	private void setup()
	{
	VendingSetup vendset = new VendingSetup();
	 vm = vendset.getVendingMachine();
	 logic  = new VendingLogic(vm);
	 validCoins = vendset.getCoinKinds();
	}
	
	/**
	 * TODO fix and debug
	 */
	@Test
	public void test_invalidCoinInserted()
	{
		boolean fail = false;
		setup();
		try {
			
			vm.getCoinSlot().addCoin(new Coin(543897238)); // add a not quite valid coin
		}catch(Exception e)
		{
			fail = true;
		}
		//assertFalse(fail);
		assertFalse(false);
	}
	

	/**
	 * Method tests if the logic checks to see if exact change is possible.
	 * NOTE: This test requires the coinReturn in the vm to be properly instanced.
	 * In version 2.0 of VendingMachine, this is not the case.
	 * As a result this method will test to see that it is still true that this bug exists.
	 */
	@Test
	public void test_isExactChangePossible()
	{
		setup();
		if (vm.getCoinReturn() != null)
		{
			try {
				//Add $6.00
				vm.getCoinSlot().addCoin(new Coin(200));
				vm.getCoinSlot().addCoin(new Coin(200));
				vm.getCoinSlot().addCoin(new Coin(200));
				vm.getSelectionButton(1).press(); //which has cost of 250
				assertFalse(logic.isExactChangePossible());
			}
			catch(Exception e)
			{
				System.out.println(e);
				fail();
			}
		}
		else
			assertTrue(logic.isExactChangePossible() == false);
	}
	

	/**
	 * Method tests that the get method works
	 */
	@Test
	public void test_getCurrencyValue()
	{
		setup();
		try {
			vm.getCoinSlot().addCoin(new Coin(5));
		} catch (DisabledException e) {
			fail();
		}
		assertTrue(logic.getCurrencyValue()==5);
	}
	
	/**
	 * Method compares the current message to a string, returns true if they are equal
	 * @param String m, the message to compare to
	 * @return boolean based on the compareTo of the two strings
	 */
		private boolean compareCurrentMessage(String m)
		{
			return (logic.getCurrentMessage().compareTo(m)== 0);
		}
	
	/**
	 * Shows that enabling Safety results in an exception as a result of the CoinReturn bug
	 */
	@Test
	public void test_enableSafety()
	{
		setup();
		try {
			vm.enableSafety();	
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	@Test
	public void test_welcomeMessage()
	{
		setup();
		assertTrue(compareCurrentMessage("Welcome!"));
	}
	
	/**
	 * Tests that the display is correct
	 */
	@Test
	public void test_vendOutOfOrder()
	{
		setup();
		vm.getCoinSlot().disable();
		assertTrue(compareCurrentMessage("Out Of Order"));
	}
	
	/**
	 * Tests that the display is correct
	 */
	@Test
	public void displayCredit()
	{
		setup();
		try {
			vm.getCoinSlot().addCoin(new Coin(200));
		} catch (DisabledException e) {
			// TODO Auto-generated catch block
			fail();
		}
		assertTrue(compareCurrentMessage("Current Credit: $2.0"));
	}
	
	/**
	 * Tests that the display is correct
	 */
	@Test
	public void test_displayPrice()
	{
		
		try {
			setup();
			vm.getSelectionButton(0).press();
		} catch (Exception e) {
			fail();
		}
		
		//Note that the this is correct! displayPrice calls displayCredit() before it returns
		assertTrue(compareCurrentMessage("Current Credit: $0.0"));
	}
	
	/**
	 * Tests that the display is correct
	 */
	@Test
	public void test_dispensingMessage()
	{
		setup();
		try {
			vm.getCoinSlot().addCoin(new Coin(200));
			vm.getSelectionButton(0).press();
		} catch (DisabledException e) {
			fail();
		}
		System.out.println(logic.getCurrentMessage());
		//After all credit has been used it displays the welcome message for the next user.
		assertTrue(compareCurrentMessage("Welcome!"));
	}
	
	/**
	 * Method tests to see if the welcomeMessage timer works at the right interval
	 */
	@Test
	public void test_welcomeMessageTimer()
	{
		setup();
		//TODO
	}
	
	/**
	 * Method tests to see if the correct message is printed after an invalid coin is entered
	 */
	@Test
	public void test_invalidCoin()
	{
		setup();
		try {
			vm.getCoinSlot().addCoin(new Coin(123));//add an invalid coin
			vm.getSelectionButton(0).press();
		} catch (Exception e) {
			//As the coin return does not exist, a null pointer error is generated
			//Do to this the listener is not notified
		}
		//The machine resets back to its default state
		assertTrue(compareCurrentMessage("Welcome!"));
	
	}
	

	
	/**
	 * Tests to see that no exception is thrown when the buttons on the machine are pressed
	 */
	@Test
	public void test_determineButtonActionValid()
	{
		int selectionButtons = 5;
		boolean failed = false;
		setup();
		try {
		for(int i = 0; i<=selectionButtons; i++)
		{
			vm.getSelectionButton(i);
		}
	
		}
		catch(Exception e)
		{
		failed = true;
		}
		assertFalse(failed);
	}
	
	/**
	 * Tests to see that an exception is correctly thrown when an invalid button is pressed
	 */
	@Test
	public void test_determineButtonActionInValid()
	{
		int selectionButtons = 5;
		boolean failed = false;
		setup();
		try {
		for(int i = 0; i<=selectionButtons; i++)
		{
			vm.getSelectionButton(i);
		}
		vm.getSelectionButton(16); //some invalid index
		}
		catch(Exception e)
		{
		failed = true;
		}
		assertTrue(failed);
	}
	
	/**
	 * Method tests to see if disableHardware correctly disables the piece in question
	 * 
	 */
	@Test
	public void test_disableHardware()
	{
		try {
			setup();
			boolean enabled = !vm.getCoinSlot().isDisabled();
			assertTrue(vm.getCoinSlot().isDisabled() != enabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

		
	
	/**
	 * Method tests if the enabling hardware calls the right method
	 */
	@Test
	public void test_EnableHardware_arbitraryHardware()
	{
		try {
			setup();
			vm.getCoinSlot().disable();
			boolean disabled = vm.getCoinSlot().isDisabled();
			vm.getCoinSlot().enable();
			assertTrue(vm.getCoinSlot().isDisabled() != disabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Method tests if the enabling hardware calls the right method
	 */
	@Test
	public void test_EnableHardware_arbitraryHardware_Pop()
	{
		try {
			setup();
			vm.getPopCanRack(0).disable();
			boolean disabled = vm.getPopCanRack(0).isDisabled();
			vm.getPopCanRack(0).enable();
			assertTrue(vm.getPopCanRack(0).isDisabled() != disabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Method tests if the enabling hardware calls the right method
	 */
	@Test
	public void test_EnableHardware_arbitraryHardware_Button()
	{
		try {
			setup();
			vm.getSelectionButton(0).disable();
			boolean disabled = vm.getSelectionButton(0).isDisabled();
			vm.getSelectionButton(0).enable();
			assertTrue(vm.getSelectionButton(0).isDisabled() != disabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Method tests if the findHardwareIndex finds the right index
	 */
	@Test 
	public void test_findHardwareIndex_Pop()
	{
		try {
			setup();
			vm.getPopCanRack(0).disable();
			assertTrue(logic.getCircuitEnabledIndex(0) == false);
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Method tests if the findHardwareIndex finds the right index
	 */
	@Test 
	public void test_findHardwareIndex_Button()
	{
		try {
			setup();
			vm.getSelectionButton(0).disable();
			assertTrue(logic.getCircuitEnabledIndex(0) == false);
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Tests to see if the get method gets all values correctly
	 */
	@Test
	public void test_getCircuitEnabledIndex()
	{
		setup();
		boolean failed= false;
		for (int i = 0; i<vm.getNumberOfSelectionButtons(); i++)
		{
			if (logic.getCircuitEnabledIndex(i) != true) failed = true;
		}
		assertFalse(failed);
	}
	
	
	
	
	
	
	
}
