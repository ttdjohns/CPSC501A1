package ca.ucalgary.seng300.a1;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;

import org.lsmr.vending.hardware.PushButton;
/**
 *Interface logic, makes it easier to test listener devices
 */
public interface VendingLogicInterface {

	//getter for EL
	public EventLogInterface getEventLog();
	
	public int getCurrencyValue();
	
	
	
	/**
	 * Method for displaying a message for 5 seconds and erase it for 10s, if credit in VM is zero.
	 */
	
	public void welcomeMessageTimer();

	/**
	 * A method to push a welcome message to the display
	 */
	public void welcomeMessage();
	
	/**
	 * A method to send an OutOfOrder message to the display
	 */
	public void vendOutOfOrder();
	
	/**
	 * A method to push the currently accumulated credit to the display
	 */
	public void displayCredit();
	
	/**
	 * A method to display the price of the pop at a specific index 
	 * @param index - the selection number that corresponds to the desired pop
	 */
	public void displayPrice(int index);
	
	/**
	 * Method to show that an invalid coin was inserted
	 */
	public void invalidCoinInserted();
	
	/**
	 * Method called by the coinSlotListener to accumulate credit when valid coins are inserted.
	 * Update the credit and update the display 
	 * @param coin  The Coin that was inserted
	 */
	public void validCoinInserted(Coin coin);
	
	/**
	 * Method to confirm that the product is being dispensed 
	 */
	public void dispensingMessage();
	
	/**
	 * A method to return change to the user
	 */
	public void returnChange();
	
	/** 
	 * A method to determine what action should be done when a button is pressed 
	 * TODO how is disabling a part going to affect what action is taken?
	 * @param button
	 */
	public void determineButtonAction(PushButton button);

	/**
	 * A method to determine which pop can rack or push button an event has occurred on
	 * needed for EventLog information
	 * 
	 * @param hardware - the hardware that the event occurred on 
	 * @return The index of the hardware according to the vending machine. -1 means error could not find
	 */
	public int findHardwareIndex(AbstractHardware<? extends AbstractHardwareListener> hardware);
	
	/**
	 * Method to disable a piece of hardware. If hardware is a selection button or pop rack, machine can remain 
	 *   operational, otherwise, disable vending machine 
	 * @param hardware
	 */
	public void disableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware);
	
	/**
	 * Method to disable a piece of hardware. If hardware is a selection button or pop rack, machine can remain 
	 *   operational, otherwise, disable vending machine 
	 * @param hardware
	 */
	public void enableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware);
	
}
