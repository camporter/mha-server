import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

/* Type of message sent by nodes to server (temporary)
 * 
 * id# \n request \n userId'
 * Example: 8787\nUSER\n2
 * id# is the unique identifier for the particular node (use IP)
 * 	-Some sort of identification for node, s
 * request is the type of request the node is making
 * 		- "INIT" Initialization with the server
 * 		- "PLAY" Request data, user entered room
 * 		- "SWITCH" Request new data, new user entered room
 * 		- "HALT" Stop the streaming of data, user left room
 * 				TODO-Keep queue of users in room, if user of high priority leaves, then low priority takes over
 * 		- "DISCONNECT" Node leaves, thread allocated to it by the server is removed
 * userId is optional, used for USER and SWITCH, indicates the user who entered the room
 * 		Server checks the preferences to find the necessary data to send to node.
 * 		For INIT, HALT, and DISCONNT use userId = 0
 * 
 */

//TODO Remove excess System.out.println statements 
//TODO Stream audio

public class Server {
	protected static int port = 9090;

	static int numClients = 0;
	/**
	 * @param args
	 *            No parameters
	 */
	public static void main(String[] args) {
		// TODO Client Node must send server INIT message, server will then create thread to handle requests
		// InitializeSetup move to new class?

		ServerSocket listenSocket = null;
		try {
			listenSocket = new ServerSocket(port);
		} catch (IOException e)
		{
			System.out.println("Unable to bind to port: "+port);
			e.printStackTrace();
		}
		
		try {
			while (true) {
				System.out.println("Listening");

				Socket clientSocket = listenSocket.accept();
				System.out.println("Connection Found");
				numClients++;

				NodeRequest request = new NodeRequest(clientSocket, numClients+100);

				Thread thread = new Thread(request);
				System.out.println("Starting New Thread For Request");
				thread.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

/* NodeRequest Class
 * -Uses TCP connection to communicate with nodes
 * -Receiving action requests from the nodes
 * 
 * @author Ryan Brown
 * 
 */
//final class NodeRequest implements Runnable {
class NodeRequest extends Thread implements ActionListener{
	//Network Variables
	Socket TCPsocket = null;
	DatagramSocket UDPsocket = null;
	DatagramPacket UDPpacket = null;
	int port = 0;
	InetAddress addr = null;
	final static int serverId = 10;
	int nodeId = -1;
	int userId = -1;
	
	
	//File variables
	String musicName = "01 Fortune Faded.wav";
	File musicFile = new File(musicName);
	boolean d = musicFile.canRead();
	int audioNum = 0;
	int audioLen;
	
	//Transmitting or Receiving variables
	int seqNumber = 0;
	InputStream is = null;
	OutputStream os = null;
	ByteArrayInputStream bais = null;
	ByteArrayOutputStream baos = null;
	BufferedReader br = null;
	BufferedWriter bw = null;
	AudioStream audio = null;
	
	//Request variables
	final static int INIT = 0;
	final static int PLAY = 1;
	final static int SWITCH =2;
	final static int HALT = 3;
	final static int DISCONNECT = 4;
	final static int RECEIVED = 5;
	
	//State variables
	final static int INITIALIZING = 0;
	final static int WAITING = 1;
	final static int STREAMING = 2;
	static int state = -1;
	
	Timer timer;
	byte[] buf;
	static int frameDelay = 100;
	
	
	/* Constructor
	 * @param socket 		server side socket connected to client
	 * @return
	 */
	public NodeRequest(Socket socket, int numClients) {
		this.TCPsocket = socket;
		this.port = socket.getPort();
		this.addr = socket.getInetAddress();
		this.nodeId = numClients;
	
	    timer = new Timer(frameDelay, this);
	    this.timer.setInitialDelay(0);
	    this.timer.setCoalesce(true);

	    //allocate memory for the sending buffer
	    this.buf = new byte[15000]; 
	}

	private int parseRequest() {
		try {
			if(!br.ready()){
				//System.out.println("BufferedReader Not Ready");
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		System.out.println("Server - Client Msg Received");
		String requestState = null;
		int senderId = 0;
		int userId = 0;
		int request = -1;
		try {
			String temp = br.readLine();
			//System.out.println(temp);
			senderId = Integer.parseInt(temp);
			//System.out.println("NodeId: "+nodeId);
			requestState = br.readLine();
			//System.out.println("RequestState: "+requestState);
			temp = br.readLine();
			//System.out.println(temp);
			userId = Integer.parseInt(temp);
			System.out.println("Server - Client Msg: "+ senderId + " " + requestState + " " + userId);
			
			if(requestState.equals("INIT")){
				request = INIT;
			}
			else if(requestState.equals("PLAY")){
				request = PLAY;
			}
			else if(requestState.equals("SWITCH")){
				request = SWITCH;
			}
			else if(requestState.equals("HALT")){
				request = HALT;
			}
			else if(requestState.equals("DISCONNECT")){
				request = DISCONNECT;
			}
			else if(requestState.equals("RECEIVED")){
				request = RECEIVED;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return request;
	}
	
	private void sendResponse(String request){
		try{
			/*The server sends two kinds of messages
			 * INIT of the form - serverId \n request \n nodeId \n
			 * ServerId - Ident of server, normally 0
			 * Request - Type of request of server to client
			 * 	- RECEIVED - returns after successful client request msg
			 * 	- INIT - returns if client sends INIT, so client knows to set nodeId for future transmissions
			 * nodeId - Always includes nodeId the server is talking to
			 */
			String msg = serverId + "\n" + request + "\n" + nodeId + "\n"; 
			System.out.println(serverId + " " + request + " " + nodeId + " ");
			bw.write(msg);
			bw.flush();
			System.out.println("Server - Sent response to Client.");
		}
	    catch(Exception ex){
	    	System.out.println("Exception caught: "+ex);
	    	System.exit(0);
	    }
	}
	
	/* UDP datagram socket for streaming audio
	 * @param
	 * 
	 * @return
	 */
	private void setupUDPsocket(){
		try {
			UDPsocket = new DatagramSocket(port, addr);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void closeUDPsocket() throws SocketException{
		UDPsocket.close();
	}
	
	@SuppressWarnings("unused")
	private void closeTCPsocket(){
		try {
			TCPsocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void run() {
		//Initialize server state
		state = INITIALIZING;
		try {
			is = TCPsocket.getInputStream();
			os = TCPsocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		br = new BufferedReader(new InputStreamReader(is));
		bw = new BufferedWriter(new OutputStreamWriter(os));

		
		//Get initial request from client, should be INIT request
		int requestType = -1;
		boolean setup = false;
		while(!setup){
			requestType = parseRequest();
			if(requestType == INIT){
				setup = true;
				state = WAITING;
				System.out.println(d);
				setupUDPsocket();
				sendResponse("INIT");
			}
		}
		while(true){
			requestType = parseRequest();
			if(requestType == INIT){
				//Node requesting another initialization?
			}
			else if(requestType == PLAY && state == WAITING){
				//Node wants audio stream and server ready to stream
				sendResponse("RECEIVED");
				audio = new AudioStream(musicFile);
				audioLen = (int)audio.getNumFrames();
				System.out.println("Streaming Audio: "+ musicName);
				timer.start();
				
			}
			else if(requestType == SWITCH && state == STREAMING){
				//Node needs new audio stream and server currently streaming
			}
			else if(requestType == HALT && state == STREAMING){
				//Node requests streaming halt
			}
			else if(requestType == DISCONNECT){
				//Node wants to disconnect
			}
		}	
	}

	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Trying to Stream: "+ audioNum + " " + audioLen);
	    if (audioNum < audioLen){
			audioNum++;
			try {
				//get next frame to send
				int audioSize = audio.getnextframe(buf);
		
				//Builds an Packet
				Packet packet = new Packet(audioNum, buf, audioSize);
				 
				//get to total length of the packet
				int packetLen = packet.getlength();
				
				//get packet stream
				byte[] packet_bits = new byte[packetLen];
				packet.getpacket(packet_bits);
		
				//send the packet over the UDP socket
				UDPpacket = new DatagramPacket(packet_bits, packetLen, addr, port);
				UDPsocket.send(UDPpacket);
		
				System.out.println("Send frame #"+audioNum);
			}catch(Exception ex){
				System.out.println("Exception caught: "+ex);
				System.exit(0);
			}
	    }else{
	    	//if we have reached the end of the audio file, stop the timer
	    	timer.stop();
	    }
	}
}


class AudioStream {
	File fileName;
	AudioInputStream ais;
	int frameNum; //current frame nb
	
	private AudioFormat format;
	private long numFrames;
	

	//constructor
	public AudioStream(File file){

	    fileName = file;
	    frameNum = 0;
	    try {
			ais = AudioSystem.getAudioInputStream(fileName);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ais.getFormat();
		ais.getFrameLength();

	}

	public int getnextframe(byte[] frame) throws IOException{
		int len = 0;
		String len1;
		byte[] frameLen = new byte[5];

		ais.read(frameLen,0,5);
		
		len1 = new String(frameLen);
		len = Integer.parseInt(len1);
			
		return(ais.read(frame,0,len));
	}

	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	public AudioFormat getFormat() {
		return format;
	}

	public void setNumFrames(long numFrames) {
		this.numFrames = numFrames;
	}

	public long getNumFrames() {
		return numFrames;
	}
}

