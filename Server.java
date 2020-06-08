import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
		public static void main(String[] args) throws IOException {   
	        if (args.length != 1) {
	            System.err.println("Usage: java EchoServer <port number>");
	            System.exit(1);
	        }       

	        int portNumber = Integer.parseInt(args[0]);
	        try{
	            ServerSocket ss = new ServerSocket(portNumber);
	            DatagramSocket ds =new DatagramSocket();
		    while(true){
	              Socket ClientSocket = ss.accept();
	              ClientWorker cw = new ClientWorker(ClientSocket,ds);
		      Thread t=new Thread(cw);  
		      t.start();
		    }	        

	        } catch (IOException e) {
	            System.out.println("Exception caught while listening to port" + portNumber + " or listening for a connection");
	            System.out.println(e.getMessage());
	        }
	    }
	}

	class ClientWorker implements Runnable {
	  private Socket Client;
      private DatagramSocket ds;
      String port;

	  ClientWorker(Socket Client,DatagramSocket ds) {
	    this.Client = Client;
	    this.ds=ds;
	  }

	  public Socket getClient() {
		return Client;
	}

	public void setClient(Socket Client) {
		this.Client = Client;
	}

	public void run() {
		  this.initial.start();
	  }

	  Thread initial=new Thread(new Runnable() {
	  public void run(){
	    String message;
	    BufferedReader in = null;
	    PrintWriter out = null;
	   int i=1;
	    try{
	      in = new BufferedReader(new InputStreamReader(Client.getInputStream()));
	      out = new PrintWriter(Client.getOutputStream(), true);
	      while((port=in.readLine())!=null){
	      break;
	      }
	      second.start();   
	      System.out.println("UDP connection with port:  " +Integer.parseInt(port));
	        } catch (IOException e) {
	      System.out.println("in or out failed");
	      System.exit(-1);
	    }

	    while(true){
	      try{
	    	message = "This is message " + i;
	        out.println(message);
	        if(i==50) {break;}
  	        i++;
	         }catch (Exception e) {
	        System.out.println("Cannot send message through TCP");
	        System.exit(-1);
	       }
	    }
	  }
	  });

	  Thread second = new Thread(new Runnable() {
          public void run() {
        	  String message;
      	    int i=51;
      	    byte[] buffer=null;
      	    DatagramPacket dp=null;
      	  InetAddress inet=Client.getLocalAddress();

      	    while(true){
      	      try{
      	        message = "This is message " + i;
      	        buffer=message.getBytes();
      	         dp = new DatagramPacket(buffer,buffer.length,inet,Integer.parseInt(port));
                 ds.send(dp);
      	         if(i==100) { break;}
      	        i++;
      	       }catch (Exception e) {
      	        System.out.println("Exception when delivering through UDP" + e);
      	        System.exit(-1);
      	       }
      	    }
      	   third.start();
          }
      });

	  Thread third = new Thread(new Runnable() {
          public void run() {
        	  String message;
        	ObjectInputStream input=null;
        	BufferedReader in = null;
        	PrintWriter out = null;
        	int[] left=new int[100];
        	 try{     
        		 in = new BufferedReader(new InputStreamReader(Client.getInputStream()));
       	    	input=new ObjectInputStream(Client.getInputStream());      	    	
       	    	left=(int[])input.readObject();
       	    	System.out.println("There was some loss of "+left.length + " packets while sending through UDP" );
       	        } catch (Exception e) {
       	        System.out.println("Exception in sending packets again : " + e);
       	        System.exit(-1);
       	       }

        	 System.out.println("Sending the remaining " + left.length + " Packets" );
      	      try{
      	    	in = new BufferedReader(new InputStreamReader(Client.getInputStream()));
      	    	out = new PrintWriter(Client.getOutputStream(), true);
      	    	int initial=100-left.length+1;
      	    	for(int x=initial;x<=100;x++) {
      	    		message = "This is message " + x;
      		        out.println(message);
      	    	}
      	    	System.out.println("Successful Delivery!!");
      	       out.close();
      	         } catch (Exception e) {
      	        System.out.println("error while UDP delivery" + e);
      	        System.exit(-1);
      	       }
          }
      });
	}




