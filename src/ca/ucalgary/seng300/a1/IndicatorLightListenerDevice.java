package ca.ucalgary.seng300.a1;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class IndicatorLightListenerDevice implements IndicatorLightListener{

	//TODO make private and add get methods
	public int enabledCount = 0;
	public int disabledCount = 0;
	public boolean lightActivated = false;
	public boolean lightDeactivated = false;
	
	private VendingLogicInterface logic;
	
	/**
	* Constructor creates the listener and assigns a logic to it
	* @param VendingLogicInterface Logic that the listener interacts with
	* 
	*/
	public IndicatorLightListenerDevice(VendingLogicInterface logic)
	{
		this.logic = logic;
	}
	
	
	/**
	* Method enables a specific peice of hardware
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the piece of hardware to enable
	*/
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    enabledCount++;
	    logic.enableHardware(hardware);
	}

	
	/**
	* Method disables a specific peice of hardware
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the piece of hardware to disable
	*/
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    disabledCount++;
	    logic.disableHardware(hardware);
	}

	/**
	* Method activates the light, and logs action to file
	* @param IndicatorLight light, the light to be turned on
	* @return None
	*/
	@Override
	public void activated(IndicatorLight light) {
		lightActivated = true;
		logic.getEventLog().writeToLog("IndicatorLight was turned on.");
		
	}
	/**
	* Method deactivates the light, and logs action to file
	* @param IndicatorLight light, the light to be turned off
	* @return None
	*/
	@Override
	public void deactivated(IndicatorLight light) {
		lightDeactivated = true;
		logic.getEventLog().writeToLog("IndicatorLight was turned off.");
		
	}
	
}
