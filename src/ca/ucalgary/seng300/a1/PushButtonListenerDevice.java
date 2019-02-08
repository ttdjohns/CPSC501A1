package ca.ucalgary.seng300.a1;

import org.lsmr.vending.hardware.PushButtonListener;
import org.lsmr.vending.hardware.VendingMachine;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.PushButton;

public class PushButtonListenerDevice implements PushButtonListener {

	private VendingLogicInterface logic;
	
	/**
	* Constructor creates the listener and assigns a logic to it
	* @param VendingLogicInterface Logic that the listener interacts with
	* 
	*/
	public PushButtonListenerDevice(VendingLogicInterface logic) {
		this.logic = logic;
	}

	
	/**
	* Method informs the logic interface that a specific button was pressed
	* @param PushButton button, the button that was pushed
	*/
	@Override
	public void pressed(PushButton button) {
		logic.determineButtonAction(button);
	}

	/**
	* Method enables a specific peice of hardware
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the piece of hardware to enable
	*/
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		logic.enableHardware(hardware);
		
	}

	/**
	* Method disables a specific peice of hardware
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the piece of hardware to disable
	*/
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		logic.disableHardware(hardware);
		
	}
}
