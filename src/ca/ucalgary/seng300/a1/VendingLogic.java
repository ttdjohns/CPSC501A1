package ca.ucalgary.seng300.a1;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;
import java.util.Timer;
import java.util.TimerTask;


public class VendingLogic {
	private VendingMachine vm;				// The vending machine that this logic program is installed on
	private int credit;					// credit is saved in terms of cents 
	private EventLogInterface EL;				// An even logger used to track vending machine interactions
	private Boolean[] circuitEnabled;			// an array used for custom configurations
	public MessageDriver msgDriver;
	
	public VendingLogic(VendingMachine vend)
	{
		//Set up attributes
		this.vm = vend;
		credit = 0;
		EL = new EventLog();
		
		//Set up the custom configuration
		circuitEnabled = new Boolean[vm.getNumberOfSelectionButtons()];
		for (int i = 0; i < circuitEnabled.length; i++) {
			circuitEnabled[i] =true; //we enable all by default
		}
		msgDriver = new MessageDriver(vm);
		msgDriver.futureTask = new MyTimer(msgDriver);
		msgDriver.welcomeMessage();
		
	}
	
	/**
	 * Method to show that an invalid coin was inserted
	 * TODO is this an acceptible way to wait for 5 seconds?
	 */
	public void invalidCoinInserted() {
		msgDriver.invalidCoinInserted(credit);
	}
	
	/**
	 * Method called by the coinSlotListener to accumulate credit when valid coins are inserted.
	 * Update the credit and update the display.  Recalculate if the exact change is possible
	 * @param coin  The Coin that was inserted
	 */
	public void validCoinInserted(Coin coin) {
		credit += coin.getValue();
		//Light the exact change light based on attempted change output
		if (!isExactChangePossible())
			vm.getExactChangeLight().activate();
		else 
			vm.getExactChangeLight().deactivate();
		
		msgDriver.validCoinInserted(credit);
	}
	/**
	*This constructor uses a vending machine as a parameter, then creates and assigns listeners to it.
	*
	*@param VendingMachine vend is the the machine that the listeners will be registered to.
	*@return a new instance of a VendingLogic object
	*
	*/
	
	/**
	* This method returns the event logger
	* @param None
	* @return EventLogInterface El
	*/
	public EventLogInterface getEventLog(){
		return EL;
	}
	
	/**
	* This method returns the the credit total that the vending machine has
	* @param None
	* @return Int credit
	*/
	public int getCurrencyValue(){
		return credit;
	}
	
	/**
	 * A method to return change to the user
	 */
	public void returnChange() {
		if (vm.getCoinReturn() != null) {
			int[] coinKinds = getVmCoinKinds(); //vm.getCoinKindForCoinRack(0);// {200, 100, 25, 10, 5};		// legal value of Canadian coins. only types returned
			for (int i = 0; i < coinKinds.length; i++) {
				CoinRack rack = vm.getCoinRackForCoinKind(coinKinds[i]);		// the coin rack for the coin value indicated by the loop
				if (rack != null) {									// if rack = null. coin kind is not a valid change option
					while ((!vm.isSafetyEnabled()) && (credit > coinKinds[i]) && (!rack.isDisabled()) && (rack.size() > 0)) {
						try {
							rack.releaseCoin();
							credit -= coinKinds[i];			// subtracting (i) cents from the credit
						} catch (CapacityExceededException e) {
							// should never happen, receptacle full should enable the safety, which is in the loop guard
							e.printStackTrace();
						} catch (EmptyException e) {
							// should never happen, checked for in the loop guard
							e.printStackTrace();
						} catch (DisabledException e) {
							// should never happen, checked for in the loop guard
							e.printStackTrace();
						}
					}
				}
			}
		}
		else
			vm.getDisplay().display("Unable to return any changed");
		
		if (!isExactChangePossible())
			vm.getExactChangeLight().activate();
		else 
			vm.getExactChangeLight().deactivate();
	}
	
	
	/**
	 * Method finds out what coin kinds are used in the vending machine based on the number of coin racks.
	 * This cannot be called while coinReturn is bugged
	 * @return int[] coinKinds, for example {5, 10, 25, 100, 200} for Canadian currency
	 */
	public int[] getVmCoinKinds()
	{
		//first we find how many coin kinds there are
		int coinTypes = 0;
		for(int i = 0; i <100; i++) {
			//when we catch an exception we have ran out of racks, and thus coin types
			try {
			vm.getCoinKindForCoinRack(i);
			
			}catch(Exception e)
			{
				break;
			}
			coinTypes++;
			
		}
		//We use coinTypes to build an array of each coin kind
		int[] coinKinds = new int[coinTypes];
		for(int i = 0; i<coinTypes; i++)
		{
			coinKinds[i] = vm.getCoinKindForCoinRack(i);
		}
		return coinKinds;
	}
	
	/**
	 * a Method to determine if exact change is possible given the prices of the pop and the current credit
	 * Checks if the credit - price can be created using the available coins is the racks
	 * checks for every pop price in the machine.
	 *   
	 * @return possible - A boolean describing if it is possible to create change for every possible transaction.
	 */
	public boolean isExactChangePossible() {
		boolean possible = true;
		if (vm.getCoinReturn() != null) {
			for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {		// get the price for every possible pop
				int credRemaining = credit;
				int price = vm.getPopKindCost(i);
				if (credRemaining >= price) {
					credRemaining -= price;
					int changePossible = 0;

					int[] coinKinds = {200, 100, 25, 10, 5};		// legal value of Canadian coins. only types returned
					for (int value = 0; value < coinKinds.length; value++) {
						CoinRack rack = vm.getCoinRackForCoinKind(coinKinds[value]);		// the coin rack for the coin value indicated by the loop
						if (rack != null) {									// if rack = null. coin kind is not a valid change option
							int coinsNeeded = 0;
							while ((!rack.isDisabled()) && (credRemaining > changePossible) && (rack.size() > coinsNeeded)) {
								coinsNeeded++;
								changePossible += coinKinds[value];			// sum of available coins
							}
						}
					}
					if (credRemaining != changePossible)		// if after going through all the coin racks, the exact change cannot be created
						possible = false;			//  return that it is not possible to 
				}
			}
		}
		else 
			possible = false;			// if the CoinReturn is not there (null) return false.
		
		return possible;
	}
	
	/** 
	 * A method to determine what action should be done when a button is pressed 
	 * TODO how is disabling a part going to affect what action is taken?
	 * @param button
	 */
	public void determineButtonAction(PushButton button) {
		boolean found = false;
		
		if(vm.isSafetyEnabled() == false) {
			// search through the selection buttons to see if the parameter button is a selection button
			for (int index = 0; (found == false) && (index < vm.getNumberOfSelectionButtons()); index++) {
				if (vm.getSelectionButton(index) == button) {
					selectionButtonAction(index);
					found = true;
				}
			}
		}
		
		// search through the configuration panel to see if the parameter button is part of these buttons
		// NOTE!!! the configuration panel has a hard coded list of 37 buttons.  If this changes it could cause an error here!
		for (int index = 0; (found == false) && (index < 37); index++) {
			if (vm.getConfigurationPanel().getButton(index) == button) {
				// TODO figure out how to configure
				found = true;
			}
		}
		
		// check to see if the button is the configuration panels enter button.
		if ((found == false) && (button == vm.getConfigurationPanel().getEnterButton())) {
			// TODO figure out how to configure
			found = true;
			
		}
		
		if (found == false) {
			throw new SimulationException("Unknown Button pressed! Could not determine action");
		}
			
	}

	/**
	 * Method to react to the press of a selection button
	 * @param index - the index of the selection button that was pressed
	 */
	public void selectionButtonAction(int index) {
		if ((vm.getPopKindCost(index) <= credit) && (circuitEnabled[index] == true)) {
			try {
				vm.getPopCanRack(index).dispensePopCan();
				msgDriver.dispensingMessage();
				credit -= vm.getPopKindCost(index);		// deduct the price of the pop
				returnChange();
				if (credit == 0)
					msgDriver.welcomeMessage();		// begin cycling the welcome message again
				else
					msgDriver.displayCredit(credit);
			} catch (DisabledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EmptyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CapacityExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (circuitEnabled[index] != true) {
			vm.getDisplay().display("Option unavailable");
		}
		else {
			msgDriver.displayPrice(index, credit);
			msgDriver.displayCredit(credit);
		}
	}
	
	/**
	 * A method to determine which pop can rack or push button an event has occurred on
	 * needed for EventLog information
	 * 
	 * @param hardware - the hardware that the event occurred on 
	 * @return The index of the hardware according to the vending machine. -1 means error could not find
	 */
	public int findHardwareIndex(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		int ret = -1;
		if (hardware instanceof PopCanRack) {
			
		}
		
		else if (hardware instanceof PushButton) {
			ret = findPushButtonIndex((PushButton) hardware);
		}
		
		else if (hardware instanceof CoinRack) {
			ret = findCoinRackIndex((CoinRack) hardware);
		}
		
		return ret; // -1 will be the error index
	}
	
	public int findCoinRackIndex(CoinRack rack) {
		int ret = -1;
		for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
			if (rack == vm.getCoinRack(i)) {
				ret = i;
			}
		}
		return ret;
	}
	
	public int findPushButtonIndex(PushButton button) {
		int ret = -1;
		for (int index = 0; index < vm.getNumberOfSelectionButtons(); index++) {
			if (vm.getSelectionButton(index) == button) {
				ret = index;
			}
		}
		
		for (int index = 0; index < 37; index++) {
			if (vm.getConfigurationPanel().getButton(index) == button) {
				ret = index;
			}
		}
		return ret;
	}
	
	public int findPopCanRackIndex(PopCanRack rack) {
		int ret = -1;
		for (int index = 0; index < vm.getNumberOfPopCanRacks(); index++) {
			if (vm.getPopCanRack(index) == rack) {
				ret = index;
			}
		}
		return ret;
	}
	
	/**
	 * Method to disable a piece of hardware. If hardware is a selection button or pop rack, machine can remain 
	 *   operational, otherwise, disable vending machine 
	 * @param hardware
	 */
	public void disableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (hardware instanceof PopCanRack) {
			circuitEnabled[findHardwareIndex(hardware)] = false;
		}
		else if (hardware instanceof PushButton) {
			for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
				if (hardware == vm.getSelectionButton(i)) {
					circuitEnabled[i] = false;
				}
			}
		}
		else {
			vm.getOutOfOrderLight().activate();
			
			returnChange();
			msgDriver.vendOutOfOrder();
			//vm.enableSafety(); NOTE: calling enableSafety() will result in a stack overflow exception
		}
	}
	
	/**
	 * Method to disable a piece of hardware. If hardware is a selection button or pop rack, machine can remain 
	 *   operational, otherwise, disable vending machine 
	 * @param hardware
	 */
	public void enableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (hardware instanceof PopCanRack) {
			int index = findHardwareIndex(hardware);
			if ((vm.getSelectionButton(index).isDisabled() == false) && (vm.isSafetyEnabled() == false))
				circuitEnabled[index] = true;
		}
		else if (hardware instanceof PushButton) {
			for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
				if (hardware == vm.getSelectionButton(i)) {
					circuitEnabled[i] = true;
				}
			}
		}
		else {
			vm.getOutOfOrderLight().deactivate();
			//vm.disableSafety(); NOTE: This may result in a stack overflow exception
			
		}
	}
	
	/**
	 * Method returns the value in the circuitEnabled array at an index
	 * @param int index, the index of the desired value
	 * @return boolean circuitEnabled[index]
	 */
	public boolean getCircuitEnabledIndex(int index)
	{
		if (index<0 || index >= circuitEnabled.length)
		{
			throw new IndexOutOfBoundsException();
		}
		else
			return circuitEnabled[index];
	}
}
