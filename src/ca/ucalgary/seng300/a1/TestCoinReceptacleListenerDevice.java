package ca.ucalgary.seng300.a1;

import static org.junit.Assert.*;

//TODO we really need to find a way to properly structure the file structure
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.CoinReceptacle;
import org.lsmr.vending.hardware.CoinReceptacleListener;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.PushButton;

/*
import ca.ucalgary.seng300.a2.CoinRackListenerDevice;
import ca.ucalgary.seng300.a2.CoinReceptacleListenerDevice;
import ca.ucalgary.seng300.a2.EventLog;
import ca.ucalgary.seng300.a2.EventLogInterface;
import ca.ucalgary.seng300.a2.VendingLogic;
import ca.ucalgary.seng300.a2.VendingLogicInterface;
*/

//Stub for testing components that use the event logger
class EventStub implements EventLogInterface {

	/**
	* Method does nothing. This is to prevent writing a work log when testing
	*/
	@Override
	public void writeToLog(String s) {
	}

	/**
	* Method does nothing. This is to prevent writing a work log when testing
	*/
	@Override
	public void timeStamp() {
	}

}

//Stub for testing the 	Vending logic interface
class StubLogic implements VendingLogicInterface {
	
	public CoinReceptacleListenerDevice dev;
	public CoinReceptacle hardware;
	EventStub ev = new EventStub(); //How does this work not in a constructor or method?

	public boolean enabled = true;

	/**
	* Creates a logic stub with a coinreceptacle with num capacity
	* @param int num, the capacity of the coin receptacle
	*/
	public StubLogic(int num) {
		dev = new CoinReceptacleListenerDevice(this);
		hardware = new CoinReceptacle(num);
		hardware.register(dev);

	}

	/**
	* getter for event log. 
	* @return EventLotInterface ev
	*/
	@Override
	public EventLogInterface getEventLog() {
		return ev;
	}

	/** 
	* getter for currency value
	* @return always returns 0
	*/
	@Override
	public int getCurrencyValue() {

		return 0;
	}

	/** 
	* The following methods are implemented, but have no direct use for testing.
	* For this reason, they all havae no contents.
	*
	*/
	@Override
	public void welcomeMessageTimer() {

	}

	@Override
	public void welcomeMessage() {

	}

	@Override
	public void vendOutOfOrder() {

	}

	@Override
	public void displayCredit() {

	}

	@Override
	public void displayPrice(int index) {

	}

	@Override
	public void invalidCoinInserted() {

	}

	@Override
	public void validCoinInserted(Coin coin) {

	}

	@Override
	public void dispensingMessage() {

	}

	@Override
	public void returnChange() {

	}

	@Override
	public void determineButtonAction(PushButton button) {

	}

	
	/**
	* Method overide for returning the harware index. Always returns 0
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the hardware to be found
	* @return returns 0. 
	*/
	@Override
	public int findHardwareIndex(AbstractHardware<? extends AbstractHardwareListener> hardware) {

		return 0;
	}

	/**
	* Method disables a specific peice of hardware
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the piece of hardware to disable
	*/
	@Override
	public void disableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		enabled = false;
	}

	/**
	* Method enables a specific peice of hardware
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the piece of hardware to enable
	*/
	@Override
	public void enableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		enabled = true;

	}

}

/**
 * 38% code coverage right now
 *
 */
public class TestCoinReceptacleListenerDevice {

	/**
	* Tests to that logic is enabled when asked to be
	*/
	@Test
	public void isEnabled() {
		StubLogic logic = new StubLogic(1);
		logic.hardware.enable();
		assertTrue(logic.enabled);

	}
	/**
	* Tests to that logic is disabled when asked to be
	*/
	@Test
	public void isDisabled() {
		StubLogic logic = new StubLogic(1);
		logic.hardware.disable();
		assertFalse(logic.enabled);

	}

	/**
	* Tests to that the logic handles valid coins
	* @throws CapacityExceededException, DisabledException
	*/
	@Test
	public void isAccept() throws CapacityExceededException, DisabledException {
		StubLogic logic = new StubLogic(2);
		logic.hardware.acceptCoin(new Coin(5));

		assertEquals(1, logic.dev.coinCount);
		assertEquals(5, logic.dev.coinValue);

	}

	@Test(expected = CapacityExceededException.class)
	public void isExceeded() throws CapacityExceededException, DisabledException {
		StubLogic logic = new StubLogic(1);
		logic.hardware.acceptCoin(new Coin(5));
		logic.hardware.acceptCoin(new Coin(5));

	}

	@Test(expected = DisabledException.class)
	public void isExceptions() throws CapacityExceededException, DisabledException {
		StubLogic logic = new StubLogic(1);
		logic.hardware.disable();
		logic.hardware.acceptCoin(new Coin(5));

	}

}
