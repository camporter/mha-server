package myhomeaudio.server.discovery;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;

public class DiscoveryDescription implements Comparable<DiscoveryDescription> {

	private String instanceName;
	private int clientPort;
	private int nodePort;
	private InetAddress address;

	public DiscoveryDescription(String instanceName, int clientPort, int nodePort, InetAddress address) {
		this.instanceName = instanceName;
		this.clientPort = clientPort;
		this.nodePort = nodePort;
		this.address = address;
	}
	
	public DiscoveryDescription() {
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress serviceAddress) {
		this.address = serviceAddress;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String serviceDescription) {
		this.instanceName = serviceDescription;
	}

	protected String getEncodedInstanceName() {
		try {
			return URLEncoder.encode(getInstanceName(), "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			return null;
		}
	}
	
	public int getClientPort() {
		return clientPort;
	}
	public int getNodePort() {
		return nodePort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
	public void setNodePort(int nodePort) {
		this.nodePort = nodePort;
	}
	
	/**
	 * Get the string representation of the description.
	 * 
	 * @return Concatenated string with all the properties.
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(getEncodedInstanceName());
		buf.append(" ");
		buf.append(getAddress().getHostAddress());
		buf.append(" ");
		buf.append(Integer.toString(clientPort));
		buf.append(" ");
		buf.append(Integer.toString(nodePort));
		return buf.toString();
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof DiscoveryDescription)) {
			return false;
		}
		DiscoveryDescription descriptor = (DiscoveryDescription) o;
		return descriptor.getInstanceName().equals(getInstanceName());
	}

	public int hashCode() {
		return getInstanceName().hashCode();
	}

	public int compareTo(DiscoveryDescription sd) throws ClassCastException {
		if (sd == null) {
			throw new NullPointerException();
		}
		if (sd == this) {
			return 0;
		}

		return getInstanceName().compareTo(sd.getInstanceName());
	}

	public static DiscoveryDescription parse(String encodedInstanceName, String addressAsString,
			String clientPortAsString, String nodePortAsString) {

		DiscoveryDescription descriptor = new DiscoveryDescription();
		try {
			String name = URLDecoder.decode(encodedInstanceName, "UTF-8");
			if (name == null || name.length() == 0) {
				return null;
			}
			descriptor.setInstanceName(name);
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			return null;
		}

		try {
			InetAddress addr = InetAddress.getByName(addressAsString);
			descriptor.setAddress(addr);
		} catch (UnknownHostException uhe) {
			System.err.println("Unexpected exception: " + uhe);
			uhe.printStackTrace();
			return null;
		}

		try {
			int p = Integer.parseInt(clientPortAsString);
			descriptor.setClientPort(p);
			p = Integer.parseInt(nodePortAsString);
			descriptor.setNodePort(p);
		} catch (NumberFormatException nfe) {
			System.err.println("Unexpected exception: " + nfe);
			nfe.printStackTrace();
			return null;
		}

		return descriptor;
	}
}
