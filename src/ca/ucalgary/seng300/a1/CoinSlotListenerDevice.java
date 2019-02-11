package ca.ucalgary.seng300.a1;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;



public class CoinSlotListenerDevice implements CoinSlotListener {

	private VendingLogic logic;
	
	//TODO make attributes private and create getters
	public int enabledCount = 0;
	public int disabledCount = 0;
	public int validCoinInsertedCount = 0;
	public int coinRejectedCount = 0;
	public int insertedCoinValue = 0;
	public int rejectedCoinValue = 0;
	

	/**
	* Constructor creates the listener and assigns a logic to it
	* @param VendingLogicInterface Logic that the listener interacts with
	* 
	*/
	public CoinSlotListenerDevice(VendingLogic logic)
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
	* Informs logic interface that a valid coin was inserted into a specific slot
	* @param Coin slot, Coin coin, wher slot is the CoinSlot where the coin was inserted
	* @return None
	*/
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
	    validCoinInsertedCount++;
	    insertedCoinValue += coin.getValue();
	    logic.validCoinInserted(coin);
	}

	/**
	* Informs logic interface that a invalid coin was inserted into a specific slot
	* @param Coin slot, Coin coin, wher slot is the CoinSlot where the coin was inserted
	* @return None
	*/
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
	    coinRejectedCount++;
	    rejectedCoinValue += coin.getValue();
	    logic.invalidCoinInserted();
	}

    

}
