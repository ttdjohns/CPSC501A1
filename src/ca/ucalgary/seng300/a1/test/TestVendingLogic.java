package ca.ucalgary.seng300.a1.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.PushButton;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a1.VendingLogic;
import ca.ucalgary.seng300.a1.VendingSetup;

public class TestVendingLogic {

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
	 * Method tests the get moethd for getting the coin kinds from the vm
	 */
	@Test
	public void test_getVmCoinKinds() {
		setup();
		int[] coinKinds = logic.getVmCoinKinds();
		boolean fail = false;
		for( int i = 0; i<coinKinds.length; i++)
		{
			if (coinKinds[i] != validCoins[i])
			{
				fail = true;
			}
		}
		
		assertFalse(fail);
		
	}
	
	
	/**
	 * Method tests to see if the credit changes when the an invalid coin is inserted
	 */
	@Test
	public void test_invalidCoinInserted()
	{
		
		setup();
		try {
			
			logic.invalidCoinInserted();
		}catch(Exception e)
		{
		fail();	
		}
		//assertFalse(fail);
		assertTrue(compareCurrentMessage("Current Credit: $0.0"));
	}
	
	/**
	 * Method tests to see if the credit changes correctly when a valid coin is inserted
	 */
	@Test
	public void test_validCoinInserted()
	{
		setup();
		logic.validCoinInserted(new Coin(200));
		assertTrue(logic.getCurrencyValue() == 200);
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
	 * Tests to see that no exception is thrown when the buttons on the machine are pressed
	 */
	@Test
	public void test_determineButtonActionValid()
	{
		boolean failed = false;
		setup();
		try {
		logic.determineButtonAction(vm.getSelectionButton(0));
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
		logic.determineButtonAction(new PushButton());
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
			logic.disableHardware(vm.getCoinSlot());
			assertTrue(vm.getCoinSlot().isDisabled() != enabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Method tests to see if the hardware is correctly enabled
	 */
	@Test
	public void test_EnableHardware_arbitraryHardware()
	{
		try {
			setup();
			vm.getCoinSlot().disable();
			boolean disabled = !vm.getCoinSlot().isDisabled();
			logic.enableHardware(vm.getCoinSlot());
			assertTrue(vm.getCoinSlot().isDisabled() != disabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Method tests to see if the hardware is correctly enabled
	 */
	@Test
	public void test_EnableHardware_arbitraryHardware_Pop()
	{
		try {
			setup();
			vm.getPopCanRack(0).disable();
			boolean disabled = !vm.getPopCanRack(0).isDisabled();
			logic.enableHardware(vm.getPopCanRack(0));
			assertTrue(vm.getPopCanRack(0).isDisabled() != disabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/**
	 * Method tests to see if the hardware is correctly enabled
	 */
	@Test
	public void test_EnableHardware_arbitraryHardware_Button()
	{
		try {
			setup();
			vm.getSelectionButton(0).disable();
			boolean disabled = !vm.getSelectionButton(0).isDisabled();
			logic.enableHardware(vm.getSelectionButton(0));
			assertTrue(vm.getSelectionButton(0).isDisabled() != disabled);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	/**
	 * Tests to see that all the display calls do not result in an exception
	 */
	@Test
	public void test_DisplayCallsException()
	{
		setup();
		try {
		
			logic.displayPrice(0);
			logic.displayCredit();
			logic.welcomeMessage();
			logic.vendOutOfOrder();
			logic.invalidCoinInserted();
		}
		catch(Exception e)
		{
			fail();
		}
		
	}
	

	/**
	 * Tests that the display is correct
	 */
	@Test
	public void test_welcomeMessage()
	{
		setup();
		logic.welcomeMessage();
		assertTrue(compareCurrentMessage("Welcome!"));
	}
	
	/**
	 * Tests that the display is correct
	 */
	@Test
	public void test_vendOutOfOrder()
	{
		setup();
		logic.vendOutOfOrder();
		assertTrue(compareCurrentMessage("Out Of Order"));
	}
	
	/**
	 * Tests that the display is correct
	 */
	@Test
	public void displayCredit()
	{
		setup();
		logic.displayCredit();
		assertTrue(compareCurrentMessage("Current Credit: $0.0"));
	}
	
	/**
	 * Tests that the display is correct
	 */
	@Test
	public void test_displayPrice()
	{
		setup();
		logic.displayPrice(0);
		
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
		logic.dispensingMessage();
		assertTrue(compareCurrentMessage("Despensing. Enjoy!"));
	}
	
	/**
	 * Method checks to see if the timer throws any exceptions
	 *TODO fix implementation
	 */
	@Test
	public void test_welcomeMessagetimer()
	{
		try {
			setup();
			logic.welcomeMessageTimer();
		}catch(Exception e)
		{
			//System.out.println("*** : " + e);
			fail();
		}
	}

	/**
	 * Method teststhe right index is found
	 */
	@Test
	public void test_findHardwareIndex_Pop()
	{
		setup();
		assertTrue(logic.findHardwareIndex(vm.getPopCanRack(0))==0);
	}

	/**
	 * Method teststhe right index is found
	 */
	@Test
	public void test_findHardwareIndex_SelectButton()
	{
		setup();
		assertTrue(logic.findHardwareIndex(vm.getSelectionButton(2))==2);
	}
	
	/**
	 * Method checks that the method returns the error flag, which should cause an exception if used as an array index
	 */
	@Test
	public void test_findHardwareIndex_ConfigButton()
	{
		setup();
		assertTrue(logic.findHardwareIndex(vm.getConfigurationPanel().getButton(0))==-1);
	}
	//DetermineActionButton will be used in the future, so for we test that no exceptions are thrown
	
	/**
	 * Method tests to see that this does not throw an exception
	 */
	@Test
	public void test_determineButtonAction_selectionButton()
	{
		try {
		setup();
		logic.determineButtonAction(vm.getSelectionButton(0));
		}catch(Exception e)
		{
			fail();
		}
		assertTrue(true);
	}
	
	/**
	 * Method tests to see that this does not throw an exception
	 */
	@Test
	public void test_determineButtonAction_config() {
		try {
			setup();
			logic.determineButtonAction(vm.getConfigurationPanel().getButton(0));
		}catch(Exception e)
		{
			fail();
		}
		assertTrue(true);
	}
	
	/**
	 * Method tests to see that this does not throw an exception
	 */
	@Test
	public void test_determineButtonAction_config_enter()
	{
		try {
			setup();
			logic.determineButtonAction(vm.getConfigurationPanel().getEnterButton());
		}catch(Exception e)
		{
			fail();
		}
		assertTrue(true);
	}
	
	/**
	 * Method tests to see that this does not throw an exception
	 */
	@Test
	public void test_determineButtonAction_invalid()
	{
		try {
			setup();
			logic.determineButtonAction(new PushButton());
		}catch(Exception e)
		{
			assertTrue(true);
		}
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
	
	
	
	
	
	
	
}