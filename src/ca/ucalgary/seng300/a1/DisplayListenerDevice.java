package ca.ucalgary.seng300.a1;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.Display;
import org.lsmr.vending.hardware.DisplayListener;

public class DisplayListenerDevice implements DisplayListener {
	
	private VendingLogic logic;
	
	/**
	* Constructor creates the listener and assigns a logic to it
	* @param LogicInterface Logic that the listener interacts with
	* 
	*/
	public DisplayListenerDevice(VendingLogic logic) {
		this.logic = logic;
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

	/**
	* Method logs a specific change in message that is being displayed
	* @param Display display, String oldMessage, String newMessage, Where newMessage is written to the event log
	* @return
	* !!TODO!! It appears that both display and oldMessage are not used?
	*/
	@Override
	public void messageChange(Display display, String oldMessage, String newMessage) {
		logic.getEventLog().writeToLog(newMessage);
		logic.setCurrentMessage(newMessage);
	}
}
