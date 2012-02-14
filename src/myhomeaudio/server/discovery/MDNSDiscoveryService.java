package myhomeaudio.server.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.jmdns.JmDNS;
import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.JmmDNSImpl;

public class MDNSDiscoveryService extends Thread {

	public static final String DISCOVERY_TYPE = "_myhomeaudio._tcp.local.";

	public MDNSDiscoveryService() {

	}
	
	public void run() {
		System.out.println("** Starting Discovery Service...");
		try {
			// Discovery must be set on all possible network interfaces, so go
			// through them all
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {
				// Each interface can have multiple addresses
				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
				for (InetAddress inetAddress : Collections.list(inetAddresses)) {
					// Ignore loopback or link-local
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
						JmDNS jmdns = JmDNS.create(inetAddress, null);

						Random random = new Random();
						byte[] randName = new byte[20];
						random.nextBytes(randName);
						System.out.println("Setting discovery on " + inetAddress.getHostAddress()
								+ " ...");
						ServiceInfo mhaService = ServiceInfo.create(DISCOVERY_TYPE,
								"test", 8080, "test");
						// Register the mDNS service
						jmdns.registerService(mhaService);
					}
				}
			}
			
			while (!Thread.interrupted()) {
				// Sit with the service on
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Problem starting the Discovery Service!");
		}
	}

	private static final char[] _nibbleToHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };

	private static String toHex(byte[] code) {
		StringBuilder result = new StringBuilder(2 * code.length);

		for (int i = 0; i < code.length; i++) {
			int b = code[i] & 0xFF;
			result.append(_nibbleToHex[b / 16]);
			result.append(_nibbleToHex[b % 16]);
		}

		return result.toString();
	}
}
