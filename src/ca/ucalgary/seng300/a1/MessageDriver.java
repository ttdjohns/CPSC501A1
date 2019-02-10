package ca.ucalgary.seng300.a1;

import java.util.TimerTask;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.VendingMachine;

public class MessageDriver {
	private boolean debug = false;
	public String currentMessage ="";
	public String prevMessage ="";
	public TimerTask futureTask;
	public boolean displayWelcome;
	private VendingMachine vm;
	
	public MessageDriver(VendingMachine vend) {
		vm = vend;
	}
	
	/**
	 * A method to push a welcome message to the display
	 */
	public void welcomeMessage() {
		vm.getDisplay().display("Hi There!");
		displayWelcome = false; 
		/*try {
			futureTask.wait(5000);				// 5 sec
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	/**
	 * A method to clear the message to the display
	 */
	public void clearDisplayMessage() {
		vm.getDisplay().display("");
		displayWelcome = true;
		/*try {
			futureTask.wait(10000);				// 10 sec
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	/**
	 * A method to send an OutOfOrder message to the display
	 */
	public void vendOutOfOrder() {
		futureTask.cancel();
		vm.getDisplay().display("Out Of Order");
	}
	
	/**
	 * Method to show that an invalid coin was inserted
	 * TODO is this an acceptible way to wait for 5 seconds?
	 */
	public void invalidCoinInserted(int currentCredit) {
		futureTask.cancel();
		prevMessage = "Invalid coin!";
		vm.getDisplay().display(prevMessage);
		try {
			if(!debug) Thread.sleep(5000);			// wait for 5 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (currentCredit == 0)
			welcomeMessage();
		else
			this.displayCredit(currentCredit);
	}
	
	/**
	 * Method called by the coinSlotListener to accumulate credit when valid coins are inserted.
	 * Update the credit and update the display.  Recalculate if the exact change is possible
	 * @param coin  The Coin that was inserted
	 */
	public void validCoinInserted(int currentCredit) {
		futureTask.cancel();
		
		this.displayCredit(currentCredit);
	}
	
	/**
	 * A method to push the currently accumulated credit to the display
	 */
	public void displayCredit(int credit) {
		futureTask.cancel();
		vm.getDisplay().display("Current Credit: $" + (((double) credit)/100));
	}
	
	/**
	 * A method to display the price of the pop at a specific index 
	 * @param index - the selection number that corresponds to the desired pop
	 */
	public void displayPrice(int index, int currentCredit) {
		futureTask.cancel();
		prevMessage = "Price of " + vm.getPopKindName(index) + ": $" + (((double) vm.getPopKindCost(index)) / 100);
		vm.getDisplay().display(prevMessage);
		try {
			if(!debug) Thread.sleep(5000);			// wait for 5 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (currentCredit == 0)
			welcomeMessage();
		else
			this.displayCredit(currentCredit);
	}
	
	
	/**
	 * Method to confirm that the product is being dispensed 
	 */
	public void dispensingMessage() {
		vm.getDisplay().display("Despensing. Enjoy!");
	}
	
	/**
	* this method returns the current contents of the display
	* @param none
	* @return String currentMessage
	*/
	public String getCurrentMessage(){
		return currentMessage;
	}
	
	/**
	* this method sets the contents of the display, called by displayListenerDevice
	* @param String x is the new message
	* @return void
	*/
	public void setCurrentMessage(String x){
		currentMessage = x;	
	}
}
