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
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.Timer;

//Client side, emulates nodes
//TODO create multiple clients automatically with threads

public class Client {
	protected static int port = 9090;
	// static int port;
	protected static String host = "localhost";
	static ClientConnect conn;
	static String msg;

	public Client() {
	}

	public static void main(String[] args) {
		// TODO Remove hardcoded client
		// Hardcoded client connect, send/receive, and disconnect
		conn = new ClientConnect(port, host);
		conn.start();
		// conn.closeConnection();
		// msg = "4567\nINIT\n0";
		// conn.send(msg);
	}
}

// Class for the client to connect to the server
/*
 * From main of Client
 * 
 * conn = new ClientConnect(port, host); conn.start(); msg = "4567\nINIT\n0";
 * conn.send(msg); //conn.receive(); //conn.closeConnection();
 */

class ClientConnect extends Thread {
	String msg;

	Timer timer;
	byte[] buf;

	// Request variables
	final static int INIT = 0;
	final static int PLAY = 1;
	final static int SWITCH = 2;
	final static int HALT = 3;
	final static int DISCONNECT = 4;
	final static int RECEIVED = 5;

	// State variables
	final static int INITIALIZING = 0;
	final static int WAITING = 1;
	final static int STREAMING = 2;
	static int state = -1;

	static int frameDelay = 100;

	// Network Variables
	Socket tcpSocket = null;
	DatagramSocket udpSocket = null;
	DatagramPacket recvPacket = null;
	DatagramPacket sendPacket = null;
	InetAddress udpAddrClient = null;
	InetAddress udpAddrServer = null;
	protected int udpPortServer = 0;
	protected int tcpPortServer = 0;

	protected String host;
	int nodeId = -1;
	int connSeqNum = 0;
	final static int serverId = 0;

	// File variables
	String musicName = "01 Fortune Faded.wav";
	File musicFile = new File(musicName);
	int audioNum = 0;
	int audioLen;

	// Transmitting or Receiving variables
	InputStream is = null;
	OutputStream os = null;
	ByteArrayInputStream bais = null;
	ByteArrayOutputStream baos = null;
	BufferedReader br = null;
	BufferedWriter bw = null;
	AudioStream audio = null;

	// Constructor
	public ClientConnect(int port, String host) {
		this.tcpPortServer = port;
		this.udpPortServer = port;
		this.host = host;
		clientConnect();

		buf = new byte[15000];
		timer = new Timer(20, new timerListener());
		// timer.setInitialDelay(0);
		timer.setCoalesce(true);
		state = INITIALIZING;
		connSeqNum = 1;
	}

	public void clientConnect() {
		try {
			// TODO Remove println
			int count = 0;
			System.out.println("Creating Connection");
			tcpSocket = new Socket(host, tcpPortServer);
			System.out.println("Client TCP Socket Created");

			// Initiate UDP connection with server, send datagram then wait for
			// response
			udpSocket = new DatagramSocket();
			udpSocket.setSoTimeout(100);
			udpAddrServer = InetAddress.getByName(host);
			byte[] buf = new byte[5];
			sendPacket = new DatagramPacket(buf, buf.length, udpAddrServer,
					tcpPortServer);
			udpSocket.send(sendPacket);

			// Receive reply back from server
			recvPacket = new DatagramPacket(buf, buf.length);
			System.out.println("Waiting for Server Response");
			while (!datagramReceive()) {
				count++;
				if (count > 10) {
					System.out.println("Resending Packet");
					udpSocket.send(sendPacket);
				}
			}
			System.out.println("Client UDPSocket Created");

			is = tcpSocket.getInputStream();
			os = tcpSocket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new OutputStreamWriter(os));

		} catch (UnknownHostException e) {
			System.out.println("Error Connecting");
			System.out.println("Client clientConnect UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error Connecting");
			System.out.println("Client clientServer IOException");
			e.printStackTrace();
		}
	}

	private boolean datagramReceive() {
		try {
			udpSocket.receive(recvPacket);
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public void run() {
		// stuff in main client.java
		// ask for request type
		msg = "INIT";
		send(msg);
		if (parseRequest() != INIT) {
			System.out.println("Invalid Server Response");
		} else {
			state = WAITING;
		}
		msg = "PLAY";
		send(msg);
		if (parseRequest() != RECEIVED) {
			System.out.println("Invalid Server Response");
		} else {
			state = STREAMING;
			timer.start();
			while (true) {
			}
			/*
			 * try { Thread.currentThread().sleep(1000); } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}
	}

	public void send(String request) {
		try {
			// os.writeChars(msg);
			// os.writeBytes(msg);
			// TODO userId required
			int userIdent;
			if (request.equals("INIT")) {
				userIdent = 0;
			} else if (request.equals("HALT")) {
				userIdent = 0;
			} else if (request.equals("DISCONNECT")) {
				userIdent = 0;
			} else {
				userIdent = 3;
			}
			String message = nodeId + "\n" + request + "\n" + userIdent + "\n";
			// String message1 = "0\nINIT\n0";
			System.out.println(nodeId + " " + request + " " + userIdent + " ");
			// System.out.println(message1);
			bw.write(message);
			bw.flush();
			System.out.println("Message sent");
		} catch (IOException e) {
			System.out.println("Client clientConnect send\n");
			e.printStackTrace();
		}
	}

	private int parseRequest() {
		System.out.println("Client - Server Msg Received");
		String requestState = null;
		int serverId = 0;
		int userId = 0;
		int request = -1;
		try {
			serverId = Integer.parseInt(br.readLine());
			requestState = br.readLine();
			userId = Integer.parseInt(br.readLine());
			System.out.println("Client - Server Msg: " + serverId + " "
					+ requestState + " " + userId);

			if (requestState.equals("INIT")) {
				request = INIT;
				nodeId = userId;
			} else if (requestState.equals("PLAY")) {
				request = PLAY;
			} else if (requestState.equals("SWITCH")) {
				request = SWITCH;
			} else if (requestState.equals("HALT")) {
				request = HALT;
			} else if (requestState.equals("DISCONNECT")) {
				request = DISCONNECT;
			} else if (requestState.equals("RECEIVED")) {
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

	class timerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// System.out.println("Performing ActionListener");

			// Construct a DatagramPacket to receive data from the UDP socket
			recvPacket = new DatagramPacket(buf, buf.length);

			try {
				System.out.println("Waiting to Receive Datagram");
				// receive the DP from the socket:
				udpSocket.receive(recvPacket);

				// create an packet object from the DP
				Packet packet = new Packet(recvPacket.getData(),
						recvPacket.getLength());
				// print important header fields of the RTP packet received:
				System.out.println("Received Packet with SeqNum # "
						+ packet.getSeqNum());

				// get the payload bitstream from the RTPpacket object
				int payloadLen = packet.getpayload_length();
				byte[] payload = new byte[payloadLen];
				packet.getpayload(payload);

				// TODO stream payload audio data
			} catch (InterruptedIOException iioe) {
				System.out.println("Nothing to read");
			} catch (IOException ioe) {
				System.out.println("Exception caught: " + ioe);
			}
		}
	}

	public void closeConnection() {
		try {
			tcpSocket.close();
			udpSocket.close();
			System.out.println("Connection Closed");
		} catch (IOException e) {
			System.out.println("Error Closing Connection");
			e.printStackTrace();
		}
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getNodeId() {
		return nodeId;
	}
}