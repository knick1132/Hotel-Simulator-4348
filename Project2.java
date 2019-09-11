import java.util.*;
import java.util.concurrent.Semaphore;

public class Project2{
	
	 
	 static int room = 0;
	 static int joinID = 0;
	 static int numberOfGuest = 25;	
	
	 static Semaphore line = new Semaphore(0, true); //customer enter the building at the same time in a line
	 static Semaphore key0 = new Semaphore(0, true); //key handing off for front0 
	 static Semaphore key1 = new Semaphore(0, true); // key handing off for front0
	 static Semaphore keyConfirm0 = new Semaphore(0, true); //key confirms it reached customer from front0
	 static Semaphore keyConfirm1 = new Semaphore(0, true); //key confirms it reached customer from front1
	 static Semaphore tellName0 = new Semaphore(0, true); //customer signals to front 0 their name
	 static Semaphore tellName1 = new Semaphore(0, true); //customer signals to front 1 their name
	 static Semaphore givekeys = new Semaphore(0, true);  //customer gets keys 
	 
	 static Semaphore bellhopline = new Semaphore(0, true); //for the line when waiting for bell hop
	 static Semaphore GuestGiveBags0 = new Semaphore(0, true); //Guest give bellhop0 bags
	 static Semaphore GuestGiveBags1 = new Semaphore(0, true); //Guest give bellhop1 bags
	 static Semaphore bellhop0ReciveBags = new Semaphore(0, true); //bellhop0 receives bags
	 static Semaphore bellhop1ReciveBags = new Semaphore(0, true); //bellhop1 receives bags
	 static Semaphore bellhop0Bring = new Semaphore(0, true); //bellhop0 brings bags 
	 static Semaphore bellhop1Bring = new Semaphore(0, true); //bellhop1 brings bags
	 static Semaphore bellhop0Room = new Semaphore(0, true); //bellhop0 brings bags to room after guest signal
	 static Semaphore bellhop1Room= new Semaphore(0, true); //bellhop1 brings bags to room after guest signal
	 static Semaphore bellhop0Tip = new Semaphore(0, true); //bellhop0 signal for customer tip
	 static Semaphore bellhop1Tip = new Semaphore(0, true);//bellhop1 signal for customer tip
	 
	 static Semaphore join = new Semaphore(0, true); //signal join function
	 static Semaphore joinIDTransfer = new Semaphore(0, true); //signal fetch guestID to join the thread
	 static Semaphore joinIDTransferConfirmed = new Semaphore(0, true);//signal confirmation of transfer
	 
	 static Thread makeFront[] = new Thread[2]; //front employee threads
	 static Thread makeBellhop[] = new Thread[2]; //bellhop threads
	 static Thread makeGuest[] = new Thread[numberOfGuest]; //guest threads
	 static guest guest[] = new guest[4]; //global for transfering infromation between guest and employees

	 public static void main(String[] args){
		 
		
		System.out.println("Simulation starts");

		
		for(int i = 0; i < 2; i++) {//creating of front employee threads
			makeFront[i] = new Thread(new front_Desk_Employee(i));
			makeFront[i].start();
		}
		
		
		for(int i = 0; i < 2; i++) {//creation of bellhop employee threads
			makeBellhop[i] = new Thread(new bellhop(i));
			makeBellhop[i].start();
		}
		
		for(int i = 0; i < numberOfGuest; i++) {//creation of guest threads
			makeGuest[i] = new Thread(new guest(i));
			makeGuest[i].start();	
		}
		/*
		//THIS IS PURLY FOR FORMAT REASONS AND NOTHING MORE 
		try {
			Thread.sleep(500);
			line.release(numberOfGuest);
			Thread.sleep(500);
			line.release(numberOfGuest);
			line.release(numberOfGuest);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		*/
		int count = 0;
		while(true) {//checks for threads to join, once they all join it exits
			try {
				joinIDTransfer.acquire();
				joinIDTransferConfirmed.release();
				join.acquire();
				makeGuest[joinID].join();
				System.out.println("Guest " + joinID + " joined");
				count++;
				if(count >= numberOfGuest) {
					System.out.println("Simulation ends");
					System.exit(0);
				}
			} catch (Exception e) {}
		}	
	}
	 
	 
	 public static void joinThread(int GuestID) throws InterruptedException {//function of joining threads
		 makeGuest[GuestID].join();
		 System.out.println("Guest " + GuestID + " joined");
	 }
}







