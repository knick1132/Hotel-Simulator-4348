import java.util.*;

public class bellhop implements Runnable{
	
	
	public int num = 0;
	private int KnowName = 0;
	private int holdRoom = 0;
	private int tip;

	public bellhop(int x) {
		num = x;
		System.out.println("Bellhop " + num + " created");
	}
	
	
	public void receiveBags() throws InterruptedException {
		
		if(num == 0) {
			Project2.GuestGiveBags0.acquire();// ask for guest info
			holdRoom = Project2.guest[2].room;//pull info
			KnowName = Project2.guest[2].getID();
			System.out.println("Bellhop 0 receives bags from guest " + KnowName);
			Project2.bellhop0ReciveBags.release();//signal info is gotten
			Project2.bellhop0Bring.acquire();//wait for guest to enter room
			System.out.println("Bellhop 0 delivers bags to guest " + KnowName);
			Project2.bellhop0Room.release();//gives bags
			Project2.bellhop0Tip.acquire(); //request tip
			tip++;

		}else { // same as above ^^
			Project2.GuestGiveBags1.acquire();
			holdRoom = Project2.guest[3].room;
			KnowName = Project2.guest[3].getID();
			System.out.println("Bellhop 1 receives bags from guest " + KnowName);
			Project2.bellhop1ReciveBags.release();
			Project2.bellhop1Bring.acquire();
			System.out.println("Bellhop 1 delivers bags to guest " + KnowName);
			Project2.bellhop1Room.release();
			Project2.bellhop1Tip.acquire();
			tip++;
			
		}
	}
	
	public void run() {
		try {
			
			while(true) {
				
				Project2.bellhopline.release();
				receiveBags();//tend to customers
			} 
		}catch (Exception e) {}	
	}
}
