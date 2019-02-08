package ca.ucalgary.seng300.a1;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CoinReturn;
import org.lsmr.vending.hardware.CoinReturnListener;

public class CoinReturnListenerDevice implements CoinReturnListener {

	private VendingLogicInterface logic;
	//TODO Make private and create get methods
	public int enabledCount = 0;
	public int disabledCount = 0;
	public int deliveredCoinCount = 0;
	public int deliveredCoinValue = 0;
	public boolean returnsfull = false;
	
	
	/**
	* Constructor creates the listener and assigns a logic to it
	* @param VendingLogicInterface Logic that the listener interacts with
	* 
	*/
	public CoinReturnListenerDevice(VendingLogicInterface logic)
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
     	* Indicates that the coin return will not be able to hold any more
     	* coins.
     	* 
     	* @param coinReturn
     	*            The device on which the event occurred.
     	*/
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins) {
		for(Coin coin:coins) {
			deliveredCoinCount++;
			deliveredCoinValue+=coin.getValue();
		}
		logic.getEventLog().writeToLog("CoinReturn delivered " + deliveredCoinValue + "cents");
		
	}
	
	/**
     	* Indicates that the coin return will not be able to hold any more
     	* coins.
     	* 
     	* @param coinReturn
     	*            The device on which the event occurred.
     	*/
	@Override
	public void returnIsFull(CoinReturn coinReturn) {
		if(coinReturn.getCapacity()<=coinReturn.size()) {
			returnsfull = true;
			logic.getEventLog().writeToLog("CoinReturn is full.");
		}
		else returnsfull = false;
	}

}
