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
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public final class DiscoverySearch implements Runnable {

	protected InetAddress broadcastAddress;
	protected int broadcastPort;

	protected String serviceName;
	protected boolean shouldRun = true;
	protected DatagramSocket socket;
	protected DatagramPacket queuedPacket;
	protected DatagramPacket receivedPacket;
	protected Vector<DiscoverySearchListener> listeners;
	protected Thread searchThread;
	protected Timer myTimer;

	public DiscoverySearch(String serviceName) {

		this.serviceName = serviceName;

		broadcastPort = DiscoveryConstants.SEARCH_BROADCAST_PORT;
		broadcastAddress = getBroadcastAddress();
		if (broadcastAddress == null)
			System.exit(1);

		try {
			this.socket = new DatagramSocket(broadcastPort);
			this.socket.setBroadcast(true);
			this.socket.setSoTimeout(DiscoveryConstants.SEARCH_SOCKET_TIMEOUT);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}

		listeners = new Vector<DiscoverySearchListener>();
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

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void addServiceBrowserListener(DiscoverySearchListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeServiceBrowserListener(DiscoverySearchListener l) {
		listeners.remove(l);
	}

	public void startLookup() {
		if (myTimer == null) {
			myTimer = new Timer("QueryTimer");
			myTimer.scheduleAtFixedRate(new QueryTimerTask(), 0L,
					DiscoveryConstants.SEARCH_QUERY_INTERVAL);
		}
	}

	public void startSingleLookup() {
		if (myTimer == null) {
			myTimer = new Timer("QueryTimer");
			myTimer.schedule(new QueryTimerTask(), 0L);
			myTimer = null;
		}
	}

	public void stopLookup() {
		if (myTimer != null) {
			myTimer.cancel();
			myTimer = null;
		}
	}

	protected void notifyReply(DiscoveryDescription descriptor) {
		for (DiscoverySearchListener l : listeners) {
			l.serviceReply(descriptor);
		}
	}

	public void startListener() {
		if (searchThread == null) {
			shouldRun = true;
			searchThread = new Thread(this, "DiscoverySearch");
			searchThread.start();
		}
	}

	public void stopListener() {
		if (searchThread != null) {
			shouldRun = false;
			searchThread.interrupt();
			searchThread = null;
		}
	}

	public void run() {
		System.out.println("RUN");
		while (shouldRun) {

			try {
				byte[] buf = new byte[DiscoveryConstants.DATAGRAM_LENGTH];
				receivedPacket = new DatagramPacket(buf, buf.length);
				System.out.println("Waiting to receive...");
				socket.receive(receivedPacket); // note timeout in effect

				System.out.println("Found a packet");
				if (isReplyPacket()) {

					DiscoveryDescription descriptor;
					descriptor = getReplyDescriptor();
					if (descriptor != null) {
						notifyReply(descriptor);
						receivedPacket = null;
					}

				}

			} catch (SocketTimeoutException ste) {
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			sendQueuedPacket();

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

	protected boolean isReplyPacket() {
		if (receivedPacket == null) {
			return false;
		}

		String dataStr = new String(receivedPacket.getData());
		int pos = dataStr.indexOf((char) 0);
		if (pos > -1) {
			dataStr = dataStr.substring(0, pos);
		}

		if (dataStr.startsWith(DiscoveryConstants.REPLY_HEADER
				+ getEncodedServiceName())) {
			return true;
		}

		return false;
	}

	protected DiscoveryDescription getReplyDescriptor() {
		String dataStr = new String(receivedPacket.getData());
		int pos = dataStr.indexOf((char) 0);
		if (pos > -1) {
			dataStr = dataStr.substring(0, pos);
		}

		StringTokenizer tokens = new StringTokenizer(
				dataStr.substring(15 + getEncodedServiceName().length()));
		if (tokens.countTokens() == 3) {
			return DiscoveryDescription.parse(tokens.nextToken(),
					tokens.nextToken(), tokens.nextToken());
		} else {
			return null;
		}
	}

	protected DatagramPacket getQueryPacket() {
		StringBuffer buf = new StringBuffer();
		buf.append(DiscoveryConstants.SEARCH_HEADER + getEncodedServiceName());

		byte[] bytes = buf.toString().getBytes();
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
		packet.setAddress(broadcastAddress);
		packet.setPort(DiscoveryConstants.RESPONDER_BROADCAST_PORT);

		return packet;
	}

	private class QueryTimerTask extends TimerTask {

		public void run() {
			DatagramPacket packet = getQueryPacket();
			if (packet != null) {
				queuedPacket = packet;
			}
		}
	}

	protected InetAddress getBroadcastAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback())
					continue;
				if (!networkInterface.isUp())
					continue;
				for (InterfaceAddress interfaceAddress : networkInterface
						.getInterfaceAddresses()) {
					InetAddress broadcastAddress = interfaceAddress
							.getBroadcast();
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
