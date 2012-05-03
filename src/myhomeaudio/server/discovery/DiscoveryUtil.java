package myhomeaudio.server.discovery;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DiscoveryUtil {
	public static InetAddress getServerAddress() {
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
					InetAddress broadcastAddress = interfaceAddress.getAddress();
					if (broadcastAddress == null)
						continue;
					if (broadcastAddress.isAnyLocalAddress())
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
