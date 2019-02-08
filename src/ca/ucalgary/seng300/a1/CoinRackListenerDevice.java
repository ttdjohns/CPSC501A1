package ca.ucalgary.seng300.a1;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;



public class CoinRackListenerDevice implements CoinRackListener{

	private VendingLogicInterface logic;
	//TODO These should not be public variables. We should use getters for these.
	public int enabledCount = 0;
	public int disabledCount = 0;
	public int coinValue = 0;
	public int coinCount = 0;
	public boolean racksFull = false;
	public boolean racksEmpty = false;
	
	
	/**
	* Constructor creates the listener and assigns a logic to it
	* @param VendingLogicInterface Logic that the listener interacts with
	* 
	*/
	public CoinRackListenerDevice (VendingLogicInterface logic)
	{
		this.logic = logic;
	}
	
	
	/**
	* Method disables a specific peice of hardware
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
    	 * Announces that the indicated coin rack is full of coins.
    	 * 
   	 * @param rack,  The rack where the event occurred.
    	 *         
    	 */
	@Override
	public void coinsFull(CoinRack rack) {
		if(rack.getCapacity()<=rack.size()) {
			racksFull = true;
			int myRack = logic.findHardwareIndex(rack);
			logic.getEventLog().writeToLog("Coin rack #" + myRack + " is full.");
		}
		else racksFull = false;
	}

	/**
  	* Announces that the indicated coin rack is empty of coins.
   	* 
     	* @param rack
	*           The rack where the event occurred.
     	*/
	@Override
	public void coinsEmpty(CoinRack rack) {
		if(rack.size() == 0) {
			racksEmpty = true;
			int myRack = logic.findHardwareIndex(rack);
			logic.getEventLog().writeToLog("Coin rack #" + myRack + " is empty.");
		}
		else racksEmpty = false;
		
	}

	 /**
     	* Announces that the indicated coin has been added to the indicated coin
     	* rack.
     	* 
     	* @param rack
     	*            The rack where the event occurred.
     	* @param coin
     	*            The coin that was added.
     	*/
	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		coinValue+=coin.getValue();
		coinCount++;
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Coin rack #" + myRack + " was added with " + coinValue + "cents.");
	}

	/**
     	* Announces that the indicated coin has been added to the indicated coin
     	* rack.
     	* 
     	* @param rack
     	*            The rack where the event occurred.
    	 * @param coin
    	 *            The coin that was removed.
     */
	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		coinValue-=coin.getValue();
		coinCount--;
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Coin rack #" + myRack + " was removed with " + coinValue + "cents.");
	}

	/**
    	* Announces that the indicated sequence of coins has been added to the
     	* indicated coin rack. Used to simulate direct, physical loading of the
     	* rack.
     	* 
     	* @param rack
     	*            The rack where the event occurred.
    	 * @param coins
    	 *            The coins that were loaded.
     	*/
	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {
		for(Coin coin : coins) {
			 coinAdded(rack, coin);
		}
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Coin rack #" + myRack + " was loaded with " + coinCount + "coins.");
		logic.getEventLog().writeToLog("Total loaded value is "+ coinValue);
	}

	/**
    	* Announces that the indicated sequence of coins has been removed to the
    	* indicated coin rack. Used to simulate direct, physical unloading of the
     	* rack.
     	* 
    	* @param rack
    	*            The rack where the event occurred.
     	* @param coins
     	*            The coins that were unloaded.
     	*/
	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {
		for(Coin coin : coins) {
			coinRemoved(rack, coin);
		}
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Coin rack #" + myRack + " was unloaded with " + coinCount + "coins.");
		logic.getEventLog().writeToLog("Total unloaded value is "+ coinValue);
	}

	
	
	
}
