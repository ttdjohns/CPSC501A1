package ca.ucalgary.seng300.a1;
import java.util.TimerTask;

import org.lsmr.vending.hardware.VendingMachine;

public class MyTimer extends TimerTask{
	
	private MessageDriver msgDriver;	
	
	/**
	* Constructor uses the vending machine vend to get the display to interact with
	* @param VendingMachine vend, the vendingmachine that the timer works with
	*/
	public MyTimer(MessageDriver md){
		this.msgDriver = md;
	}
	
	/**
	* calls the methods to display or clear the welcome message.  done on a timer
	*/
	@Override
	public void run() {
		if (msgDriver.displayWelcome) {
			msgDriver.welcomeMessage();
		}
		else 
			msgDriver.clearDisplayMessage(); 
	}

}
