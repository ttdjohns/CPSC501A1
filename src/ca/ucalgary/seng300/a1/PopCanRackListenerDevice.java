package ca.ucalgary.seng300.a1;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.PopCanRack;
import org.lsmr.vending.hardware.PopCanRackListener;

public class PopCanRackListenerDevice implements PopCanRackListener {

	private VendingLogic logic;
	
	/**
	* Constructor creates the listener and assigns a logic to it
	* @param VendingLogicInterface Logic that the listener interacts with
	* 
	*/
	public PopCanRackListenerDevice(VendingLogic vl) {
		logic = vl;
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
	*
	* @param PopCanRack popCanRack, PopCan popCan, //TODO define action
	* @return
	*/
	@Override
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {
		// TODO Auto-generated method stub
		
	}

	/**
	*
	* @param PopCanRack popCanRack, PopCan popCan, //TODO define action
	* @return
	*/
	@Override
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		// TODO Auto-generated method stub
		
	}

	/**
	* Write to the work log the rack that is full
	* @param PopCanRack popCanRack, the popcan rack that is full
	* @return None
	*/
	@Override
	public void popCansFull(PopCanRack popCanRack) {
		int myRack = logic.findHardwareIndex(popCanRack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " is full.");
		
	}

	/**
	* Write to the work log the rack that is empty
	* @param PopCanRack popCanRack, the popcan rack that is empty
	* @return None
	*/
	@Override
	public void popCansEmpty(PopCanRack popCanRack) {
		int myRack = logic.findHardwareIndex(popCanRack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " is empty.");
		logic.disableHardware(popCanRack);
	}

	/**
	* Write to the work log the rack that is loaded
	* @param PopCanRack popCanRack, the popcan rack that is loaded
	* @return None
	*/
	@Override
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " was loaded with " + popCans.length + "cans.");
	}

	/**
	* Write to the work log the rack that is unloaded
	* @param PopCanRack popCanRack, the popcan rack that is unloaded
	* @return None
	*/
	@Override
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " had " + popCans.length + "pop cans unloaded.");
	}

}
