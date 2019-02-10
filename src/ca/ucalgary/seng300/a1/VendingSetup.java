package ca.ucalgary.seng300.a1;

import java.util.ArrayList;

import org.lsmr.vending.hardware.VendingMachine;
import ca.ucalgary.seng300.a1.*;

public class VendingSetup {
//THE VENDING MACHINE
	private VendingMachine myVM;

//PARAMETERS THAT WILL BE PASSED TO INSTANTIATE VENDING MACHINE
	private int [] validCoins = {5,10,25,100,200}; //Array initializer must be up here
	private int ButtonCount;
	private int coinRackCapacity;
	private int popCanRackCapacity;
	private int receptacleCapacity;
	private int deliveryChuteCapacity;
	private int coinReturnCapacity;
	private VendingLogic vendLogic;
	
	
	//pop names, pop costs, to configure vending machine
	private String [] pNames = {"Grape", "Root Beer", "Orange", "Cream Soda", "Coke", "Pepsi"};
	private Integer [] pCosts = {200, 250, 100, 150, 300, 300};
	public int [] capacEachRack = {10, 10, 10, 10, 10, 10};
	private ArrayList<String> popNames;
	private ArrayList<Integer> popCosts;
	
	/**
	* Constructor creates the object, and sets the following as default values:
	*		ButtonCount = 6;
	*		coinRackCapacity = 100;
	*		popCanRackCapacity = 10;
	*		receptacleCapacity = 200;
	*		deliveryChuteCapacity = 1;
	*		coinReturnCapacity = 50; 
	*
	*/
	public VendingSetup() {
			
	//arbitrary values right now
			ButtonCount = 6;
			coinRackCapacity = 100;
			popCanRackCapacity = 10;
			receptacleCapacity = 200;
			deliveryChuteCapacity = 1;
			coinReturnCapacity = 50; 
			
	//Popcan names
			popNames = new ArrayList<String>(ButtonCount);
	//convert to appropriate type for configuring VM
			for (int i = 0; i < pNames.length; i++) {
				popNames.add(pNames[i]);
			}		
			//Popcan costs
			popCosts = new ArrayList<Integer>(ButtonCount);
			//convert to appropriate type to configure VM
			for (int j = 0; j < pCosts.length; j++) {
				popCosts.add(pCosts[j]);	
			}
		//INSTANTIATE THE NEW VENDING MACHINE
		myVM = new VendingMachine(validCoins, ButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
		//Configure lists of pop names and popCosts 
		myVM.configure(popNames, popCosts);
		//load PopCans into the PopCanRacks up to their maximum capacity for the rack
		myVM.loadPopCans(capacEachRack);
		
		vendLogic = new VendingLogic(myVM);
		
		registerListeners(myVM);
	}
	
	/**
	* This method creates and registers listeners for the vending machine.
	* @param None
	* @return None
	*/
	private void registerListeners(VendingMachine vm)
	{
		//Register each of our listener objects here
		vm.getCoinSlot().register(new CoinSlotListenerDevice(vendLogic));
		vm.getDisplay().register(new DisplayListenerDevice(vendLogic));
		
		//For each coin rack create and register a listener
		for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
			vm.getCoinRack(i).register(new CoinRackListenerDevice(vendLogic));
		}
		vm.getCoinReceptacle().register(new CoinReceptacleListenerDevice(vendLogic));
		
		//!!The current version of the vending machine is bugged. The coin return is never instantiated.!!
		// This means we are unable to register to the coin return, as we get a null pointer.
		vm.getCoinReturn().register(new CoinReturnListenerDevice(vendLogic));
		
		//For each button create and register a listener
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
			vm.getSelectionButton(i).register(new PushButtonListenerDevice(vendLogic));
		}
		/*
		try {
		// Configuration Panel has 37 buttons.  This is a hard coded value.
		for (int i = 0; i < 37; i++) {
			vm.getConfigurationPanel().getButton(i).register(new PushButtonListenerDevice(this));
		}
		
		vm.getConfigurationPanel().getEnterButton().register(new PushButtonListenerDevice(this));
		}catch(Exception e)
		{
			if (debug)System.out.println("Invalid config setup");
		}
		*/
		//For each pop rack create and register a listener
		for (int i = 0; i < vm.getNumberOfPopCanRacks(); i++) {
			vm.getPopCanRack(i).register(new PopCanRackListenerDevice(vendLogic));
		}
		vm.getOutOfOrderLight().register(new IndicatorLightListenerDevice(vendLogic));
	}
	
	/**
	* getter for the setup vending machine
	* @return Vending vm, the vending machine created.
	*/
	public VendingMachine getVendingMachine() {
		return myVM;
	}		
	
	public VendingLogic getVendingLogic() {
		return vendLogic;
	}
	
	/**
	* getter for the valid coin array
	* @return int[] validCoins, the coin kinds used
	*/
	public int[] getCoinKinds()
	{
		return validCoins;
	}
}
