package myhomeaudio.node;

import java.net.InetAddress;

import myhomeaudio.server.discovery.DiscoverySearch;
import myhomeaudio.server.discovery.DiscoverySearchListener;
import myhomeaudio.server.discovery.DiscoveryDescription;


public class ServerDiscovery implements DiscoverySearchListener {
	
	DiscoverySearch browser;
	DiscoveryDescription descriptor;
	
	
	public ServerDiscovery() {
		browser = new DiscoverySearch();
		browser.addServiceBrowserListener(this);
		browser.setServiceName("myhomeaudio");
	}
	
	public boolean doDiscovery() {
		browser.startListener();
		browser.startLookup();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Nothing
		}
		
		browser.stopLookup();
		browser.stopListener();
		
		if (descriptor == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public int getNodePort() {
		if (descriptor != null)
			return descriptor.getNodePort();
		else
			return -1;
	}
	
	public InetAddress getAddress() {
		if (descriptor != null)
			return descriptor.getAddress();
		else
			return null;
	}

	@Override
	public void serviceReply(DiscoveryDescription descriptor) {
		this.descriptor = descriptor;
		
	}

}
