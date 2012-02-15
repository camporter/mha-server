package myhomeaudio.server.discovery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

public final class DiscoveryResponder implements Runnable {

	protected static InetAddress multicastAddress;
	protected static int multicastPort;

	static {
		try {
			multicastAddress = InetAddress.getByName(DiscoveryConstants.MULTICAST_ADDRESS);
			multicastPort = DiscoveryConstants.MULTICAST_PORT;
		} catch (UnknownHostException uhe) {
			System.err.println("Unexpected exception: " + uhe);
			uhe.printStackTrace();
		}
	}

	protected String serviceName;
	protected DiscoveryDescription descriptor;
	protected boolean continueThread = true;
	protected MulticastSocket socket;
	protected DatagramPacket queuedPacket;
	protected DatagramPacket receivedPacket;
	protected Thread responderThread;

	public DiscoveryResponder(String serviceName, DiscoveryDescription descriptor) {
		this.serviceName = serviceName;
		this.descriptor = descriptor;
		try {
			socket = new MulticastSocket(multicastPort);
			socket.joinGroup(multicastAddress);
			socket.setSoTimeout(DiscoveryConstants.RESPONDER_SOCKET_TIMEOUT);

		} catch (IOException ioe) {
			ioe.printStackTrace();
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
		packet.setAddress(multicastAddress);
		packet.setPort(multicastPort);

		return packet;
	}

	public void addShutdownHandler() {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				stopResponder();
			}
		});
	}

}
