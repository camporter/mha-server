package myhomeaudio.server.discovery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * This Runnable responds to discovery searches with replies and the description
 * of the server's important information, stored in a DiscoveryDescription.
 * 
 * @author Cameron
 * 
 */
public final class DiscoveryResponder implements Runnable {

	protected InetAddress broadcastAddress;
	protected int broadcastPort;

	protected String serviceName;
	protected DiscoveryDescription descriptor;
	protected boolean continueThread = true;
	protected DatagramSocket socket;
	protected DatagramPacket queuedPacket;
	protected DatagramPacket receivedPacket;
	protected Thread responderThread;

	public DiscoveryResponder(String serviceName, DiscoveryDescription descriptor) {
		this.serviceName = serviceName;
		this.descriptor = descriptor;
		
		this.broadcastPort = DiscoveryConstants.BROADCAST_PORT;
		this.broadcastAddress = getBroadcastAddress();
		if (broadcastAddress == null)
			System.exit(1);
		
		try {
			this.socket = new DatagramSocket(broadcastPort);
			this.socket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public boolean isAlive() {
		return continueThread;
	}

	public String getServiceName() {
		return serviceName;
	}

	protected String getEncodedServiceName() {
		try {
			return URLEncoder.encode(getServiceName(), "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			return null;
		}
	}

	public void startResponder() {
		if (responderThread == null || !responderThread.isAlive()) {
			continueThread = true;
			responderThread = new Thread(this, "DiscoveryResponder");
			responderThread.setDaemon(true);
			responderThread.start();
		}
	}

	public void stopResponder() {
		if (responderThread != null && responderThread.isAlive()) {
			continueThread = false;
			responderThread.interrupt();
		}
	}

	protected void sendQueuedPacket() {
		if (queuedPacket == null) {
			return;
		}
		try {
			socket.send(queuedPacket);
			queuedPacket = null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void run() {

		while (continueThread) {

			byte[] buf = new byte[DiscoveryConstants.DATAGRAM_LENGTH];
			receivedPacket = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(receivedPacket); // note a timeout in effect

				if (isQueryPacket()) {
					DatagramPacket replyPacket = getReplyPacket();
					queuedPacket = replyPacket;
					sendQueuedPacket();
				}
			} catch (SocketTimeoutException ste) {
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

		}
		socket.close();
	}

	protected boolean isQueryPacket() {
		if (receivedPacket == null) {
			return false;
		}

		String dataStr = new String(receivedPacket.getData());
		int pos = dataStr.indexOf((char) 0);
		if (pos > -1) {
			dataStr = dataStr.substring(0, pos);
		}

		if (dataStr.startsWith(DiscoveryConstants.SEARCH_HEADER + getEncodedServiceName())) {
			return true;
		}

		return false;
	}

	protected DatagramPacket getReplyPacket() {
		StringBuffer buf = new StringBuffer();
		try {
			buf.append(DiscoveryConstants.REPLY_HEADER + getEncodedServiceName() + " ");
			buf.append(descriptor.toString());
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		}

		byte[] bytes = buf.toString().getBytes();
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
		packet.setAddress(broadcastAddress);
		packet.setPort(broadcastPort);

		return packet;
	}

	public void addShutdownHandler() {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				stopResponder();
			}
		});
	}

	/**
	 * Tries to get the local broadcast address from the network interfaces we
	 * want, so that we can listen on it for discovery packets.
	 * 
	 * @return
	 */
	protected InetAddress getBroadcastAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback())
					continue;
				if (!networkInterface.isUp())
					continue;
				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcastAddress = interfaceAddress.getBroadcast();
					if (broadcastAddress == null)
						continue;
					return broadcastAddress;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

}
