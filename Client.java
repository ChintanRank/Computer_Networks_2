import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	
	
	static ArrayList<Integer> l=new ArrayList<Integer>();
	
	public static int[] test() {
		int rem = Client.l.size()+1;
		int[] remaining=new int[50-l.size()];
		int x=0;
		for( int i=rem;i<51;i++) {
			remaining[x]=i;
			x++;
		}
		return remaining;
	} 
	

	public void UDP(DatagramSocket datagramSocket){
		Thread t2 = new Thread(new Runnable() {
	
			 byte[] buffer=new byte[122];
		     DatagramPacket datagramPacket=null;
	  
	        public void run() { 	 
	        	try {
	        		datagramSocket.setReceiveBufferSize(1);                
	                long st = System.currentTimeMillis();
	                while(true) {
	               	 if((System.currentTimeMillis()-st)>4) {
	        	         		break;
	        	         	}
	               	datagramPacket=new DatagramPacket(buffer,buffer.length);
	                  datagramSocket.receive(datagramPacket);
	                  String got = new String(datagramPacket.getData(), 0, datagramPacket.getLength());                	
	                	System.out.println(got);         	
	                	String s=got.toString().replaceAll("\\D+", "");
	                	l.add(Integer.parseInt(s));
	                }
	            	}catch(Exception e) {System.out.println(e);}
	        }
	    });
		
		t2.start();
	}
	
	
	public static void main(String args[]){	
		String hostname=args[0];
		int portnumber=Integer.parseInt(args[1]);
		int portnumber2=Integer.parseInt(args[2]);
		
		try {
		Socket socket= new Socket(hostname,portnumber);	
		DatagramSocket datagramSocket=new DatagramSocket(portnumber2);  	
		PrintWriter out =
	             new PrintWriter(socket.getOutputStream(), true);	 
		 out.println(portnumber2);
		 
	         BufferedReader in =  new BufferedReader(
	                 new InputStreamReader(socket.getInputStream()));
 	         String s;
	         int counter=1;
	         while((s=in.readLine())!=null) {
	        	 System.out.println(s);
	        	 if(counter==50) {break;}
	        	counter++;
	         }
	         Client c=new Client();
	         c.UDP(datagramSocket);
	         Thread.sleep(200);
	         int remaining[]= Client.test();
	         ObjectOutputStream outputObject=new ObjectOutputStream(socket.getOutputStream());
	        outputObject.writeObject(remaining);
	        outputObject.flush();
	        System.out.println("Lost  "+ remaining.length+" packets through UDP");
	        String rl;
	         while((rl=in.readLine())!=null) {
	        	 System.out.println(rl);
	         }
	        	        
	         System.out.println("Successful Delivery!!");        
	}
		
		catch (UnknownHostException e) {
	        System.err.println("Unkwown host " + hostname);
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to" + hostname);
	        System.exit(1);
	    } catch(Exception e) {
	    	System.err.println("Error while running the UDP Thread" + e);
	    }
	}
}










