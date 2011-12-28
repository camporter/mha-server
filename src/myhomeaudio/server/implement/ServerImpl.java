/**
 * 
 */
package myhomeaudio.server.implement;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import myhomeaudio.server.interfaces.NodeInterface;
import myhomeaudio.server.interfaces.ServerInterface;
import myhomeaudio.server.interfaces.StreamInterface;
import myhomeaudio.server.interfaces.UserInterface;

/**
 * @author Ryan Brown
 * 
 */
public class ServerImpl implements ServerInterface {

	// Networking variables
	protected static int tcpPort = 9090;
	protected static int udpPort = 9080;
	protected InetAddress ipAddr;
	protected InetAddress ipBroadcast;
	private Socket tcpSocket;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.ServerInterface#addUser(myhomeaudio.server
	 * .interfaces.UserInterface)
	 */
	@Override
	public boolean addUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see myhomeaudio.server.interfaces.ServerInterface#connect()
	 */
	@Override
	public boolean connect() {

		try {
			// Gets IP address, Uses the information to create a
			// NetworkInterface that
			// will create a broadcast IP address based on the IP of the host
			// therefore the broadcast will stay within the network
			ipAddr = InetAddress.getLocalHost();
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddr);
			List<InterfaceAddress> interfaceAddrCollection = networkInterface
					.getInterfaceAddresses();
			InterfaceAddress interfaceAddr = interfaceAddrCollection.get(0);
			ipBroadcast = interfaceAddr.getBroadcast();
			System.out.println(ipBroadcast.getHostAddress());
			// Create UDP packet and broadcast to network
			DatagramSocket udpSocket = new DatagramSocket();
			udpSocket.setSoTimeout(100);
			byte[] buf = new byte[5];// empty data to be sent
			DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ipBroadcast, udpPort);
			udpSocket.send(sendPacket);

			// Receive reply back from server
			DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
			System.out.println("Waiting for Server Response");
			int count = 0;
			while (!datagramReceive(udpSocket, recvPacket)) {
				count++;
				if (count > 10) {
					System.out.println("Resending Packet");
					udpSocket.send(sendPacket);
					count = 0;
				}
			}
			System.out.println("Client UDPSocket Created");
			ipAddr = udpSocket.getInetAddress();
			return attemptConnect();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.ServerInterface#connect(java.lang.String)
	 */
	@Override
	public boolean connect(String ipAddress) {
		try {
			ipAddr = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.ServerInterface#connectToNode(java.lang
	 * .String)
	 */
	@Override
	public NodeInterface connectToNode(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see myhomeaudio.server.interfaces.ServerInterface#disconnect()
	 */
	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see myhomeaudio.server.interfaces.ServerInterface#getAvailableStreams()
	 */
	@Override
	public StreamInterface[] getAvailableStreams() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see myhomeaudio.server.interfaces.ServerInterface#listUsers()
	 */
	@Override
	public UserInterface[] listUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.ServerInterface#login(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public UserInterface login(String username, String deviceID, String hashedPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.ServerInterface#removeUser(myhomeaudio.
	 * server.interfaces.UserInterface)
	 */
	@Override
	public boolean removeUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.ServerInterface#updateUser(myhomeaudio.
	 * server.interfaces.UserInterface)
	 */
	@Override
	public boolean updateUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Connects to the server
	 * 
	 * @param
	 * 
	 * @return true if successful connect
	 */
	public boolean attemptConnect() {

		// Creates TCP Connection for HTTP requests
		System.out.println("Creating Connection");
		try {
			this.tcpSocket = new Socket(ipAddr, tcpPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
		System.out.println("Client TCP Socket Created");
		return true;
	}

	private boolean datagramReceive(DatagramSocket udpSocket, DatagramPacket recvPacket) {
		try {
			udpSocket.receive(recvPacket);
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
