import java.util.*;

public class guest implements Runnable{
		
		private int bags = 0;
		private int GuestID = 0;
		public int room;
		public boolean key = false;

		public guest(int x) { //guest constructors
			GuestID = x;
			System.out.println("Guest " + x + " created");
			Random rand = new Random();
			bags = rand.nextInt(5) + 1;
		}
		
		public guest(int x, int y) {
			GuestID = x;
			bags = y;
		}
		
		public guest(int x, int y, int z) {
			GuestID = x;
			bags = y;
			room = z;
		}
		
		public int getID() { //returns guestID
			return GuestID;
		}
		
		public void enter() {//guest enters the building print
			if(bags == 1)
				System.out.println("Guest " + GuestID + " enters hotel with " + bags + " bag");
			else
				System.out.println("Guest " + GuestID + " enters hotel with " + bags + " bags");
		}
		
		public void request() {//prints request for help bags
			System.out.println("Guest " + GuestID + " requests help with bags" );
		}
		
		public void tellID() throws InterruptedException {//guest enters communicates with front employees
					
				if(GuestID % 2 == 1) {//front employee 0
					Project2.guest[1] = new guest(GuestID, bags); //save self so front can see
					Project2.tellName1.release(); //signal front it saved
					Project2.key1.acquire(); //request key
					key = true;
					this.room = Project2.guest[1].room; //set room, print
					System.out.println("Guest " + GuestID +  " receives room key for room " + room +  " From Front desk employee 1");
					Project2.keyConfirm1.release(); //confirm guest is done
				}else{//front employee 1, same as 0
					Project2.guest[0] = new guest(GuestID, bags);
					Project2.tellName0.release();
					Project2.key0.acquire();
					key = true;
					this.room = Project2.guest[0].room;
					System.out.println("Guest " + GuestID +  " receives room key for room " + room +  " From Front desk employee 0");
					Project2.keyConfirm0.release();		
			}
		}

		public void enterRoom() {//prints it entering the room
			System.out.println("Guest " + GuestID + " enters room " + room);
		}
		
		public void join() throws InterruptedException {//calls to Project2 to join
			Project2.joinIDTransfer.release();
			Project2.joinIDTransferConfirmed.acquire();
			Project2.joinID = GuestID;
			Project2.join.release();
		}
		
		
		public void requestHelp() {//print help request
			System.out.println("Guest " + GuestID + " requests help with bags");
		}
		
		public void giveBags() throws InterruptedException {
			
				if(GuestID % 2 == 0) {//bellhop0
					requestHelp();//print help
					Project2.guest[2] = new guest(GuestID, bags, room);//save info
					Project2.GuestGiveBags0.release();//let him know info can be pulled
					Project2.bellhop0ReciveBags.acquire(); //wait for bellhop to get bags
					enterRoom(); //enter room
					Project2.bellhop0Bring.release();//let bellhop0 know its in the room
					Project2.bellhop0Room.acquire(); //request for bags
					System.out.println("Guest " + GuestID + " receives bags from bellhop 0 and gives tip");
					Project2.bellhop0Tip.release();//give tip
					
				}else { //bellhop 1 == bellhop2
					requestHelp();
					Project2.guest[3] = new guest(GuestID, bags, room);
					Project2.GuestGiveBags1.release();
					Project2.bellhop1ReciveBags.acquire();
					enterRoom();
					Project2.bellhop1Bring.release();
					Project2.bellhop1Room.acquire();
					System.out.println("Guest " + GuestID + " receives bags from bellhop 1 and gives tip");
					Project2.bellhop1Tip.release();
				}
			
		}

		public void run() {
			
			try {
				//Project2.line.acquire();//create and then wait
				enter();// enter then wait
				Project2.line.acquire();
							
				//Project2.line.acquire();
				tellID(); //front senario
				
				if(bags <= 2) {
					enterRoom(); //bags 2 or less, enter room
				}else {
					Project2.bellhopline.acquire(); //if not, bellhop call
					giveBags(); // bellhop senario
				}
				System.out.println("Guest "  + GuestID + " retires for the evening");
				join();	//join
				
				
			} catch (Exception e) {}
		}
	}