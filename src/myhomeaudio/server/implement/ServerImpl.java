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
class ServerImpl implements ServerInterface {
	//Networking variables
	protected static int port = 9090;
	protected InetAddress ipAddr;
	protected InetAddress ipBroadcast;
	private Socket tcpSocket;
	
	
	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#addUser(myhomeaudio.server.interfaces.UserInterface)
	 */
	@Override
	public boolean addUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#connect()
	 */
	@Override
	public boolean connect() {
		
		try {
			ipAddr = InetAddress.getLocalHost();
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddr);
			List<InterfaceAddress> interfaceAddrCollection = networkInterface.getInterfaceAddresses();
			InterfaceAddress interfaceAddr = interfaceAddrCollection.get(0);
			ipBroadcast = interfaceAddr.getBroadcast();
			//Create UDP packet and broadcast to network
			//Wait for Server response 
			//Extract IP and call attemptConnect()
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#connect(java.lang.String)
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

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#connectToNode(java.lang.String)
	 */
	@Override
	public NodeInterface connectToNode(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#disconnect()
	 */
	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#getAvailableStreams()
	 */
	@Override
	public StreamInterface[] getAvailableStreams() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#listUsers()
	 */
	@Override
	public UserInterface[] listUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public UserInterface login(String username, String deviceID,
			String hashedPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#removeUser(myhomeaudio.server.interfaces.UserInterface)
	 */
	@Override
	public boolean removeUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#updateUser(myhomeaudio.server.interfaces.UserInterface)
	 */
	@Override
	public boolean updateUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/* Connects to the server
	 * @param
	 * @return true if successful connect
	 */
	public boolean attemptConnect(){
		
		//Creates TCP Connection for HTTP requests
		System.out.println("Creating Connection");
		try {
			tcpSocket = new Socket(ipAddr, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		System.out.println("Client TCP Socket Created");
		return true;
	}
}
