import java.util.*;
import java.util.concurrent.Semaphore;

public class front_Desk_Employee implements Runnable{
	
	private int num = 0;
	private int KnowName = 0;
	private int holdRoom;

	public front_Desk_Employee(int x) {
		num = x;
		System.out.println("Front desk employee " + num + " created");
	}
	
	public void checkIn(int guest, int room) {
		System.out.println("Front desk employee " + num + " registers guest " + guest + " and assigns room " + room);
		KnowName = guest;
		holdRoom = room;
	}
	
	public void signGuest() {
		try {
			if(num == 0) {//slot 0
				Project2.tellName0.acquire(); //request for guest info
				checkIn(Project2.guest[0].getID(), ++Project2.room); //check guest in
				Project2.guest[0].room = holdRoom; //give guest room
				Project2.key0.release();	//signal guest key
				Project2.keyConfirm0.acquire(); //request if guest is done
				
			}else {//slot 1, same as o
				Project2.tellName1.acquire();
				checkIn(Project2.guest[1].getID(), ++Project2.room);
				Project2.guest[1].room = holdRoom;
				Project2.key1.release();
				Project2.keyConfirm1.acquire();
			}
			
			
		}catch(Exception e) {}
	}
	
	public void run() {
		try {

			
			while(true) {
				Project2.line.release();
				signGuest();
			} 
		
		}catch (Exception e) {}
	}
}
